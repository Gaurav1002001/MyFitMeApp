package com.example.myfitmeapp.Yoga;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfitmeapp.R;

public class Yoga_BeginnerActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yogabeginner);

        Toolbar mToolbar = findViewById(R.id.toolbar2);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().getDecorView().setSystemUiVisibility(1280);
    }
}
