package com.example.myfitmeapp.Meditation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myfitmeapp.Meditation_PageActivity;
import com.example.myfitmeapp.R;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.security.CertificateUtil;
import com.google.firebase.firestore.util.ExponentialBackoff;
import java.io.IOException;

public class Stay_Activity extends AppCompatActivity {

    /*private Handler handler = new Handler();
    private ImageView imagePlayPause;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView textCurrentTime;
    private final Runnable updater = new Runnable() {

        public void run() {
            updateSeekBar();
            textCurrentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);

    }

        /*this.imagePlayPause = (ImageView) findViewById(R.id.imagePlayPause);
        this.textCurrentTime = (TextView) findViewById(R.id.textCurrentTime);
        this.seekBar = (SeekBar) findViewById(R.id.playerSeekbar);
        this.mediaPlayer = new MediaPlayer();
        this.imagePlayPause.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Stay_Activity.this.lambda$onCreate$0$Stay_Activity(view);
            }
        });
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public final void onCompletion(MediaPlayer mediaPlayer) {
                Stay_Activity.this.lambda$onCreate$1$Stay_Activity(mediaPlayer);
            }
        });
        ((ImageButton) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Stay_Activity.this.lambda$onCreate$2$Stay_Activity(view);
            }
        });
    }

    public void lambda$onCreate$0$Stay_Activity(View v) {
        if (this.mediaPlayer.isPlaying()) {
            this.handler.removeCallbacks(this.updater);
            this.mediaPlayer.pause();
            this.imagePlayPause.setImageResource(R.drawable.play);
            return;
        }
        if (this.mediaPlayer.getCurrentPosition() == 1) {
            prepareMediaPlayer();
        }
        this.mediaPlayer.start();
        this.imagePlayPause.setImageResource(R.drawable.pause);
        updateSeekBar();
    }

    public void lambda$onCreate$1$Stay_Activity(MediaPlayer mp) {
        this.imagePlayPause.setImageResource(R.drawable.play);
        this.textCurrentTime.setText(R.string.zero);
        this.mediaPlayer.reset();
        prepareMediaPlayer();
    }

    public void lambda$onCreate$2$Stay_Activity(View v) {
        new Intent(this, Meditation_PageActivity.class);
        finish();
    }

    private void prepareMediaPlayer() {
        try {
            this.mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/fitme-91d7f.appspot.com/o/stay.mp3?alt=media&token=ab555fa9-f4e9-4c95-b1a6-0db27aeccf13");
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSeekBar() {
        if (this.mediaPlayer.isPlaying()) {
            this.seekBar.setProgress((int) ((((float) this.mediaPlayer.getCurrentPosition()) / ((float) this.mediaPlayer.getDuration())) * 100.0f));
            this.handler.postDelayed(this.updater, 1000);
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String secondsString;
        String finalTimerString = "";
        int hours = (int) (milliseconds / 3600000);
        int minutes = ((int) (milliseconds % 3600000)) / 60000;
        int seconds = (int) (((milliseconds % 3600000) % ExponentialBackoff.DEFAULT_BACKOFF_MAX_DELAY_MS) / 1000);
        if (hours > 0) {
            finalTimerString = hours + CertificateUtil.DELIMITER;
        }
        if (seconds < 10) {
            secondsString = AppEventsConstants.EVENT_PARAM_VALUE_NO + seconds;
        } else {
            secondsString = "" + seconds;
        }
        return finalTimerString + minutes + CertificateUtil.DELIMITER + secondsString;
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.pause();
            if (isFinishing()) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
            }
        }
    }*/
}
