package com.example.myfitmeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONException;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Login_PageFragment extends Fragment {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "FACEBOOK";
    String email;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog mProgress;
    private FragmentTransaction fragmentTransaction;

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    int currentPage = 0;
    DotsIndicator dotsIndicator;

    private final Integer[] images = {R.drawable.pass, R.drawable.bodybuilder, R.drawable.img_register_guide_video, R.drawable.img_register_guide_workouts, R.drawable.preview3};

    public Login_PageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login_page, container, false);

        FacebookSdk.sdkInitialize(requireActivity());
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        viewPager = view.findViewById(R.id.viewPager1);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
        adapter = new ViewPagerAdapter(getActivity(),images);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == images.length) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 4000);

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
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        view.findViewById(R.id.emailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out,R.anim.slide_in,R.anim.slide_in_down);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.mainPage_Container, new EmailFragment());
                fragmentTransaction.commit();
            }
        });

        TextView textLogin = view.findViewById(R.id.textLogin);
        String mystring = "Log In";
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 5);
        textLogin.setText(content);

        textLogin.setOnClickListener(v -> {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out,R.anim.slide_in,R.anim.slide_in_down);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainPage_Container, new EmailFragment2());
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
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        if (((AdditionalUserInfo) Objects.requireNonNull(((AuthResult) task.getResult()).getAdditionalUserInfo())).isNewUser()) {
                            startActivity(new Intent(getActivity(), KnowUserActivity_B.class));
                            requireActivity().finish();
                        } else {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            requireActivity().finish();
                        }
                        GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
                            Log.v("LoginActivity", response.toString());
                                    if (object != null) {
                                        try {
                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            String fbUserID = object.getString("id");

                                            disconnectFromFacebook();

                                            // do action after Facebook login success
                                            // or call your API
                                        } catch (JSONException | NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString(
                                "fields",
                                "id, name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                        //updateUI();
                        return;
                    }
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    mProgress.dismiss();
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                });
    }

    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }


    private void updateUI() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("email", this.email);
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

    public void onBackPressed() {
        //handle back press event
    }
}