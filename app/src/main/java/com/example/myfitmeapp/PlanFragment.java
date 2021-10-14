package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PlanFragment extends Fragment {

    public PlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        view.findViewById(R.id.meditationButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Meditation_PageActivity.class));
            }
        });

        view.findViewById(R.id.yogaButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),YogaPageActivity.class));
            }
        });

        view.findViewById(R.id.gymButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Gym_PageActivity.class));
            }
        });
        return view;
    }
}