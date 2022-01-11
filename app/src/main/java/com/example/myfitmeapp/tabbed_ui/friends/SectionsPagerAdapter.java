package com.example.myfitmeapp.tabbed_ui.friends;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myfitmeapp.Friends.Fragment_followers;
import com.example.myfitmeapp.Friends.Fragment_following;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private String id;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String id) {
        super(fm);
        this.mContext = context;
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        switch (position) {
            case 0:
                Fragment_followers fragmentFollowers = new Fragment_followers();
                fragmentFollowers.setArguments(bundle);
                return fragmentFollowers;
            case 1:
                Fragment_following fragmentFollowing = new Fragment_following();
                fragmentFollowing.setArguments(bundle);
                return fragmentFollowing;
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "followers ";
            case 1 :
                return "following ";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}