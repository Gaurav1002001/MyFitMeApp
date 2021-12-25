package com.example.myfitmeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_PasswordFragment extends Fragment {

    FirebaseAuth mAuth;
    ProgressBar progressBar;

    public Forgot_PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot__password, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progress_bar);

        ImageButton imageButton = view.findViewById(R.id.backButton);
        imageButton.setOnClickListener(v -> requireActivity().onBackPressed());

        EditText userEmail = view.findViewById(R.id.editTextEmailAddress);
        Button confirm = view.findViewById(R.id.signinButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                         if (task.isSuccessful()) {
                             Toast.makeText(getActivity(),"Password send to your mail",Toast.LENGTH_SHORT).show();
                         } else {
                             Toast.makeText(getActivity(),task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                         }
                    }
                });
            }
        });

        return view;
    }
}