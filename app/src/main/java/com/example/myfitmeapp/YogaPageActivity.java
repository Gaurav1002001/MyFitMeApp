package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myfitmeapp.Yoga.Yoga_AdvancedActivity;
import com.example.myfitmeapp.Yoga.Yoga_BeginnerActivity;
import com.example.myfitmeapp.Yoga.Yoga_IntermediateActivity;

public class YogaPageActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_page);

        Toolbar mToolbar = findViewById(R.id.toolbar2);
        mToolbar.setNavigationIcon(R.drawable.back_arrow_black);
        mToolbar.setNavigationOnClickListener(view ->
                onBackPressed());

        findViewById(R.id.button4).setOnClickListener(view ->
                startActivity(new Intent(YogaPageActivity.this, Yoga_BeginnerActivity.class)));

        findViewById(R.id.button5).setOnClickListener(view ->
                startActivity(new Intent(YogaPageActivity.this, Yoga_IntermediateActivity.class)));

        findViewById(R.id.button6).setOnClickListener(view ->
                startActivity(new Intent(YogaPageActivity.this, Yoga_AdvancedActivity.class)));
    }
}
