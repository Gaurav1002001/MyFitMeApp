package com.example.myfitmeapp.TrainingStatus;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.myfitmeapp.R;
import com.example.myfitmeapp.databinding.ActivityTainingStatusBinding;
import com.example.myfitmeapp.tabbed_ui.training.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class TrainingStatusActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.myfitmeapp.databinding.ActivityTainingStatusBinding binding = ActivityTainingStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Toolbar mToolbar = binding.toolbarTop;
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        ImageView imageView = binding.stopwatch;
        imageView.setOnClickListener(v -> showAlertDialog());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrainingStatusActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_stopwatch, null);
        alertDialog.setView(customLayout);

        final TextView timeView = customLayout.findViewById(R.id.time_view);
        runTimer(timeView);

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }


    private void runTimer(TextView timeView) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
}