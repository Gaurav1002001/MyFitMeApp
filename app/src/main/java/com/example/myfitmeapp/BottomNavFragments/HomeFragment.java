package com.example.myfitmeapp.BottomNavFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.OnFragmentInteractionListener;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.TrainingStatus.TrainingStatusActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private BottomNavigationView navView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        navView = requireActivity().findViewById(R.id.nav_view);

        view.findViewById(R.id.buttonTraining).setOnClickListener(v -> startActivity(new Intent(getActivity(), TrainingStatusActivity.class)));
        view.findViewById(R.id.yogaTraining).setOnClickListener(v -> startActivity(new Intent(getActivity(),TrainingStatusActivity.class)));
        view.findViewById(R.id.currentStreak).setOnClickListener(v -> startActivity(new Intent(getActivity(),TrainingStatusActivity.class)));

        view.findViewById(R.id.buttonSessions).setOnClickListener(v -> {
            mListener.changeFragment(2);
            navView.getMenu().findItem(R.id.navigation_explore).setChecked(true);
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
