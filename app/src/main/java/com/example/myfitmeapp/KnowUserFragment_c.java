package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class KnowUserFragment_c extends Fragment {

    private StateProgressBar stateProgressBar;

    public KnowUserFragment_c() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_know_user_c, container, false);

        stateProgressBar = requireActivity().findViewById(R.id.your_state_progress_bar);
        final TextInputLayout textInputLayout = view.findViewById(R.id.editLoginWrapper);
        final TextView counter = view.findViewById(R.id.counter);

        ImageButton backButton = requireActivity().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            }
        });

        view.findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), ThankYou.class));
                requireActivity().finish();
            }
        });

        CheckBox checkBox = view.findViewById(R.id.button_other);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textInputLayout.setVisibility(View.VISIBLE);
                textInputLayout.requestFocus();
                return;
            }
            textInputLayout.setVisibility(View.INVISIBLE);
            textInputLayout.setFocusable(false);
        });

        EditText editText = view.findViewById(R.id.editOther);
        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                counter.setText((100 - s.toString().length()) + "/100");
            }
        });
        return view;
    }
}