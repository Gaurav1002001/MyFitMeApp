package com.example.myfitmeapp.UserAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.KnowUser.Know_UserContainer;
import com.example.myfitmeapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_SignUp_Email_Phone extends Fragment {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private EditText emailAddress, editTextMobile;
    ;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private EditText userPassword;
    private TextInputLayout emailTil, pwrdTil, phoneTil;
    private Button button;

    private Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_email, container, false);

        args = new Bundle();
        mAuth = FirebaseAuth.getInstance();

        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        editTextMobile = view.findViewById(R.id.editTextPhoneNumber);
        userPassword = view.findViewById(R.id.editTextPassword);
        emailTil = view.findViewById(R.id.emailTextInputLayout);
        phoneTil = view.findViewById(R.id.phoneTextInputLayout);
        pwrdTil = view.findViewById(R.id.pwrdTextInputLayout);

        emailAddress.addTextChangedListener(textWatcher);
        editTextMobile.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);

        Button signupButton = view.findViewById(R.id.signupButton);
        progressDialog = new ProgressDialog(getActivity());

        view.findViewById(R.id.closeButton).setOnClickListener(v -> requireActivity().onBackPressed());

        RelativeLayout relativeLayout = view.findViewById(R.id.phoneSignUp);
        button = view.findViewById(R.id.buttonUsePhone);
        button.setOnClickListener(v -> {
            if (button.getText().toString().equals("Use Phone")) {
                relativeLayout.setVisibility(View.VISIBLE);
                emailTil.setVisibility(View.GONE);
                button.setText("Use Email");
                pwrdTil.setVisibility(View.GONE);
            } else {
                relativeLayout.setVisibility(View.GONE);
                emailTil.setVisibility(View.VISIBLE);
                button.setText("Use Phone");
                pwrdTil.setVisibility(View.VISIBLE);
            }
        });

        signupButton.setOnClickListener(view1 -> CreateNewAccount());

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
            phoneTil.setError(null);
            pwrdTil.setError(null);
        }
    };

    private boolean validateEmail() {
        String emailInput = emailAddress.getText().toString().trim();

        if (emailInput.isEmpty()) {
            return false;
        } else return Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    private boolean validatePassword() {
        String passwordInput = userPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            return false;
        } else return PASSWORD_PATTERN.matcher(passwordInput).matches();
    }

    private void CreateNewAccount() {
        String email = emailAddress.getText().toString().trim();
        String phone = editTextMobile.getText().toString();
        String password = userPassword.getText().toString();
        String buttonText = button.getText().toString();

        if (buttonText.equals("Use Phone")) {
            if (!validateEmail()) {
                emailTil.setError("Invalid email format");
            }
            if (!validatePassword()) {
                pwrdTil.setError("Password must be between 5 and 60 characters");
            }
            if (!email.isEmpty() && !password.isEmpty() && validateEmail() && validatePassword()) {
                progressDialog.setTitle("Creating new Account");
                progressDialog.setMessage("Please wait, while we are creating a new account for you...");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity(), task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getActivity(), Know_UserContainer.class));
                                requireActivity().finish();
                            } else {
                                Toast.makeText(requireActivity(), "Error " + Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        });
            }
        } else {
            if (phone.length() != 10) {
                phoneTil.setError("Please enter a correct phone number");
            } else {
                FirebaseDatabase.getInstance().getReference().child("Phone").child(phone)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(requireActivity(), "Account already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    Fragment_Verify_Phone fragmentVerifyPhone = new Fragment_Verify_Phone();
                                    args.putString("mobile",phone);
                                    fragmentVerifyPhone.setArguments(args);
                                    requireFragmentManager().beginTransaction()
                                            .replace(R.id.mainPage_Container, fragmentVerifyPhone,null).addToBackStack(null).commit();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }
    }
}
