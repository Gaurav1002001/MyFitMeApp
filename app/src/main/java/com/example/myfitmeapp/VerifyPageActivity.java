package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPageActivity extends AppCompatActivity {

    DocumentReference documentReference;
    private EditText editTextCode;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    String mobile;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_page);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        editTextCode = findViewById(R.id.codeNumber);
        progressBar = findViewById(R.id.progressBar);

        mobile = getIntent().getStringExtra("mobile");
        sendVerificationCode(mobile);

        findViewById(R.id.backButton1).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(VerifyPageActivity.this,Main_PageActivity.class));
                finish();
            }
        });

        findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {

                String code = editTextCode.getText().toString().trim();

                if (code.length() != 6) {

                    editTextCode.setError("Enter code...");
                    editTextCode.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });

        findViewById(R.id.resendButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                resendVerificationCode(mobile);
            }
        });
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
            Toast.makeText(VerifyPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
            Toast.makeText(VerifyPageActivity.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
        }
    };

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void resendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                mResendToken);
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        this.mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /*if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                        //VerifyPageActivity.this.startActivity(new Intent(VerifyPageActivity.this, KnowUserActivity_B.class));
                        VerifyPageActivity.this.finish();
                    } else {
                        VerifyPageActivity.this.startActivity(new Intent(VerifyPageActivity.this, MainActivity.class));
                        VerifyPageActivity.this.finish();
                    }*/

                    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    documentReference = fStore.collection("users").document(userID);
                    documentReference.update("phone", "+91-" + mobile);

                    Intent intent = new Intent(VerifyPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(VerifyPageActivity.this, "Signed in Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyPageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}
