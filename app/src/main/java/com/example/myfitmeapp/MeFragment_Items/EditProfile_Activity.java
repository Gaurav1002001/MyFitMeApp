package com.example.myfitmeapp.MeFragment_Items;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.example.myfitmeapp.Model.User;
import com.example.myfitmeapp.MyRadioButton;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile_Activity extends AppCompatActivity {

    private MyRadioButton acrbFemale;
    private MyRadioButton acrbMale;
    DatabaseReference databaseReference;
    String imageUrl,imageUrlCopy;
    private CircleImageView profileImageView;
    ProgressBar progressBar;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView saveProfile;
    private Uri imageUri;
    FirebaseUser user;
    EditText userBio;
    EditText userBirthday;
    EditText userEmail;
    EditText userHeight;
    EditText userLocation;
    EditText userName;
    EditText userPhone;
    EditText userWeight;
    ImageView yellowDot;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    private ProgressDialog mProgress;
    private CharSequence[] items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
        saveProfile = findViewById(R.id.saveProfile);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        yellowDot = findViewById(R.id.yellowDot);
        acrbMale = findViewById(R.id.acrb_male);
        acrbFemale = findViewById(R.id.acrb_female);
        mProgress = new ProgressDialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());

        saveProfile.setOnClickListener(view -> saveProfile());

        loadUserProfile();

        profileImageView.setOnClickListener(view -> {
            Select();
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
            userLocation.setEnabled(true);
        });

        final DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };

        textView3.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(EditProfile_Activity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            datePickerDialog.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
            datePickerDialog.getButton(-1).setTextColor(getResources().getColor(R.color.teal_200));
        });

        userHeight.setEnabled(true);
        userWeight.setEnabled(true);
        /*textView4.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(userHeight, InputMethodManager.SHOW_IMPLICIT);
            userHeight.setEnabled(true);
            userHeight.setFocusable(true);
            userWeight.setEnabled(true);
        });*/

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("imageurl")) {
                    items = new CharSequence[]{"Update Photo", "Remove Photo"};
                    imageUrlCopy = snapshot.child("imageurl").getValue().toString();
                } else {
                    items = new CharSequence[]{"Update Photo"};
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateDate() {
        String myFormat = "dd/MM/yy"; //put your date format in which you need to display
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        userBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    public void loadUserProfile() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                assert user != null;
                if (user.getImageurl() != null) {
                    Glide.with(getApplicationContext())
                            .load(user.getImageurl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(profileImageView);
                } else {
                    profileImageView.setImageResource(R.drawable.ic_avatar_recent_login);
                }

                userBio.setText(user.getBio());
                userName.setText(user.getFullname());
                userPhone.setText(user.getPhone());
                userLocation.setText(user.getLocation());
                userEmail.setText(user.getEmail());
                userBirthday.setText(user.getBirthday());
                if (user.getGender() != null && user.getGender().equals("Male")) {
                    acrbMale.setChecked(true);
                } else if (user.getGender() != null && user.getGender().equals("Female")) {
                    acrbFemale.setChecked(true);
                }
                /*userHeight.setText(user.getHeight());
                userWeight.setText(user.getWeight());*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Failed", "Failed retreiving document");
            }
        });
    }

    private void saveProfile() {
        mProgress.setMessage("Saving");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        String fullname = userName.getText().toString();
        String bio = userBio.getText().toString();
        String phone = userPhone.getText().toString();
        String location = userLocation.getText().toString();
        String g1 = acrbMale.getText().toString();
        String g2 = acrbFemale.getText().toString();
        String birthday = userBirthday.getText().toString();
        String height = userHeight.getText().toString();
        String weight = userWeight.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("fullname", fullname);
        user.put("bio", bio);
        user.put("phone", phone);
        user.put("location", location);
        if (acrbMale.isChecked()) {
            user.put("gender", g1);
        } else if (acrbFemale.isChecked()) {
            user.put("gender", g2);
        }
        user.put("birthday", birthday);
        /*user.put("height", height);
        user.put("weight", weight);*/

        databaseReference.updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                } else {
                    mProgress.dismiss();
                    Toast.makeText(EditProfile_Activity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Select() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    CropImage.activity().start(EditProfile_Activity.this);
                    break;
                case 1:
                    databaseReference.child("imageurl").removeValue();
                    if (imageUrlCopy != null) {
                        FirebaseStorage.getInstance().getReferenceFromUrl(imageUrlCopy).delete();
                    }
                    break;
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
            upload();
        } else {
            finish();
        }
    }

    private void upload() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating..");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        if (imageUri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("profileImages/").child("" + UUID.randomUUID().toString());

            StorageTask uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    databaseReference.child("imageurl").setValue(imageUrl)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditProfile_Activity.this, "Image Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }
    }
}

