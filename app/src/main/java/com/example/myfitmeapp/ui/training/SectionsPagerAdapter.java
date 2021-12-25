package com.example.myfitmeapp.ui.training;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myfitmeapp.AllFragment;
import com.example.myfitmeapp.DayFragment;
import com.example.myfitmeapp.Fragment_followers;
import com.example.myfitmeapp.Fragment_following;
import com.example.myfitmeapp.MonthFragment;
import com.example.myfitmeapp.WeekFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DayFragment();
                break;
            case 1:
                fragment = new WeekFragment();
                break;
            case 2:
                fragment = new MonthFragment();
            case 3:
                fragment = new AllFragment();
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "Day";
            case 1 :
                return "Week";
            case 2 :
                return "Month";
            case 3 :
                return "All";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}