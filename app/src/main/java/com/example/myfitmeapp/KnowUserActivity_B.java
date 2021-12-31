package com.example.myfitmeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class KnowUserActivity_B extends AppCompatActivity {

    private EditText firstName;
    FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_user_b);

        firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        Button signIn = findViewById(R.id.signIn);
        CircleImageView profileImage = findViewById(R.id.profile_Image);
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        firstName.setText(mUser.getDisplayName());
        if (mUser.getPhotoUrl() != null){
            Glide.with(this).load(mUser.getPhotoUrl()).into(profileImage);
        }
        signIn.setOnClickListener(view -> saveProfile());
    }

    private void saveProfile() {
        String name = firstName.getText().toString();
        Intent intent = new Intent(KnowUserActivity_B.this,Know_UserActivity.class);
        intent.putExtra("fullName",name);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
