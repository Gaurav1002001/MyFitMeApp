package com.example.myfitmeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myfitmeapp.Meditation.Accepting_Activity;
import com.example.myfitmeapp.Meditation.Body_Activity;
import com.example.myfitmeapp.Meditation.Breathing_Activity;
import com.example.myfitmeapp.Meditation.Connect_Activity;
import com.example.myfitmeapp.Meditation.Cultivative_Activity;
import com.example.myfitmeapp.Meditation.Stay_Activity;
import com.example.myfitmeapp.Meditation.Training_Activity;

public class Meditation_PageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation__page);

        Toolbar mToolbar = findViewById(R.id.toolbar2);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setTitle("Meditation Courses");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        findViewById(R.id.imageButton1).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Stay_Activity.class)));

        findViewById(R.id.imageButton2).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Body_Activity.class)));

        findViewById(R.id.imageButton3).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Breathing_Activity.class)));

        findViewById(R.id.imageButton4).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Training_Activity.class)));

        findViewById(R.id.imageButton5).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Accepting_Activity.class)));

        findViewById(R.id.imageButton6).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Cultivative_Activity.class)));

        findViewById(R.id.imageButton7).setOnClickListener(view ->
                startActivity(new Intent(Meditation_PageActivity.this, Connect_Activity.class)));
    }
}
