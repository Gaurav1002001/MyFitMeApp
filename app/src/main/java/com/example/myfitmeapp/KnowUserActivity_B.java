package com.example.myfitmeapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class KnowUserActivity_B extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    DocumentReference documentReference;
    boolean doubleBackToExitPressedOnce = false;
    String emailID;
    FirebaseFirestore fStore;
    private EditText firstName;
    String imageUrl;
    private EditText lastName;
    String personEmail;
    String personFamilyName;
    String personGivenName;
    String previousID;
    private CircleImageView profileImage;
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri uri;
    FirebaseUser user;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_user_b);

        emailID = getIntent().getStringExtra("emailID");
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        firstName = findViewById(R.id.name);
        lastName = findViewById(R.id.sirName);
        Button signIn = findViewById(R.id.signIn);
        profileImage = findViewById(R.id.profile_Image);
        FirebaseStorage instance = FirebaseStorage.getInstance();
        storage = instance;
        storageReference = instance.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        previousID = userID;

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            firstName.setText(personGivenName);
            lastName.setText(personFamilyName);
            Glide.with(this).load(personPhoto).into(profileImage);
        }

        profileImage.setOnClickListener(view -> {
            if (checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 112);
            } else { Select(); }
        });

        signIn.setOnClickListener(view -> saveProfile());
    }

    private void saveProfile() {

        String name = firstName.getText().toString();
        String sirName = lastName.getText().toString();
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", name);
        user.put("lastName", sirName);
        user.put("email", personEmail);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                startActivity(new Intent(KnowUserActivity_B.this,Know_UserActivity.class));
                Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(e ->
                Log.d("Failure","Failed"));
    }

    private void Select() {
        CharSequence[] items = {"Take Photo", "Choose from Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0 :
                    cameraIntent();
                    break;
                case 1 :
                    galleryIntent();
                    break;
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put("title", "New Picture");
        values.put("description", "From the Camera");
        this.uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra("output", this.uri);
        startActivityForResult(cameraIntent, 102);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent, 105);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK ) {

            Bitmap bitmap = uriToBitmap(uri);
            profileImage.setImageBitmap(bitmap);
            uploadImage();
        }

        else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK  && data != null
                && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(KnowUserActivity_B.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(KnowUserActivity_B.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedFileUri, "r");
            Bitmap image = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadImage() {
        if (uri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("profileImages/" + UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        getDownloadUrl(ref);
                        progressDialog.dismiss();
                    })
                    .addOnFailureListener(e -> {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(KnowUserActivity_B.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    });
        }
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    documentReference.update("imageUrl",imageUrl);
                    setUserProfileUrl(uri);
                });
    }

    private void setUserProfileUrl(Uri uri) {

        user.updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build())
                .addOnCompleteListener(task -> Toast.makeText(KnowUserActivity_B.this, "Image Updated", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 1000);
    }
}
