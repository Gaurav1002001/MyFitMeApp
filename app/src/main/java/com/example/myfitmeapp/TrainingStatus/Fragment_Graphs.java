package com.example.myfitmeapp.TrainingStatus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.myfitmeapp.R;

public class Fragment_Graphs extends Fragment {

    private Spinner spinner1;
    private Spinner spinner2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphs, container, false);

        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);

        String[] arraySpinner = new String[] {"1", "2", "3", "4", "5", "6", "7"};

        return view;
    }
}