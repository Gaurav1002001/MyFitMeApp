package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash_PageActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String userId;
    FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        Button button = findViewById(R.id.getStartedButton);

        fStore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser == null) {
            button.postDelayed(() -> {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {
                    startActivity(new Intent(Splash_PageActivity.this,Main_PageActivity.class));
                    finish();
                });
            },1000);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUser != null) {
            userId = mUser.getUid();
            documentReference = fStore.collection("users").document(userId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(Splash_PageActivity.this,MainActivity.class));
                                finish();
                            }, 1000);
                            Log.d("Success", "Document exists!");
                        } else {
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(Splash_PageActivity.this,KnowUserActivity_B.class));
                                finish();
                            }, 1000);
                        }
                    } else {
                        Log.d("Failed", "Failed with: ", task.getException());
                    }
                }
            });
        }
    }
}
