package com.example.myfitmeapp.KnowUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitmeapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class Fragment_KUA_c extends Fragment {

    String fullname,imageUri,gender,birthday,height,weight,bmi;
    Bundle args;

    public Fragment_KUA_c() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_know_user_c, container, false);

        args = this.getArguments();
        if(args != null) {
            fullname = args.getString("fullName");
            imageUri = args.getString("imageUri");
            gender = args.getString("gender");
            birthday = args.getString("birthday");
            height = args.getString("height");
            weight = args.getString("weight");
            bmi = args.getString("bmi");
        }

        final TextInputLayout textInputLayout = view.findViewById(R.id.editLoginWrapper);
        final TextView counter = view.findViewById(R.id.counter);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        view.findViewById(R.id.buttonContinue).setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ThankYou.class);
            Bundle bundle = new Bundle();
            if (imageUri != null) {
                bundle.putString("imageUri",imageUri);
            }
            bundle.putString("fullname",fullname);
            bundle.putString("gender",gender);
            bundle.putString("birthday",birthday);
            bundle.putString("height",height);
            bundle.putString("weight",weight);
            bundle.putString("bmi",bmi);
            intent.putExtras(bundle);
            startActivity(intent);
            requireActivity().finish();
        });

        CheckBox checkBox = view.findViewById(R.id.button_other);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textInputLayout.setVisibility(View.VISIBLE);
                textInputLayout.requestFocus();
                return;
            }
            textInputLayout.setVisibility(View.GONE);
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