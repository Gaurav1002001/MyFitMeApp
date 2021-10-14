package com.example.myfitmeapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.internal.view.SupportMenu;

import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Account_Activity1 extends AppCompatActivity {

    private static final String TAG = "REAUTHENTICATE";
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    DocumentReference documentReference;
    FirebaseFirestore fStore;
    SwitchCompat facebookSwitch;
    SwitchCompat googleSwitch;
    private FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    private FirebaseUser mUser;
    String previousID;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account1);

        Toolbar mToolbar = findViewById(R.id.toolbar5);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setTitle("Account");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.inflateMenu(R.menu.menu_horizontal);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.moreVertical) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");
                builder.setPositiveButton("Delete", (dialogInterface, i) ->
                        mUser.delete().addOnCompleteListener((OnCompleteListener) task -> deleteProfile()));
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                Button nbutton = dialog.getButton(-2);
                nbutton.setBackgroundColor(-1);
                nbutton.setTextColor(-7829368);
                nbutton.setAllCaps(false);
                Button pbutton = dialog.getButton(-1);
                pbutton.setBackgroundColor(-1);
                pbutton.setTextColor(SupportMenu.CATEGORY_MASK);
                pbutton.setAllCaps(false);
            }
            return super.onOptionsItemSelected(item);
        });
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        previousID = userID;
        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("users").document(userID);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        facebookSwitch = findViewById(R.id.switch1);
        googleSwitch = findViewById(R.id.switch2);

        if (AccessToken.getCurrentAccessToken() != null) {
            facebookSwitch.setChecked(true);
        }
        GoogleApiClient googleApiClient = mGoogleApiClient;
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleSwitch.setChecked(true);
        }

        googleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                mAuth.signOut();
            }
        });

        findViewById(R.id.connectButton).setOnClickListener(v -> showAlertDialog(R.layout.activity_custom_dialog));

        findViewById(R.id.logoutButton).setOnClickListener(v -> signOutDialog());
    }

    private void showAlertDialog(int layout) {
        dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        dialogBuilder.setView(layoutView);
        AlertDialog create = dialogBuilder.create();
        alertDialog = create;
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
        layoutView.findViewById(R.id.cancel).setOnClickListener(view -> alertDialog.dismiss());
    }

    public void signOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Sign out?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            mAuth.signOut();
            updateUI();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        Button nbutton = dialog.getButton(-2);
        nbutton.setBackgroundColor(-1);
        nbutton.setTextColor(-7829368);
        nbutton.setAllCaps(false);
        Button pbutton = dialog.getButton(-1);
        pbutton.setBackgroundColor(-1);
        pbutton.setTextColor(-7829368);
        pbutton.setAllCaps(false);
    }

    private void updateUI() {
        Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Account_Activity1.this, Main_PageActivity.class));
        finish();
    }

    public void deleteProfile() {
        fStore.collection("users").document(previousID).delete()
                .addOnSuccessListener(aVoid -> Log.d(Account_Activity1.TAG, "DocumentSnapshot successfully deleted!")).addOnFailureListener(e -> Log.w(Account_Activity1.TAG, "Error deleting document", e));
            Log.d(TAG, "User account deleted.");
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show();
            updateUI();
    }
}
