package com.example.myfitmeapp.TrainingStatus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myfitmeapp.MyRadioButton;
import com.example.myfitmeapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fragment_Workout extends Fragment {

    private MyRadioButton strength;
    private MyRadioButton cardio;
    private MyRadioButton isometric;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        EditText currentDate = view.findViewById(R.id.currentDate);
        EditText currentTime = view.findViewById(R.id.currentTime);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(calendar.getTime());
        currentDate.setText(date);

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
        String time = sdf1.format(calendar.getTime());
        currentTime.setText(time);

        return view;
    }
}