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

import com.example.myfitmeapp.FriendsModel.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ThankYou extends AppCompatActivity {

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    private ImageView done;

    FirebaseUser fUser;
    DatabaseReference databaseReference;
    String subString;

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

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String userName = fUser.getEmail();
        int iend = userName.indexOf("@");
        if (iend != -1)
        {
            subString= userName.substring(0 , iend);
        }
        String name = extras.getString("fullname");
        String gender = extras.getString("gender");
        String birthday = extras.getString("birthday");
        String height = extras.getString("height");
        String weight = extras.getString("weight");
        String bmi = extras.getString("bmi");

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime());

        done = findViewById(R.id.done);
        Drawable drawable = done.getDrawable();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid());
        HashMap<String, Object> user = new HashMap<>();
        user.put("birthday", birthday);
        user.put("bmi",bmi);
        user.put("email",fUser.getEmail());
        user.put("fullname", name);
        user.put("gender", gender);
        user.put("height", height);
        user.put("userid",fUser.getUid());
        user.put("username",subString);
        user.put("weight", weight);
        user.put("joindate", currentDate);
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
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ThankYou.this, MainActivity.class));
                    finish();
                }, 3000);
            }
        }).addOnFailureListener(e ->
                Log.d("Failure","Failed"));

        FirebaseDatabase.getInstance().getReference().child("Follow").
                child((fUser.getUid())).child("following").child("Kgom6JB67RNyAw9gYN7DN40E81q1").setValue(true);

        FirebaseDatabase.getInstance().getReference().child("Follow").
                child("Kgom6JB67RNyAw9gYN7DN40E81q1").child("followers").child(fUser.getUid()).setValue(true);
    }
}
