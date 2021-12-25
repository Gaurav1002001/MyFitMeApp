package com.example.myfitmeapp;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;

import java.util.regex.Pattern;

public class EmailFragment2 extends Fragment {

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
    private EditText userPassword;
    private TextInputLayout emailTil,pwrdTil;
    private ProgressDialog mProgress;
    String email;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email2, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(requireActivity());
        mProgress = new ProgressDialog(getActivity());

        fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        userPassword = view.findViewById(R.id.editTextPassword);
        Button signinButton = view.findViewById(R.id.signinButton);

        view.findViewById(R.id.closeButton).setOnClickListener(v ->
                requireActivity().onBackPressed());

        view.findViewById(R.id.buttonUsePhone).setOnClickListener(v -> {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new Phone_AuthFragment());
            fragmentTransaction.commit();
        });

        emailAddress.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);

        emailTil = view.findViewById(R.id.emailTextInputLayout);
        pwrdTil = view.findViewById(R.id.pwrdTextInputLayout);

        signinButton.setOnClickListener(v -> {
            signInAccount();
        });

        TextView textForgot = view.findViewById(R.id.textForgot);
        textForgot.setOnClickListener(v -> {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new Forgot_PasswordFragment());
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
                handleFacebookAccessToken(loginResult.getAccessToken());
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
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out,R.anim.slide_in,R.anim.slide_in_down);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new Join_NowFragment());
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

        if (!validateEmail()) {
            emailTil.setError("Invalid email format");
        }
        if (password.isEmpty()) {
            pwrdTil.setError("Password must be not be empty");
        }
        if (!email.isEmpty() && !password.isEmpty() && validateEmail()){
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
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                }
            });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            /*if (((AdditionalUserInfo) Objects.requireNonNull(((AuthResult) task.getResult()).getAdditionalUserInfo())).isNewUser()) {
                                startActivity(new Intent(getActivity(), KnowUserActivity_B.class));
                                requireActivity().finish();
                            } else {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                requireActivity().finish();
                            }*/
                            GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    email = object.getString("email").trim();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString(GraphRequest.FIELDS_PARAM, "name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                            updateUI();
                            Toast.makeText(getActivity(), "Signed In Successfully", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        requireActivity().finish();
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
                firebaseAuthWithGoogle(result.getSignInAccount());
                return;
            }
            mProgress.dismiss();
            Log.e(TAG, "Google Sign In failed.");
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                mAuth.getCurrentUser();
                boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                //Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                if (isNew) {
                    startActivity(new Intent(getActivity(), KnowUserActivity_B.class));
                    requireActivity().finish();
                    return;
                }
                startActivity(new Intent(getActivity(), MainActivity.class));
                requireActivity().finish();
                return;
            }
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            mProgress.dismiss();
            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
        });
    }
}
