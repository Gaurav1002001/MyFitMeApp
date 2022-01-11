package com.example.myfitmeapp.tabbed_ui.training;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myfitmeapp.TrainingStatus.Fragment_History;
import com.example.myfitmeapp.TrainingStatus.Fragment_Workout;
import com.example.myfitmeapp.TrainingStatus.Fragment_Graphs;
import com.example.myfitmeapp.TrainingStatus.Fragment_Program;

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
                fragment = new Fragment_Workout();
                break;
            case 1:
                fragment = new Fragment_Program();
                break;
            case 2:
                fragment = new Fragment_Graphs();
                break;
            case 3:
                fragment = new Fragment_History();
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "Workout";
            case 1 :
                return "Program";
            case 2 :
                return "Graphs";
            case 3 :
                return "History";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}