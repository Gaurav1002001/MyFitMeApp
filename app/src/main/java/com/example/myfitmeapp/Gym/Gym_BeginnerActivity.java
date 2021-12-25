package com.example.myfitmeapp.Gym;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myfitmeapp.R;

public class Gym_BeginnerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gym_beginner_page);

        Toolbar mToolbar = findViewById(R.id.toolbar2);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().getDecorView().setSystemUiVisibility(1280);
    }
}
