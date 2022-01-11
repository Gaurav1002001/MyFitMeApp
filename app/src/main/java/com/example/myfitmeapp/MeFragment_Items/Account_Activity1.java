package com.example.myfitmeapp.MeFragment_Items;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.myfitmeapp.R;
import com.example.myfitmeapp.UserAccount.UserAccount_Container;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Account_Activity1 extends AppCompatActivity {

    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    FirebaseFirestore fStore;
    SwitchCompat facebookSwitch;
    SwitchCompat googleSwitch;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String userID;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account1);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mProgress = new ProgressDialog(this);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        facebookSwitch = findViewById(R.id.facebookSwitch);
        googleSwitch = findViewById(R.id.googleSwitch);
        googleSwitch.setChecked(true);

        Toolbar mToolbar = findViewById(R.id.toolbar5);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        ImageView imageView = findViewById(R.id.deleteAccount);
        imageView.setOnClickListener(v -> {
            CharSequence[] items = {"Delete Account"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Account_Activity1.this);
            builder.setItems(items, (dialog, which) -> {
                if (which == 0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Account_Activity1.this);
                    builder1.setTitle("Are you sure?");
                    builder1.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");
                    builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    });
                    builder1.setNegativeButton("Cancel", (dialog1, which1) -> dialog1.dismiss());
                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                    Button nbutton = dialog1.getButton(-2);
                    nbutton.setBackgroundColor(-1);
                    nbutton.setTextColor(getResources().getColor(R.color.yoga_green));
                    nbutton.setAllCaps(false);
                    Button pbutton = dialog1.getButton(-1);
                    pbutton.setBackgroundColor(-1);
                    pbutton.setTextColor(getResources().getColor(R.color.yoga_green));
                    pbutton.setAllCaps(false);
                } else {
                    dialog.dismiss();
                }
            });
            builder.show();
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            facebookSwitch.setChecked(true);
            googleSwitch.setChecked(false);
        }

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
        builder.setMessage("Log out?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            mAuth.signOut();
            updateUI();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        Button nbutton = dialog.getButton(-2);
        nbutton.setBackgroundColor(-1);
        nbutton.setTextColor(getResources().getColor(R.color.yoga_green));
        nbutton.setAllCaps(false);
        Button pbutton = dialog.getButton(-1);
        pbutton.setBackgroundColor(-1);
        pbutton.setTextColor(getResources().getColor(R.color.yoga_green));
        pbutton.setAllCaps(false);
    }

    public void deleteUser() {
        mProgress.setMessage("Just a moment");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        FirebaseDatabase.getInstance().getReference().child("Follow").child(mUser.getUid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        mUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mProgress.dismiss();
                                            updateUI();
                                        } else {
                                            mProgress.dismiss();
                                            Toast.makeText(Account_Activity1.this,"Error Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

    }

    private void updateUI() {
        Intent i = new Intent(Account_Activity1.this, UserAccount_Container.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
