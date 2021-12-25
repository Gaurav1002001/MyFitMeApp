package com.example.myfitmeapp;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ThankYou extends AppCompatActivity {

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    private ImageView done;

    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String userID,subString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        Bundle extras = getIntent().getExtras();
        ImageView imageView = findViewById(R.id.circle);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        String userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int iend = userName.indexOf("@");
        if (iend != -1)
        {
            subString= userName.substring(0 , iend);
        }
        String name = extras.getString("fullName");
        String gender = extras.getString("gender");
        String birthday = extras.getString("birthday");
        String height = extras.getString("height");
        String weight = extras.getString("weight");
        String bmi = extras.getString("bmi");

        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        done = findViewById(R.id.done);
        Drawable drawable = done.getDrawable();

        documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("userId",userID);
        user.put("userName",subString);
        user.put("fullName", name);
        user.put("gender", gender);
        user.put("birthday", birthday);
        user.put("height", height);
        user.put("weight", weight);
        user.put("bmi", bmi);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener() {
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
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ThankYou.this, MainActivity.class));
                    finish();
                }, 3000);
                Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(e ->
                Log.d("Failure","Failed"));
    }
}
