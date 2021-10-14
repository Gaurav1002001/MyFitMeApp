package com.example.myfitmeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

public class EmailFragment extends Fragment {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("" +
            "^(?=" +
            ".*[a-zA-Z])(?=" +
            ".*[@#$%^&+=])(?=\\S+$)." +
            "{4,}$");
    private String email;
    private EditText emailAddress;
    private FirebaseAuth mAuth;
    private String password;
    private ProgressDialog progressDialog;
    private Button signupButton;
    private TextInputLayout textInputLayout;
    private EditText userPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);

        mAuth = FirebaseAuth.getInstance();
        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        userPassword = view.findViewById(R.id.editTextPassword);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        signupButton = view.findViewById(R.id.signupButton);
        progressDialog = new ProgressDialog(getActivity());

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        emailAddress.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);

        signupButton.setOnClickListener(view1 -> CreateNewAccount());

        view.findViewById(R.id.text_link).setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, new EmailFragment2());
            fragmentTransaction.commit();
        });
        return view;
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String emailInput = emailAddress.getText().toString();
            String passwordInput = userPassword.getText().toString();
            signupButton.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
            textInputLayout.setPasswordVisibilityToggleEnabled(!passwordInput.isEmpty());
        }

        public void afterTextChanged(Editable editable) {
            validateEmail();
        }
    };

    private boolean validateEmail() {
        String emailInput = emailAddress.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailAddress.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailAddress.setError("Please enter a valid email address");
            return false;
        } else {
            emailAddress.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = userPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            userPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            userPassword.setError("Password too weak");
            return false;
        } else {
            userPassword.setError(null);
            return true;
        }
    }

    private void CreateNewAccount() {
        email = emailAddress.getText().toString().trim();
        password = userPassword.getText().toString();

        if (validateEmail() && validatePassword()) {
            progressDialog.setTitle("Creating new Account");
            progressDialog.setMessage("Please wait, while we are creating a new account for you...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                requireActivity().finish();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }else{
                                //display some message here
                                Toast.makeText(requireActivity(), "Error " + ((Exception) Objects.requireNonNull(task.getException())).toString(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}
