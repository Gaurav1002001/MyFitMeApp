package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class Know_UserActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    private StateProgressBar stateProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know__user_activity);

        String name = getIntent().getStringExtra("fullName");
        stateProgressBar = findViewById(R.id.your_state_progress_bar);

        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.container) != null && savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.container, new UserGenderFragment(), null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        super.onBackPressed();
        switch (count) {
            case 0:
                return;
            case 1:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                return;
            case 2:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                return;
            case 3:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                return;
            case 4:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                return;
            default:
                getSupportFragmentManager().popBackStack();
        }
    }
}
