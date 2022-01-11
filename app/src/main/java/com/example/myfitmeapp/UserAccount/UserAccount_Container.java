package com.example.myfitmeapp.UserAccount;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfitmeapp.R;

public class UserAccount_Container extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_container);

        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.mainPage_Container) != null && savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.mainPage_Container, new Fragment_LoginPage(), "loginPage").commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count != 0) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
