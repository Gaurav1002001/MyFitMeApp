package com.example.myfitmeapp.UserAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.BottomNavFragments.MainActivity;
import com.example.myfitmeapp.KnowUser.Know_UserContainer;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class Fragment_Verify_Phone extends Fragment {

    private EditText editTextCode;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView resend;

    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    String mobile;
    private ProgressDialog progressDialog;
    private final int attempt = 3;

    private Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verify, container, false);

        args = this.getArguments();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        editTextCode = view.findViewById(R.id.codeNumber);
        progressBar = view.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(getActivity());

        if (args != null) {
            mobile = args.getString("mobile");
            sendVerificationCode(mobile);
        }

        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        TextView textView = view.findViewById(R.id.textView);
        textView.setText("Enter the 6-digit code we sent to +91 " + mobile + ".");

        view.findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                String code = editTextCode.getText().toString().trim();

                if (code.length() != 6) {
                    Toast.makeText(getActivity(), "Wrong Code, please try again!", Toast.LENGTH_SHORT).show();
                    editTextCode.requestFocus();
                } else {
                    if (attempt > 0) {
                        progressBar.setVisibility(View.GONE);
                        verifyVerificationCode(code);
                    } else {
                        Toast.makeText(getActivity(), "Attempt Exceeded , try again", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
            }
        });

        resend = view.findViewById(R.id.resendButton);
        startTimer();
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mobile);
                startTimer();
            }
        });

        return view;
    }

    private void startTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend.setText("" + millisUntilFinished / 1000 + " sec");
                resend.setAllCaps(false);
                resend.setEnabled(false);
            }

            public void onFinish() {
                resend.setText("Resend");
                resend.setAllCaps(true);
                resend.setEnabled(true);
            }
        }.start();
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editTextCode.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
            Toast.makeText(getActivity(), "Code sent successfully", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                mCallbacks);
    }

    private void resendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                mCallbacks,
                mResendToken);
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Phone").child(mobile).setValue(true);
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                    if (isNew) {
                        progressDialog.setTitle("Creating new Account");
                        progressDialog.setMessage("Please wait, while we are creating a new account for you...");
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.show();
                        gotoUserInfo();
                    } else {
                        progressDialog.setMessage("Please wait, verifying account");
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.show();
                        gotoMainActivity();
                    }
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void gotoUserInfo() {
        Intent intent = new Intent(getActivity(), Know_UserContainer.class);
        startActivity(intent);
    }

    private void gotoMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}
