package com.example.myfitmeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class KnowUserFragment_a extends Fragment {

    private StateProgressBar stateProgressBar;
    Bundle args;

    public KnowUserFragment_a() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_know_user_a, container, false);

        args = this.getArguments();
        stateProgressBar = requireActivity().findViewById(R.id.your_state_progress_bar);

        ImageButton backButton = requireActivity().findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        });

        Button button = view.findViewById(R.id.buttonContinue);
        button.setOnClickListener(v -> {
            KnowUserFragment_b fragment_b = new KnowUserFragment_b();
            fragment_b.setArguments(args);
            requireFragmentManager().beginTransaction().replace(R.id.container, fragment_b, null).addToBackStack(null).commit();
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        });
        return view;
    }
}