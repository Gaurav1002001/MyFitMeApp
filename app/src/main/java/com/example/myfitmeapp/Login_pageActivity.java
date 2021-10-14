package com.example.myfitmeapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Login_pageActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container) != null && savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new Login_PageFragment(), null);
            fragmentTransaction.commit();
        }
    }
}
