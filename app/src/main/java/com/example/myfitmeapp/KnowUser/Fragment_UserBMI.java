package com.example.myfitmeapp.KnowUser;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class Fragment_UserBMI extends Fragment {

    private TextView bmiResult;
    DecimalFormat decimalFormat;
    private TextInputEditText editHeight;
    private TextInputEditText editWeight;
    int heightValue = 175;
    int weightValue = 75;

    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_bmi, container, false);

        args = this.getArguments();
        decimalFormat = new DecimalFormat(".#");

        editHeight = view.findViewById(R.id.height);
        editWeight = view.findViewById(R.id.weight);
        bmiResult = view.findViewById(R.id.bmi_value);
        editHeight.addTextChangedListener(textWatcher);
        editWeight.addTextChangedListener(textWatcher);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        Button button = view.findViewById(R.id.btnNext);
        button.setOnClickListener(view1 -> saveProfile());
        return view;
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                editHeight.getText().toString();
                heightValue = Integer.parseInt(editHeight.getText().toString());
                editWeight.getText().toString();
                weightValue = Integer.parseInt(editWeight.getText().toString());
                editHeight.getText().toString();
            } catch (NumberFormatException e) {
                heightValue = 175;
                weightValue = 0;
            }
            calculateBmi();
        }

        public void afterTextChanged(Editable editable) {
        }
    };

    public void saveProfile() {

        String height = editHeight.getText().toString();
        String weight = editWeight.getText().toString();
        String bmi = bmiResult.getText().toString();

        if (editHeight.getText().toString().isEmpty() && editWeight.getText().toString().isEmpty() &&
                bmiResult.getText().toString().isEmpty()) {
            args.putString("height", "175cm");
            args.putString("weight", "75kg");
            args.putString("bmi", "24.5");
        } else if (!editHeight.getText().toString().isEmpty() && editWeight.getText().toString().isEmpty() &&
                bmiResult.getText().toString().isEmpty()) {
            args.putString("height", height);
            args.putString("weight", "75kg");
            args.putString("bmi", "24.5");
        } else if (editHeight.getText().toString().isEmpty() && editWeight.getText().toString().isEmpty() &&
                !bmiResult.getText().toString().isEmpty()) {
            args.putString("height", "175cm");
            args.putString("weight", "75kg");
            args.putString("bmi", bmi);
        } else if (editHeight.getText().toString().isEmpty() && !editWeight.getText().toString().isEmpty() &&
                bmiResult.getText().toString().isEmpty()) {
            args.putString("height", "175cm");
            args.putString("weight", weight);
            args.putString("bmi", "24.5");
        } else if (!editHeight.getText().toString().isEmpty() && !editWeight.getText().toString().isEmpty() &&
                bmiResult.getText().toString().isEmpty()) {
            args.putString("height", height);
            args.putString("weight", weight);
            args.putString("bmi", "24.5");
        } else if (!editHeight.getText().toString().isEmpty() && editWeight.getText().toString().isEmpty() &&
                !bmiResult.getText().toString().isEmpty()) {
            args.putString("height", height);
            args.putString("weight", "75kg");
            args.putString("bmi", bmi);
        } else if (editHeight.getText().toString().isEmpty() && !editWeight.getText().toString().isEmpty() &&
                !bmiResult.getText().toString().isEmpty()) {
            args.putString("height", "175cm");
            args.putString("weight", weight);
            args.putString("bmi", bmi);
        } else {
            args.putString("height", height);
            args.putString("weight", weight);
            args.putString("bmi", bmi);
        }
        Fragment_KUA_a fragment = new Fragment_KUA_a();
        fragment.setArguments(args);
        requireFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
    }

    public void calculateBmi() {
        double heightInMetres = ((double) heightValue) / 100.0d;
        double d = weightValue;
        bmiResult.setText(decimalFormat.format(d / (heightInMetres * heightInMetres)));
    }
}
