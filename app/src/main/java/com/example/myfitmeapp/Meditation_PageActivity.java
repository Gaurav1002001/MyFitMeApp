package com.example.myfitmeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        /*findViewById(R.id.imageButton7).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$1$Meditation_PageActivity(view);
            }
        });

        findViewById(R.id.advancedbutton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$2$Meditation_PageActivity(view);
            }
        });

        findViewById(R.id.imageButton5).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$3$Meditation_PageActivity(view);
            }
        });

        findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$4$Meditation_PageActivity(view);
            }
        });

        findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$5$Meditation_PageActivity(view);
            }
        });

        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$6$Meditation_PageActivity(view);
            }
        });

        findViewById(R.id.imageButton1).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Meditation_PageActivity.this.lambda$onCreate$7$Meditation_PageActivity(view);
            }
        });*/
    }
}
