package com.example.myfitmeapp.KnowUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.R;

public class Fragment_KUA_a extends Fragment {

    Bundle args;

    public Fragment_KUA_a() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_know_user_a, container, false);

        args = this.getArguments();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        CheckBox stayHealthy = view.findViewById(R.id.button_stayHealthy);

        Button button = view.findViewById(R.id.buttonContinue);
        button.setOnClickListener(v -> {
            Fragment_KUA_b fragment_b = new Fragment_KUA_b();
            fragment_b.setArguments(args);
            requireFragmentManager().beginTransaction().replace(R.id.container, fragment_b, null).addToBackStack(null).commit();
        });
        return view;
    }
}