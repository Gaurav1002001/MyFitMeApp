package com.example.myfitmeapp.KnowUser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfitmeapp.R;

public class Know_UserContainer extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know_user_container);

        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.container) != null && savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.container, new Fragment_UserNamePhoto(), null).commit();
        }
    }
}
