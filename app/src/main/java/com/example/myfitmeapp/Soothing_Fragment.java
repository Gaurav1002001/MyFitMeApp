package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Soothing_Fragment extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_soothing_);

        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());
    }
}
