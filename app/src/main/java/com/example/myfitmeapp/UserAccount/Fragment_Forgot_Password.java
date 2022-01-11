package com.example.myfitmeapp.UserAccount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Fragment_Forgot_Password extends Fragment {

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    private EditText userEmail;
    private TextInputLayout emailtil;

    public Fragment_Forgot_Password() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progress_bar);

        ImageButton imageButton = view.findViewById(R.id.backButton);
        imageButton.setOnClickListener(v -> requireActivity().onBackPressed());

        userEmail = view.findViewById(R.id.editTextEmailAddress);
        userEmail.addTextChangedListener(textWatcher);
        emailtil = view.findViewById(R.id.emailTil);

        Button confirm = view.findViewById(R.id.signinButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail()) {
                    emailtil.setError("Invalid email id");
                }
                if (!userEmail.getText().toString().isEmpty() && validateEmail()) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Password send to your mail", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            emailtil.setError(null);
        }
    };

    private boolean validateEmail() {
        String emailInput = userEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            return false;
        } else return Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }
}