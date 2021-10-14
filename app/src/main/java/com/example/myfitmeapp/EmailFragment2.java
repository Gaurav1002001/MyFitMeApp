package com.example.myfitmeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class EmailFragment2 extends Fragment {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("" +
            "^(?=" +
            ".*[a-zA-Z])(?=" +
            ".*[@#$%^&+=])(?=\\S+$)." +
            "{4,}$");

    private static final String TAG = "EmailPassword";
    private EditText emailAddress;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button signinButton;
    private TextView textView;
    private EditText userPassword;
    private TextInputLayout textInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email2, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(requireActivity());

        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        userPassword = view.findViewById(R.id.editTextPassword);
        signinButton = view.findViewById(R.id.signinButton);
        textView = view.findViewById(R.id.text_link);
        textInputLayout = view.findViewById(R.id.textInputLayout);

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        emailAddress.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);

        signinButton.setOnClickListener(view1 -> signInAccount());

        view.findViewById(R.id.text_link).setOnClickListener(v -> {
              requireActivity().onBackPressed();
        });

        return view;
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String emailInput = emailAddress.getText().toString();
            String passwordInput = userPassword.getText().toString();
            signinButton.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
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

    private void signInAccount() {
        String email = emailAddress.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Verifying Account");
            progressDialog.setMessage("Please wait, verfying account");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(EmailFragment2.TAG, "signInWithEmail:success");
                        progressDialog.dismiss();
                        updateUI();
                        Toast.makeText(getActivity(), "Signed In", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.w(EmailFragment2.TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                private void updateUI() {
                    startActivity(new Intent(EmailFragment2.this.getActivity(), MainActivity.class));
                    requireActivity().finish();
                }
            });
        }
    }
}
