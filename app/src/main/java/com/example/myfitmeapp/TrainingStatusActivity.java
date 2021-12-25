package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.myfitmeapp.databinding.ActivityTainingStatusBinding;
import com.example.myfitmeapp.ui.training.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TrainingStatusActivity extends AppCompatActivity {

    private ActivityTainingStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTainingStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Toolbar mToolbar = binding.toolbarTop;
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }
}