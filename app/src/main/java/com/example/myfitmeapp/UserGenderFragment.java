package com.example.myfitmeapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserGenderFragment extends Fragment {

    private MyRadioButton acrbFemale;
    private MyRadioButton acrbMale;
    private Button button;
    DatePickerDialog datePickerDialog;
    DocumentReference documentReference;
    private EditText editText;
    FirebaseFirestore fStore;
    final Calendar myCalendar = Calendar.getInstance();
    private StateProgressBar stateProgressBar;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_gender, container, false);

        acrbMale = view.findViewById(R.id.acrb_male);
        acrbFemale = view.findViewById(R.id.acrb_female);
        button = view.findViewById(R.id.btnNext);
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(this.userID);
        stateProgressBar = requireActivity().findViewById(R.id.your_state_progress_bar);

        ImageButton imageButton = requireActivity().findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch (i) {
                case R.id.acrb_female:
                case R.id.acrb_male:
                    button.setEnabled(true);
                    return;
                default:
                    return;
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
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void saveProfile() {

        String g1 = acrbMale.getText().toString();
        String g2 = acrbFemale.getText().toString();
        String birthday = editText.getText().toString();

        documentReference = fStore.collection("users").document(userID);
        if (acrbMale.isChecked()) {
            documentReference.update("gender", g1);
        } else {
            documentReference.update("gender", g2);
        }
        if (editText.getText().toString().isEmpty()) {
            documentReference.update("birthday", "08/07/1990");
        } else {
            documentReference.update("birthday", birthday);
        }
        getFragmentManager().beginTransaction().replace(R.id.container,new User_BMI1(),null).addToBackStack(null).commit();
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
    }
}
