package com.example.myfitmeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONException;

public class Phone_AuthFragment extends Fragment {

    DotsIndicator dotsIndicator;
    private EditText editTextMobile;
    private Spinner spinner;
    ViewPager viewPager;
    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    String email;
    TextInputLayout phoneTil;

    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());

        editTextMobile = view.findViewById(R.id.editTextPhoneNumber);
        editTextMobile.addTextChangedListener(textWatcher);
        phoneTil = view.findViewById(R.id.phoneTextInputLayout);

        ImageButton imageButton = view.findViewById(R.id.closeButton);
        imageButton.setOnClickListener(v -> requireActivity().onBackPressed());

        Button btnEmail = view.findViewById(R.id.buttonUseEmail);
        btnEmail.setOnClickListener(v -> requireActivity().onBackPressed());

        Button btnSignIn = view.findViewById(R.id.signinButton);
        btnSignIn.setOnClickListener(v -> {
            String mobile = editTextMobile.getText().toString().trim();

            if (mobile.length() != 10) {
                phoneTil.setError("Please enter a correct phone number");
                editTextMobile.requestFocus();
                return;
            }

            Intent intent = new Intent(requireActivity(), VerifyPageActivity.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);

        });

        TextView textForgot = view.findViewById(R.id.textForgot);
        textForgot.setOnClickListener(v -> {
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
        }

        public void afterTextChanged(Editable editable) {
            phoneTil.setError(null);
        }
    };

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

    /*public class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }

        @Override
        public void run() {
            if (viewPager.getCurrentItem() == 0) {
                viewPager.setCurrentItem(1);
            } else if (viewPager.getCurrentItem() == 1) {
                viewPager.setCurrentItem(2);
            } else if (viewPager.getCurrentItem() == 2) {
                viewPager.setCurrentItem(3);
            } else if (viewPager.getCurrentItem() == 3) {
                viewPager.setCurrentItem(4);
            } else {
                viewPager.setCurrentItem(0);
            }
        }
    }*/
}
