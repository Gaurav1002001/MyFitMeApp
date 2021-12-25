package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Health_NutritionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_nutrition);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}