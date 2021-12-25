package com.example.myfitmeapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.login.Login;

public class Main_PageActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    boolean doubleBackToExitPressedOnce = false;

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
        }
    }

    /*@Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }*/
}
