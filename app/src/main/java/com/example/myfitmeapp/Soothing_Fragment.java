package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Soothing_Fragment extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_soothing_);

        Toolbar mToolabr = findViewById(R.id.backToolbar);
        mToolabr.setOnClickListener(view -> onBackPressed());
    }
}
