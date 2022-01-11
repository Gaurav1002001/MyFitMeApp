package com.example.myfitmeapp.KnowUser;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.myfitmeapp.R;

public class Fragment_KUA_b extends Fragment {

    Bundle args;
    private Button button;

    private final CheckBox[] checkBoxes = new CheckBox[4];

    public Fragment_KUA_b() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_know_user_b, container, false);

        args = this.getArguments();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        button = view.findViewById(R.id.buttonContinue);

        checkBoxes[0] = (CheckBox) view.findViewById(R.id.button_notActive);
        checkBoxes[0].setOnClickListener(mListener);
        checkBoxes[1] = (CheckBox) view.findViewById(R.id.button_slightlyActive);
        checkBoxes[1].setOnClickListener(mListener);
        checkBoxes[2] = (CheckBox) view.findViewById(R.id.button_active);
        checkBoxes[2].setOnClickListener(mListener);
        checkBoxes[3] = (CheckBox) view.findViewById(R.id.button_veryActive);
        checkBoxes[3].setOnClickListener(mListener);

        if (checkBoxes[0].isChecked() || checkBoxes[1].isChecked() || checkBoxes[2].isChecked() | checkBoxes[3].isChecked()) {
            button.setEnabled(true);
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yoga_green)));
        }

        button.setOnClickListener(v -> {
            Fragment_KUA_c fragment_c = new Fragment_KUA_c();
            fragment_c.setArguments(args);
            requireFragmentManager().beginTransaction().replace(R.id.container, fragment_c, null).addToBackStack(null).commit();
        });
        return view;
    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final int checkedId = v.getId();
            for (final CheckBox current : checkBoxes) {
                if (current.getId() == checkedId) {
                    current.setChecked(true);
                    button.setEnabled(true);
                    button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yoga_green)));
                } else {
                    current.setChecked(false);
                }
            }
        }
    };
}