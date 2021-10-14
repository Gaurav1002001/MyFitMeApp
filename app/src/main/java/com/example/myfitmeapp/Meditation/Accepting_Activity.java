package com.example.myfitmeapp.Meditation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfitmeapp.R;

public class Accepting_Activity extends AppCompatActivity {

    /*String audioUrl;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    MediaPlayer mediaPlayer;
    ImageButton pauseBtn;
    ImageButton playBtn;*/
    
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepting);

        /*FirebaseDatabase instance = FirebaseDatabase.getInstance();
        this.firebaseDatabase = instance;
        DatabaseReference reference = instance.getReference("url");
        this.databaseReference = reference;
        reference.addValueEventListener(new ValueEventListener() {

            @Override // com.google.firebase.database.ValueEventListener
            public void onDataChange(DataSnapshot snapshot) {
                Accepting_Activity.this.audioUrl = (String) snapshot.getValue(String.class);
            }

            @Override // com.google.firebase.database.ValueEventListener
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Accepting_Activity.this, "Fail to get audio url.", Toast.LENGTH_SHORT).show();
            }
        });
        this.playBtn = (ImageButton) findViewById(R.id.playButton);
        this.pauseBtn = (ImageButton) findViewById(R.id.pauseButton);
        this.playBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Accepting_Activity accepting_Activity = Accepting_Activity.this;
                accepting_Activity.playAudio(accepting_Activity.audioUrl);
                Accepting_Activity.this.playBtn.setVisibility(4);
                Accepting_Activity.this.pauseBtn.setVisibility(0);
            }
        });
        this.pauseBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (Accepting_Activity.this.mediaPlayer.isPlaying()) {
                    Accepting_Activity.this.mediaPlayer.stop();
                    Accepting_Activity.this.mediaPlayer.reset();
                    Accepting_Activity.this.mediaPlayer.release();
                    Accepting_Activity.this.pauseBtn.setVisibility(4);
                    Accepting_Activity.this.playBtn.setVisibility(0);
                    Toast.makeText(Accepting_Activity.this, "Audio has been paused", 0).show();
                    return;
                }
                Toast.makeText(Accepting_Activity.this, "Audio has not played", 0).show();
            }
        });
        ((ImageButton) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                Accepting_Activity.this.lambda$onCreate$0$Accepting_Activity(view);
            }
        });
    }

    public void lambda$onCreate$0$Accepting_Activity(View v) {
        new Intent(this, Meditation_PageActivity.class);
        finish();
    }
    
    private void playAudio(String audioUrl2) {
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        this.mediaPlayer = mediaPlayer2;
        mediaPlayer2.setAudioStreamType(3);
        try {
            this.mediaPlayer.setDataSource(audioUrl2);
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            Toast.makeText(this, "Audio started playing..", 0).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error found is " + e, 0).show();
        }*/
    }
}
