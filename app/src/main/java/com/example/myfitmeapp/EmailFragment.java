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
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    //".{4,}" +               //at least 4 characters
                    "$");

    private EditText emailAddress;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private EditText userPassword;
    private TextInputLayout emailTil,pwrdTil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);

        mAuth = FirebaseAuth.getInstance();
        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        userPassword = view.findViewById(R.id.editTextPassword);

        Button signupButton = view.findViewById(R.id.signupButton);
        progressDialog = new ProgressDialog(getActivity());

        view.findViewById(R.id.closeButton).setOnClickListener(v -> requireActivity().onBackPressed());

        emailAddress.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);

        emailTil = view.findViewById(R.id.emailTextInputLayout);
        pwrdTil = view.findViewById(R.id.pwrdTextInputLayout);

        signupButton.setOnClickListener(view1 ->
                CreateNewAccount());

        return view;
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String passwordInput = userPassword.getText().toString();
            pwrdTil.setPasswordVisibilityToggleEnabled(!passwordInput.isEmpty());
        }

        public void afterTextChanged(Editable editable) {
            emailTil.setError(null);
            pwrdTil.setError(null);
        }
    };

    private boolean validateEmail() {
        String emailInput = emailAddress.getText().toString().trim();

        if (emailInput.isEmpty()) {
            return false;
        } else return Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    /*private boolean validatePassword() {
        String passwordInput = userPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            return false;
        } //else return PASSWORD_PATTERN.matcher(passwordInput).matches();
    }*/

    private void CreateNewAccount() {
        String email = emailAddress.getText().toString().trim();
        String password = userPassword.getText().toString();

        if (!validateEmail()) {
            emailTil.setError("Invalid email format");
        }
        if (!password.isEmpty()) {
            pwrdTil.setError("Password must be between 5 and 60 characters");
        }
        if (!email.isEmpty() && !password.isEmpty() && validateEmail()) {
            progressDialog.setTitle("Creating new Account");
            progressDialog.setMessage("Please wait, while we are creating a new account for you...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if(task.isSuccessful()){
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            //Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                            if (isNew) {
                                startActivity(new Intent(getActivity(), KnowUserActivity_B.class));
                                requireActivity().finish();
                                return;
                            }
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            requireActivity().finish();
                        }else{
                            //display some message here
                            Toast.makeText(requireActivity(), "Error " + ((Exception) Objects.requireNonNull(task.getException())).toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    });
        }
    }
}
