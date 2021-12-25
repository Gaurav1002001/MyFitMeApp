package com.example.myfitmeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;

public class Join_NowFragment extends Fragment {

    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    String email;
    private FragmentTransaction fragmentTransaction;

    public Join_NowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join__now, container, false);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ImageButton imageButton = view.findViewById(R.id.closeButton);
        imageButton.setOnClickListener(v -> requireActivity().onBackPressed());

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

        Button signupButton = view.findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new EmailFragment());
            fragmentTransaction.commit();
        });

        return view;
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