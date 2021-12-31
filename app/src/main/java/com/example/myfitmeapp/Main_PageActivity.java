package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class Main_PageActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.mainPage_Container) != null && savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.mainPage_Container, new Login_PageFragment(), "loginPage").commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
