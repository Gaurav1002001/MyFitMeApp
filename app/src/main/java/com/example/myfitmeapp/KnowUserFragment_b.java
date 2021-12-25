package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.Objects;

public class KnowUserFragment_b extends Fragment {

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private StateProgressBar stateProgressBar;
    Bundle args;

    public KnowUserFragment_b() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_know_user_b, container, false);

        args = this.getArguments();
        stateProgressBar = requireActivity().findViewById(R.id.your_state_progress_bar);

        ImageButton imageButton = requireActivity().findViewById(R.id.backButton);
        imageButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        });

        checkBox1 = view.findViewById(R.id.button_notActive);
        checkBox2 = view.findViewById(R.id.button_slightlyActive);
        checkBox3 = view.findViewById(R.id.button_active);
        checkBox4 = view.findViewById(R.id.button_veryActive);

        checkBox1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                return;
            }
            checkBox2.setClickable(true);
            checkBox3.setClickable(true);
            checkBox4.setClickable(true);
        });

        checkBox2.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                return;
            }
            checkBox1.setClickable(true);
            checkBox3.setClickable(true);
            checkBox4.setClickable(true);
        });

        checkBox3.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
                return;
            }
            checkBox1.setClickable(true);
            checkBox2.setClickable(true);
            checkBox4.setClickable(true);
        });

        checkBox4.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                return;
            }
            checkBox1.setClickable(true);
            checkBox2.setClickable(true);
            checkBox3.setClickable(true);
        });

        Button button = view.findViewById(R.id.buttonContinue);
        button.setOnClickListener(v -> {
            KnowUserFragment_c fragment_c = new KnowUserFragment_c();
            fragment_c.setArguments(args);
            requireFragmentManager().beginTransaction().replace(R.id.container, fragment_c, null).addToBackStack(null).commit();
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
        });
        return view;
    }
}