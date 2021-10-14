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

public class Body_Activity extends AppCompatActivity {

    /*private MediaPlayer mediaPlayer;
    private ImageButton pauseButton;
    private ImageButton playButton;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        /*this.playButton = (ImageButton) findViewById(R.id.playButton);
        this.pauseButton = (ImageButton) findViewById(R.id.imageButton1);
        this.playButton.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Body_Activity.this.lambda$onCreate$0$Body_Activity(view);
            }
        });
        this.pauseButton.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Body_Activity.this.lambda$onCreate$1$Body_Activity(view);
            }
        });
        ((ImageButton) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Body_Activity.this.lambda$onCreate$2$Body_Activity(view);
            }
        });
    }

    public void lambda$onCreate$0$Body_Activity(View v) {
        playAudio();
        this.playButton.setVisibility(4);
        this.pauseButton.setVisibility(0);
    }

    public void lambda$onCreate$1$Body_Activity(View v) {
        pause();
        this.pauseButton.setVisibility(4);
        this.playButton.setVisibility(0);
    }

    public void lambda$onCreate$2$Body_Activity(View v) {
        new Intent(this, Meditation_PageActivity.class);
        finish();
    }

    private void playAudio() {
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        this.mediaPlayer = mediaPlayer2;
        mediaPlayer2.setAudioStreamType(3);
        try {
            this.mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/fitme-91d7f.appspot.com/o/body.mp3?alt=media&token=d7110c79-b480-4e78-a3d0-8838c4276138");
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
