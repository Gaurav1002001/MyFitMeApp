package com.example.myfitmeapp.UserAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfitmeapp.BottomNavFragments.MainActivity;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.Sign_InUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_SignIn_Email_Phone extends Fragment {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("" +
            "^(?=" +
            ".*[a-zA-Z])(?=" +
            ".*[@#$%^&+=])(?=\\S+$)." +
            "{4,}$");

    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText emailAddress;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private EditText userPassword, editTextMobile;
    private TextInputLayout emailTil, pwrdTil, phoneTil;
    private ProgressDialog mProgress;
    private Button button;

    private FragmentTransaction fragmentTransaction;
    private Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_email, container, false);

        args = new Bundle();

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(requireActivity());
        mProgress = new ProgressDialog(getActivity());

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        editTextMobile = view.findViewById(R.id.editTextPhoneNumber);
        userPassword = view.findViewById(R.id.editTextPassword);
        emailTil = view.findViewById(R.id.emailTextInputLayout);
        phoneTil = view.findViewById(R.id.phoneTextInputLayout);
        pwrdTil = view.findViewById(R.id.pwrdTextInputLayout);

        emailAddress.addTextChangedListener(textWatcher);
        editTextMobile.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);

        Button signinButton = view.findViewById(R.id.signinButton);

        view.findViewById(R.id.closeButton).setOnClickListener(v ->
                requireActivity().onBackPressed());

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


        signinButton.setOnClickListener(v -> {
            signInAccount();
        });

        TextView textForgot = view.findViewById(R.id.textForgot);
        textForgot.setOnClickListener(v -> {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new Fragment_Forgot_Password());
            fragmentTransaction.commit();
        });

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = view.findViewById(R.id.facebookButton);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Sign_InUtils.handleFacebookAccessToken(loginResult.getAccessToken(), mAuth, getActivity());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        view.findViewById(R.id.googleSignInButton).setOnClickListener(view1 -> signIn());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        TextView textJoin = view.findViewById(R.id.textJoin);
        String mystring = "Join Now";
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 8);
        textJoin.setText(content);

        textJoin.setOnClickListener(v -> {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out, R.anim.slide_in, R.anim.slide_in_down);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new Fragment_Join_Now());
            fragmentTransaction.commit();
        });

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
            phoneTil.setError(null);
        }
    };

    private boolean validateEmail() {
        String emailInput = emailAddress.getText().toString().trim();

        if (emailInput.isEmpty()) {
            return false;
        } else return Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    private void signInAccount() {
        String email = emailAddress.getText().toString();
        String password = userPassword.getText().toString();
        String buttonText = button.getText().toString();
        String phone = editTextMobile.getText().toString();

        if (buttonText.equals("Use Phone")) {
            if (!validateEmail()) {
                emailTil.setError("Invalid email id");
            }
            if (password.isEmpty()) {
                pwrdTil.setError("Password must be not be empty");
            }
            if (!email.isEmpty() && !password.isEmpty() && validateEmail()) {
                progressDialog.setMessage("Please wait, verifying account");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(Fragment_SignIn_Email_Phone.TAG, "signInWithEmail:success");
                            progressDialog.dismiss();
                            updateUI();
                            Toast.makeText(getActivity(), "Signed In", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.w(Fragment_SignIn_Email_Phone.TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed.Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    private void updateUI() {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        requireActivity().finish();
                    }
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
                                    Fragment_Verify_Phone fragmentVerifyPhone = new Fragment_Verify_Phone();
                                    args.putString("mobile",phone);
                                    fragmentVerifyPhone.setArguments(args);
                                    requireFragmentManager().beginTransaction()
                                            .replace(R.id.mainPage_Container, fragmentVerifyPhone,null).addToBackStack(null).commit();
                                } else {
                                    Toast.makeText(requireActivity(), "Account does not exist with this phone number!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }
        }
    }

    private void signIn() {
        mGoogleSignInClient.signOut();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                mProgress.dismiss();
                Sign_InUtils.firebaseAuthWithGoogle(Objects.requireNonNull(result.getSignInAccount()), mAuth, getActivity());
                return;
            }
            mProgress.dismiss();
            Log.e(TAG, "Google Sign In failed.");
        }
    }
}
