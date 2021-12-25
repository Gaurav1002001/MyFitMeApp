package com.example.myfitmeapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private GoogleSignInAccount acct;
    private MyRadioButton acrbFemale;
    private MyRadioButton acrbMale;
    ImageButton button;
    DocumentReference documentReference;
    FirebaseFirestore fStore;
    String imageUrl;
    private CircleImageView profileImageView;
    ProgressBar progressBar;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    private Uri uri;
    FirebaseUser user;
    EditText userBio;
    EditText userBirthday;
    EditText userEmail;
    EditText userHeight;
    String userID;
    EditText userLocation;
    EditText userName;
    EditText userPhone;
    EditText userWeight;
    ImageView yellowDot;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.profile_Image);
        userName = findViewById(R.id.user_Name);
        userEmail = findViewById(R.id.user_Email);
        userPhone = findViewById(R.id.user_phone);
        userLocation = findViewById(R.id.user_location);
        userBirthday = findViewById(R.id.birthday);
        userHeight = findViewById(R.id.height);
        userWeight = findViewById(R.id.weight);
        userBio = findViewById(R.id.userBio);
        progressBar = findViewById(R.id.progress);
        button = findViewById(R.id.profile);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        yellowDot = findViewById(R.id.yellowDot);
        acrbMale = findViewById(R.id.acrb_male);
        acrbFemale = findViewById(R.id.acrb_female);

        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        acct = GoogleSignIn.getLastSignedInAccount(this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());

        button.setOnClickListener(view -> showProfileDialog());

        loadUserProfile();

        profileImageView.setOnClickListener(view -> {
            /*if (checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 112);
            } else { */
            Select(); //}
        });

        textView1.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(userBio, InputMethodManager.SHOW_IMPLICIT);
            userBio.setEnabled(true);
            userBio.setFocusable(true);
        });

        textView2.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(userPhone, InputMethodManager.SHOW_IMPLICIT);
            userPhone.setEnabled(true);
            userPhone.setFocusable(true);
            userEmail.setEnabled(true);
            userLocation.setEnabled(true);
        });

        final DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };

        textView3.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(Profile_Activity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            datePickerDialog.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
            datePickerDialog.getButton(-1).setTextColor(getResources().getColor(R.color.teal_200));
        });

        textView4.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(userHeight, InputMethodManager.SHOW_IMPLICIT);
            userHeight.setEnabled(true);
            userHeight.setFocusable(true);
            userWeight.setEnabled(true);
        });
    }

    private void updateDate() {
        String myFormat = "dd/MM/yy"; //put your date format in which you need to display
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        userBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    public void showProfileDialog() {
        CharSequence[] items = {"Save Profile", "Delete Profile", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Profile");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    saveProfile();
                    break;
                case 1:
                    deleteProfile();
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    public void loadUserProfile() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.INVISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(profileImageView);
            }
            documentReference.get().addOnSuccessListener(value -> {
                userBio.setText(value.getString("bio"));
                userName.setText(value.getString("fullName"));
                userPhone.setText(value.getString("phone"));
                userLocation.setText(value.getString("location"));
                userEmail.setText(value.getString("email"));
                userBirthday.setText(value.getString("birthday"));
                if (value.getString("gender") == null) {
                    acrbMale.setChecked(false);
                    acrbFemale.setChecked(false);
                } else if (value.getString("gender").equals("Male")) {
                    acrbMale.setChecked(true);
                } else {
                    acrbFemale.setChecked(true);
                }
                userHeight.setText(value.getString("height"));
                userWeight.setText(value.getString("weight"));
            })
                    .addOnFailureListener(e -> Log.d("Failed", "Failed retreiving document"));
        }
    }

    private void saveProfile() {
        String bio = userBio.getText().toString();
        String name = userName.getText().toString();
        String phone = userPhone.getText().toString();
        String email = userEmail.getText().toString().trim();
        String location = userLocation.getText().toString();
        String g1 = acrbMale.getText().toString();
        String g2 = acrbFemale.getText().toString();
        String birthday = userBirthday.getText().toString();
        String height = userHeight.getText().toString();
        String weight = userWeight.getText().toString();

        userID = user.getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("bio", bio);
        user.put("fullName", name);
        user.put("phone", phone);
        user.put("email", email);
        user.put("location", location);
        if (acrbMale.isChecked()) {
            user.put("gender", g1);
        } else {
            user.put("gender", g2);
        }
        user.put("birthday", birthday);
        user.put("height", height);
        user.put("weight", weight);
        documentReference.set(user).addOnSuccessListener((OnSuccessListener) obj -> Toast.makeText(Profile_Activity.this, "Profile Updated", Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                Toast.makeText(Profile_Activity.this, "Error updating profile" + e, Toast.LENGTH_SHORT).show());
    }

    private void deleteProfile() {

        Map<String, Object> updates = new HashMap<>();
        updates.put("bio", FieldValue.delete());
        updates.put("birthday", FieldValue.delete());
        updates.put("bmi", FieldValue.delete());
        updates.put("gender", FieldValue.delete());
        updates.put("height", FieldValue.delete());
        updates.put("location", FieldValue.delete());
        updates.put("phone", FieldValue.delete());
        updates.put("weight", FieldValue.delete());
        documentReference.update(updates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public final void onComplete(Task task) {
                Toast.makeText(Profile_Activity.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Select() {
        CharSequence[] items = {"Take Photo", "Choose from Library", "Remove Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    cameraIntent();
                    break;
                case 1:
                    galleryIntent();
                    break;
                case 2:
                    removePhoto();
                    break;
            }
        });
        builder.show();
    }

    private void removePhoto() {
        DocumentReference docRef = fStore.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.get("imageUrl") != null) {
                        String imageUrl = document.getString("imageUrl");
                        StorageReference strRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        strRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Profile_Activity.this, "Image deleted", Toast.LENGTH_LONG).show();
                                    replacePhoto();
                                } else {
                                    Toast.makeText(Profile_Activity.this, "Error deleting image", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Log.d("Delete","No Image");
                    }
                } else {
                    Toast.makeText(Profile_Activity.this, "Error deleting image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void replacePhoto() {

        if (acct.getPhotoUrl() != null) {
            Uri personPhoto = acct.getPhotoUrl();
            Glide.with(getApplicationContext()).load(personPhoto).into(profileImageView);
            user.updateProfile(new UserProfileChangeRequest.Builder()
                    .setPhotoUri(personPhoto).build())
                    .addOnCompleteListener(task -> {
                    });
        }
        Glide.with(getApplicationContext()).load(R.drawable.accepting_circle).into(profileImageView);

    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put("title", "New Picture");
        values.put("description", "From the Camera");
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra("output", uri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bitmap bitmap = uriToBitmap(uri);
            profileImageView.setImageBitmap(bitmap);
            uploadImage();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Profile_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(Profile_Activity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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
            progressDialog.setCanceledOnTouchOutside(false);

            StorageReference ref = storageReference.child("profileImages/" + UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        getDownloadUrl(ref);
                        progressDialog.dismiss();
                    })
                    .addOnFailureListener(e -> {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(Profile_Activity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    documentReference.update("imageUrl", imageUrl);
                    setUserProfileUrl(uri);
                });
    }

    private void setUserProfileUrl(Uri uri) {

        user.updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build())
                .addOnCompleteListener(task -> Toast.makeText(Profile_Activity.this, "Image Updated", Toast.LENGTH_SHORT).show());
    }
}

