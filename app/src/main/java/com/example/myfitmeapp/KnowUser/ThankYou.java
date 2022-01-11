package com.example.myfitmeapp.KnowUser;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.myfitmeapp.BottomNavFragments.MainActivity;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class ThankYou extends AppCompatActivity {

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    FirebaseUser fUser;
    DatabaseReference databaseReference;
    String subString;
    private String imageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        Bundle extras = getIntent().getExtras();
        ImageView imageView = findViewById(R.id.circle);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid());

        String userName = fUser.getEmail();
        if (userName != null) {
            int iend = userName.indexOf("@");
            if (iend != -1) {
                subString = userName.substring(0, iend);
            }
        }

        String imageUri = getIntent().getStringExtra("imageUri");
        if (imageUri != null) {
            Uri uri = Uri.parse(extras.getString("imageUri"));

            upload(uri);
        }

        String fullname = extras.getString("fullname");
        String gender = extras.getString("gender");
        String birthday = extras.getString("birthday");
        String height = extras.getString("height");
        String weight = extras.getString("weight");
        String bmi = extras.getString("bmi");

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime());

        ImageView done = findViewById(R.id.done);
        Drawable drawable = done.getDrawable();

        HashMap<String, Object> user = new HashMap<>();
        user.put("birthday", birthday);
        user.put("bmi", bmi);
        user.put("email", fUser.getEmail());
        user.put("fullname", fullname);
        user.put("gender", gender);
        user.put("height", height);
        user.put("userid", fUser.getUid());
        if (subString != null) {
            user.put("username", subString);
        }
        user.put("weight", weight);
        user.put("joindate", currentDate);
        user.put("isUser", "true");
        databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    imageView.setVisibility(View.VISIBLE);
                    avd = (AnimatedVectorDrawableCompat) drawable;
                    avd.start();
                } else if (drawable instanceof AnimatedVectorDrawable) {
                    imageView.setVisibility(View.VISIBLE);
                    avd2 = (AnimatedVectorDrawable) drawable;
                    avd2.start();
                }
                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child((fUser.getUid())).child("following").child("Kgom6JB67RNyAw9gYN7DN40E81q1").setValue(true);

                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child("Kgom6JB67RNyAw9gYN7DN40E81q1").child("followers").child(fUser.getUid()).setValue(true);


                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ThankYou.this, MainActivity.class));
                    finish();
                }, 3000);
            }
        }).addOnFailureListener(e ->
                Log.d("Failure", "Failed"));
    }

    private void upload(Uri uri) {
        if (uri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("profileImages/").child("" + UUID.randomUUID().toString());

            StorageTask uploadTask = filePath.putFile(uri);
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

                    databaseReference.child("imageurl").setValue(imageUrl);
                }
            });
        }
    }
}
