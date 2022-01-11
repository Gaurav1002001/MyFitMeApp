package com.example.myfitmeapp.ExploreFagment_Items;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfitmeapp.R;

public class India_Activity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india);
        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());
    }
}
