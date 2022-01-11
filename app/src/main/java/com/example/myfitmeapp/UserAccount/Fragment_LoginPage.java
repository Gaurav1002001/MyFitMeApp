package com.example.myfitmeapp.UserAccount;

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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myfitmeapp.Adapter.ViewPagerAdapter;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.Sign_InUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_LoginPage extends Fragment {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "FACEBOOK";
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

    public Fragment_LoginPage() {
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
        adapter = new ViewPagerAdapter(requireActivity(),images);
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
        loginButton.setReadPermissions(Arrays.asList(
                "email", "public_profile"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Sign_InUtils.handleFacebookAccessToken(loginResult.getAccessToken(),mAuth,getActivity());
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

        view.findViewById(R.id.emailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out,R.anim.slide_in,R.anim.slide_in_down);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.mainPage_Container, new Fragment_SignUp_Email_Phone());
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
            fragmentTransaction.replace(R.id.mainPage_Container, new Fragment_SignIn_Email_Phone());
            fragmentTransaction.commit();
        });

        return view;
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