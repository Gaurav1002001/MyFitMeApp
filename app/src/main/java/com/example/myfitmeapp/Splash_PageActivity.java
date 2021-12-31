package com.example.myfitmeapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.AccessToken;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash_PageActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser mUser;
    Button button;
    AccessToken accessToken;

    private RelativeLayout internetLayout;
    private RelativeLayout noInternetLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        button = findViewById(R.id.getStartedButton);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        accessToken = AccessToken.getCurrentAccessToken();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(mUser.getUid())) {
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(Splash_PageActivity.this,MainActivity.class));
                            finish();
                        }, 1000);
                    } else {
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(Splash_PageActivity.this,KnowUserActivity_B.class));
                            finish();
                        }, 1000);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            button.postDelayed(() -> {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {
                    startActivity(new Intent(Splash_PageActivity.this,Main_PageActivity.class));
                    finish();
                });
            },1000);
        }
    }
}
