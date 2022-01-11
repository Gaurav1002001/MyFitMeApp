package com.example.myfitmeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myfitmeapp.Model.Meditation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Meditation_ContentActivity extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private TextView time;

    private ProgressBar progressBar;
    private String audiourl;
    private String audioFilename;
    private MediaPlayer mediaPlayer;

    private ImageView playButton, pauseButton, backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_content);

        String id = getIntent().getStringExtra("id");
        mediaPlayer = new MediaPlayer();

        backImage = findViewById(R.id.backImage);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        time = findViewById(R.id.textCurrentTime);
        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                showAlertDialog();
            } else {
                finish();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Drawables").child("meditationPage").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Meditation med = snapshot.getValue(Meditation.class);

                        Glide.with(Meditation_ContentActivity.this).load(med.getBackImageurl()).into(backImage);
                        title.setText(med.getTitle());
                        description.setText(med.getDescription());
                        time.setText(med.getTime());
                        audiourl = med.getAudiourl();
                        audioFilename = med.getAudioFilename();

                        try {
                            mediaPlayer.setDataSource(audiourl);
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
            }
        });

        playButton = findViewById(R.id.imagePlay);
        playButton.setOnClickListener(v -> {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);

            mediaPlayer.start();
        });

        pauseButton = findViewById(R.id.imagePause);
        pauseButton.setOnClickListener(v -> {
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);

            mediaPlayer.pause();
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to end your current training?\nThe data will not be saved");
        dialog.setPositiveButton("End Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        }).setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            showAlertDialog();
        } else {
            super.onBackPressed();
        }
    }
}