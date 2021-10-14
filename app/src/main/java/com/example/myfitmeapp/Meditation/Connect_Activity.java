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

public class Connect_Activity extends AppCompatActivity {

    /*private MediaPlayer mediaPlayer;
    private ImageButton pauseButton;
    private ImageButton playButton;*/

    /* access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_page);

        /*this.playButton = (ImageButton) findViewById(R.id.playButton);
        this.pauseButton = (ImageButton) findViewById(R.id.imageButton1);
        this.playButton.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Connect_Activity.this.lambda$onCreate$0$Connect_Activity(view);
            }
        });
        this.pauseButton.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Connect_Activity.this.lambda$onCreate$1$Connect_Activity(view);
            }
        });
        ((ImageButton) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Connect_Activity.this.lambda$onCreate$2$Connect_Activity(view);
            }
        });
    }

    public void lambda$onCreate$0$Connect_Activity(View v) {
        playAudio();
        this.playButton.setVisibility(4);
        this.pauseButton.setVisibility(0);
    }

    public void lambda$onCreate$1$Connect_Activity(View v) {
        pause();
        this.pauseButton.setVisibility(4);
        this.playButton.setVisibility(0);
    }

    public void lambda$onCreate$2$Connect_Activity(View v) {
        new Intent(this, Meditation_PageActivity.class);
        finish();
    }

    private void playAudio() {
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        this.mediaPlayer = mediaPlayer2;
        mediaPlayer2.setAudioStreamType(3);
        try {
            this.mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/fitme-91d7f.appspot.com/o/connect_to_life.mp3?alt=media&token=6f28e6b5-a14d-44e2-a016-bf1864148c71");
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing..", 0).show();
    }

    private void pause() {
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
