package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Morning_Fragment extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_morning_);

        Toolbar mToolbar = findViewById(R.id.backToolbar);
        mToolbar.setOnClickListener(view -> onBackPressed());
    }
}
