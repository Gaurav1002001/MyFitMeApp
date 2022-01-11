package com.example.myfitmeapp.KnowUser;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.MyRadioButton;
import com.example.myfitmeapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Fragment_UserGender extends Fragment {

    private MyRadioButton acrbFemale;
    private MyRadioButton acrbMale;
    private Button button;
    DatePickerDialog datePickerDialog;
    private EditText editText;
    final Calendar myCalendar = Calendar.getInstance();

    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_gender, container, false);

        args = this.getArguments();

        acrbMale = view.findViewById(R.id.acrb_male);
        acrbFemale = view.findViewById(R.id.acrb_female);
        button = view.findViewById(R.id.btnNext);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch (i) {
                case R.id.acrb_female:
                case R.id.acrb_male:
                    button.setEnabled(true);
                    button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yoga_green)));
                    return;
                default:
            }
        });

        final DatePickerDialog.OnDateSetListener date = (view12, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };

        editText = view.findViewById(R.id.birthday);
        editText.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            datePickerDialog.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
            datePickerDialog.getButton(-1).setTextColor(getResources().getColor(R.color.teal_200));
        });

        button.setOnClickListener(view13 -> saveProfile());
        return view;
    }

    private void updateDate() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void saveProfile() {
        String g1 = acrbMale.getText().toString();
        String g2 = acrbFemale.getText().toString();
        String birthday = editText.getText().toString();

        if (acrbMale.isChecked()) {
            if (editText.getText().toString().isEmpty()) {
                args.putString("gender", g1);
                args.putString("birthday", "08/07/1990");
            } else {
                args.putString("gender", g1);
                args.putString("birthday", birthday);
            }
        } else {
            if (editText.getText().toString().isEmpty()) {
                args.putString("gender", g2);
                args.putString("birthday", "08/07/1990");
            } else {
                args.putString("gender", g2);
                args.putString("birthday", birthday);
            }
        }
        Fragment_UserBMI fragment = new Fragment_UserBMI();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment,null).addToBackStack(null).commit();
    }
}
