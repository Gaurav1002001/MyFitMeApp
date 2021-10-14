package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Splash_PageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        Button button = findViewById(R.id.getStartedButton);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(v -> {
                        startActivity(new Intent(Splash_PageActivity.this,Main_PageActivity.class));
                        finish();
                    });
                }
            },1000);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            new Handler().postDelayed(new Runnable() {

                public final void run() {
                    startActivity(new Intent(Splash_PageActivity.this,MainActivity.class));
                    finish();
                }
            }, 2000);
        }
    }
}
