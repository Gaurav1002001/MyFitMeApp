package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private BottomNavigationView navView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        navView = requireActivity().findViewById(R.id.nav_view);

        view.findViewById(R.id.buttonTraining).setOnClickListener(v -> startActivity(new Intent(getActivity(),TrainingDaysActivity.class)));

        view.findViewById(R.id.buttonSessions).setOnClickListener(v -> {
            requireFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ExploreFragment()).commit();
            navView.getMenu().findItem(R.id.navigation_explore).setChecked(true);
        });

        return view;
    }
}
