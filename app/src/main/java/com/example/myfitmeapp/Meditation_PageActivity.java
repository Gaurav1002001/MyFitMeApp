package com.example.myfitmeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.Adapter.MeditationAdapter;
import com.example.myfitmeapp.Model.Meditation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Meditation_PageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeditationAdapter meditationAdapter;
    private List<Meditation> mlist;
    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation__page);

        mProgress = findViewById(R.id.progress_bar);
        Toolbar mToolbar = findViewById(R.id.toolbar2);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setTitle("Meditation Courses");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mlist = new ArrayList<>();

        loadContent();
    }

    private void loadContent() {
        FirebaseDatabase.getInstance().getReference().child("Drawables").child("meditationPage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Meditation med = snapshot.getValue(Meditation.class);

                    mlist.add(med);
                }
                meditationAdapter = new MeditationAdapter(Meditation_PageActivity.this,mlist);
                mProgress.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(meditationAdapter);
                meditationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgress.setVisibility(View.INVISIBLE);
            }
        });
    }
}
