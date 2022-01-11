package com.example.myfitmeapp.Gym;

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

public class Gym_PageActivity extends AppCompatActivity {

    private ProgressBar progressBar1,progressBar2,progressBar3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_page);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view ->
                onBackPressed());

        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);
        progressBar3 = findViewById(R.id.progress_bar3);

        ImageView beginner = findViewById(R.id.gym_beginner);
        ImageView intermediate = findViewById(R.id.gym_intermediate);
        ImageView advanced = findViewById(R.id.gym_advanced);

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Drawables").child("gymPage");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(Gym_PageActivity.this)
                        .load(snapshot.child("beginner").getValue().toString())
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
                        .into(beginner);

                Glide.with(Gym_PageActivity.this)
                        .load(snapshot.child("intermediate").getValue().toString())
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
                        .into(intermediate);

                Glide.with(Gym_PageActivity.this)
                        .load(snapshot.child("advanced").getValue().toString())
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
                        .into(advanced);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        beginner.setOnClickListener(v -> startActivity(new Intent(Gym_PageActivity.this, Gym_BeginnerActivity.class)));
        intermediate.setOnClickListener(v -> startActivity(new Intent(Gym_PageActivity.this, Gym_IntermediateActivity.class)));
        advanced.setOnClickListener(v -> startActivity(new Intent(Gym_PageActivity.this, Gym_AdvancedActivity.class)));
    }
}
