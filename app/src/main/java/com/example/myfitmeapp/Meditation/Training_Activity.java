package com.example.myfitmeapp.Meditation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myfitmeapp.Meditation_PageActivity;
import com.example.myfitmeapp.R;

import java.io.IOException;

public class Training_Activity extends AppCompatActivity {

    /*private MediaPlayer mediaPlayer;
    private ImageButton pauseButton;
    private ImageButton playButton;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        /*this.playButton = (ImageButton) findViewById(R.id.playButton);
        this.pauseButton = (ImageButton) findViewById(R.id.imageButton1);
        this.playButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Training_Activity.this.playAudio();
                Training_Activity.this.playButton.setVisibility(4);
                Training_Activity.this.pauseButton.setVisibility(0);
            }
        });
        this.pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Training_Activity training_Activity = Training_Activity.this;
                training_Activity.pause(training_Activity.pauseButton);
                Training_Activity.this.pauseButton.setVisibility(4);
                Training_Activity.this.playButton.setVisibility(0);
            }
        });
        ((ImageButton) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Intent(Training_Activity.this, Meditation_PageActivity.class);
                Training_Activity.this.finish();
            }
        });
    }

    private void playAudio() {
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        this.mediaPlayer = mediaPlayer2;
        mediaPlayer2.setAudioStreamType(3);
        try {
            this.mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/fitme-91d7f.appspot.com/o/breathing.mp3?alt=media&token=0199b1b6-6c23-4cff-b4bc-56f73c8360e0");
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing..", 0).show();
    }

    private void pause(View view) {
        this.mediaPlayer.pause();
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onPause() {
        super.onPause();
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.pause();
            if (isFinishing()) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
            }
        }*/
    }
}
