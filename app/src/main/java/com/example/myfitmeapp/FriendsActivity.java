package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.myfitmeapp.databinding.ActivityFriendsBinding;
import com.example.myfitmeapp.ui.friends.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.myfitmeapp.databinding.ActivityFriendsBinding binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView imageButton = findViewById(R.id.addFriend);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this,AddFriend.class));
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Toolbar mToolbar = binding.friendToolbar;
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(ContextCompat.getColorStateList(this,R.color.goodgrey));
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.white));
    }
}