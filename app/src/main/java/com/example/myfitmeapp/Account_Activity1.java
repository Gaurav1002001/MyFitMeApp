package com.example.myfitmeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.internal.view.SupportMenu;

import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Account_Activity1 extends AppCompatActivity {

    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    FirebaseFirestore fStore;
    SwitchCompat facebookSwitch;
    SwitchCompat googleSwitch;
    private FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    private FirebaseUser mUser;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account1);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Toolbar mToolbar = findViewById(R.id.toolbar5);
        mToolbar.inflateMenu(R.menu.menu_horizontal);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.moreVertical) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deleteUser();
                        deleteProfileImage();
                    }
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
                pbutton.setTextColor(SupportMenu.CATEGORY_MASK);
                pbutton.setAllCaps(false);
            }
            return super.onOptionsItemSelected(item);
        });
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
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
        startActivity(new Intent(Account_Activity1.this, Main_PageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    public void deleteUser() {
        DocumentReference docRef = fStore.collection("users").document(userID);
        docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Account_Activity1.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                updateUI();
                            } else {
                                Toast.makeText(Account_Activity1.this, "Error Deleting Account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Account_Activity1.this, "Error Deleting Account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteProfileImage() {
        DocumentReference docRef = fStore.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.get("imageUrl") != null) {
                        String imageUrl = document.getString("imageUrl");
                        StorageReference strRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        strRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    deleteUser();
                                } else {
                                    Toast.makeText(Account_Activity1.this, "Error Deleting Account", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        deleteUser();
                    }
                } else {
                    Toast.makeText(Account_Activity1.this, "Error Deleting Account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
