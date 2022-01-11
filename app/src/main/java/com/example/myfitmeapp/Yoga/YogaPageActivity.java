package com.example.myfitmeapp.Yoga;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myfitmeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class YogaPageActivity extends AppCompatActivity {

    private ProgressBar progressBar1,progressBar2,progressBar3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_page);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow_black);
        mToolbar.setNavigationOnClickListener(view ->
                onBackPressed());

        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);
        progressBar3 = findViewById(R.id.progress_bar3);

        ImageView stress = findViewById(R.id.stress);
        ImageView tone = findViewById(R.id.tone);
        ImageView posture = findViewById(R.id.posture);

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Drawables").child("yogaPage");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(YogaPageActivity.this)
                        .load(snapshot.child("stress").child("imageurl").getValue().toString())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(stress);

                Glide.with(YogaPageActivity.this)
                        .load(snapshot.child("tone").child("imageurl").getValue().toString())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar2.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar2.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(tone);

                Glide.with(YogaPageActivity.this)
                        .load(snapshot.child("posture").child("imageurl").getValue().toString())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar3.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar3.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(posture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        findViewById(R.id.cardView1).setOnClickListener(v -> startActivity(new Intent(YogaPageActivity.this, Yoga_BeginnerActivity.class)));
        findViewById(R.id.cardView2).setOnClickListener(v -> startActivity(new Intent(YogaPageActivity.this, Yoga_IntermediateActivity.class)));
        findViewById(R.id.cardView3).setOnClickListener(v -> startActivity(new Intent(YogaPageActivity.this, Yoga_AdvancedActivity.class)));
    }
}
