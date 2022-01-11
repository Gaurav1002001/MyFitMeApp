package com.example.myfitmeapp.BottomNavFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myfitmeapp.OnFragmentInteractionListener;
import com.example.myfitmeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    boolean doubleBackToExitPressedOnce = false;

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new FeedFragment();
    final Fragment fragment3 = new ExploreFragment();
    final Fragment fragment4 = new PlanFragment();
    final Fragment fragment5 = new MeFragment();
    final FragmentManager fm = getSupportFragmentManager();
    public Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setItemIconTintList(null);

        fm.beginTransaction().add(R.id.nav_host_fragment, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment,fragment1, "1").commit();
        bottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_explore:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;

                case R.id.navigation_plan:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;

                case R.id.navigation_me:
                    fm.beginTransaction().hide(active).show(fragment5).commit();
                    active = fragment5;
                    return true;
            }
            return false;
        });
    }

    public void changeFragment(int id){
        if (id == 1) {
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
        }
        else if (id == 2) {
            fm.beginTransaction().hide(active).show(fragment3).commit();
            active = fragment3;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}