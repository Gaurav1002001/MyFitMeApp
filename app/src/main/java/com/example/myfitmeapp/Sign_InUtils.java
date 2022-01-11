package com.example.myfitmeapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myfitmeapp.BottomNavFragments.MainActivity;
import com.example.myfitmeapp.KnowUser.Fragment_UserNamePhoto;
import com.example.myfitmeapp.KnowUser.Know_UserContainer;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class Sign_InUtils extends Activity{

    public static void handleFacebookAccessToken(AccessToken token, FirebaseAuth mAuth, Context context) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                            gotoUserInfo(context);
                        } else {
                            gotoMainActivity(context);
                        }
                        return;
                    } else
                        Toast.makeText(context, "Authentication failed. "+task.getException(), Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();

                });
    }

    public static void firebaseAuthWithGoogle(GoogleSignInAccount acct, FirebaseAuth mAuth, Context context) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context, task -> {
            if (task.isSuccessful()) {
                Log.d("TAG", "signInWithCredential:success");
                mAuth.getCurrentUser();
                boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                if (isNew) {
                    gotoUserInfo(context);
                    return;
                }
                gotoMainActivity(context);
                return;
            } else
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();

        });
    }

    private static void gotoUserInfo(Context context) {
        Intent intent = new Intent(context, Know_UserContainer.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private static void gotoMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
        ((Activity) context).finish();
    }
}
