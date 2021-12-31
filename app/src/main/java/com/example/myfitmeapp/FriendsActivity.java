package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.myfitmeapp.databinding.ActivityFriendsBinding;
import com.example.myfitmeapp.ui.friends.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FriendsActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.myfitmeapp.databinding.ActivityFriendsBinding binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView imageButton = findViewById(R.id.addFriend);

        id = getIntent().getStringExtra("id");
        if (id != null && !id.equals(user.getUid())){
            imageButton.setVisibility(View.GONE);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this,AddFriend.class));
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), id);
        Toolbar mToolbar = binding.friendToolbar;
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(ContextCompat.getColorStateList(this,R.color.goodgrey));
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.white));
        TabLayout.Tab tab = tabs.getTabAt(getIntent().getIntExtra("position",0));
        tab.select();
    }
}