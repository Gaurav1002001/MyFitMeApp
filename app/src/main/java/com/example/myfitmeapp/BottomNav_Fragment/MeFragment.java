package com.example.myfitmeapp.BottomNav_Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfitmeapp.AboutUs_Activity;
import com.example.myfitmeapp.Account_Activity1;
import com.example.myfitmeapp.BuildConfig;
import com.example.myfitmeapp.FriendsActivity;
import com.example.myfitmeapp.Profile_Activity;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.Reminder_Activity;
import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends Fragment {

    Button bmi;
    String bmi_txt;
    ImageButton btn_decrement;
    ImageButton btn_increment;
    TextView display_bmi;
    TextView display_weight;
    DocumentReference documentReference;
    String email;
    FirebaseFirestore fStore;
    String getHeight_txt;
    String height_txt;
    CircleImageView profileImageView;
    TextView textView;
    Button update_btn;
    FirebaseUser user;
    String userID;
    View view1;
    View view2;
    View view3;
    View view4;
    Button weight;
    String weight_txt;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), R.style.CustomTheme);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_me, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        profileImageView = view.findViewById(R.id.profileImageView);
        textView = view.findViewById(R.id.email);
        display_weight = view.findViewById(R.id.display_weight);
        display_bmi = view.findViewById(R.id.display_bmi);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        view4 = view.findViewById(R.id.view4);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("users").document(userID);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (!(user == null || user.getPhotoUrl() == null)) {
            Glide.with(this).load(user.getPhotoUrl()).into(profileImageView);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }
                    if (value != null && value.exists()) {
                        textView.setText(value.getString("fullName"));
                        display_weight.setText(value.getString("weight"));
                        display_bmi.setText(value.getString("bmi"));
                        getHeight_txt = value.getString(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY);
                    }
                }
            });
            textView.setText(email);
        }
        if (display_weight.getText() != null) {
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
        } else {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        }
        if (display_bmi.getText() != null) {
            view3.setVisibility(View.INVISIBLE);
            view4.setVisibility(View.INVISIBLE);
        } else {
            view3.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
        }

        weight = view.findViewById(R.id.button_weight);
        weight.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                showAlertDialog(R.layout.dialog_weight);
            }
        });

        bmi = view.findViewById(R.id.button_bmi);
        bmi.setOnClickListener(view52 ->
                showAlertDialog(R.layout.dialog_bmi));

        view.findViewById(R.id.buttonProfile).setOnClickListener(view5 ->
                startActivity(new Intent(getActivity(), Profile_Activity.class)));

        view.findViewById(R.id.buttonFriends).setOnClickListener(view53 ->
                startActivity(new Intent(getActivity(), FriendsActivity.class)));

        view.findViewById(R.id.buttonAccount).setOnClickListener(view54 ->
                startActivity(new Intent(getActivity(), Account_Activity1.class)));

        view.findViewById(R.id.buttonAbout_Us).setOnClickListener(view55 ->
                startActivity(new Intent(getActivity(), AboutUs_Activity.class)));

        view.findViewById(R.id.buttonRecommend).setOnClickListener(view56 -> {
            requireActivity().getSupportFragmentManager();
            try {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("text/plain");
                shareIntent.putExtra("android.intent.extra.SUBJECT", "FitMe");
                shareIntent.putExtra("android.intent.extra.TEXT", "\nJoin me in FitMe, a complete Fitness App\n\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n");
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
            }
        });

        view.findViewById(R.id.buttonReminder).setOnClickListener(view57 ->
                startActivity(new Intent(getActivity(), Reminder_Activity.class)));
        return view;
    }

    private void showAlertDialog(int layout) {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(requireActivity());
        View layoutView = getLayoutInflater().inflate(layout, null);
        layoutView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        alertdialogBuilder.setView(layoutView);
        if (layout == R.layout.dialog_weight) {
            final EditText dialog_txt = layoutView.findViewById(R.id.dialog_txt);
            dialog_txt.requestFocus();
            dialog_txt.setText(display_weight.getText().toString());
            weight_txt = display_weight.getText().toString();
            ImageButton btn_increment = layoutView.findViewById(R.id.btn_increment);
            btn_increment.setOnClickListener(v -> {
                float t = Float.parseFloat(dialog_txt.getText().toString());
                if (t < 250.0f) {
                    dialog_txt.setText(new DecimalFormat("###.#").format(((double) t) + 0.1d));
                    MeFragment.this.weight_txt = dialog_txt.getText().toString();
                }
            });
            ImageButton btn_decrement = layoutView.findViewById(R.id.btn_decrement);
            btn_decrement.setOnClickListener(v -> {
                float t = Float.parseFloat(dialog_txt.getText().toString());
                if (t > 40.0f) {
                    dialog_txt.setText(new DecimalFormat("###.#").format(((double) t) - 0.1d));
                    MeFragment.this.weight_txt = dialog_txt.getText().toString();
                }
            });
            AlertDialog alertDialog = alertdialogBuilder.create();
            alertDialog.show();
            /*Button button = (Button) layoutView.findViewById(R.id.update_button);
            this.update_btn = button;
            button.setOnClickListener(new View.OnClickListener(dialog_txt, alertDialog) {
                public final EditText f$1;
                public final AlertDialog f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void onClick(View view) {
                    MeFragment.this.lambda$showAlertDialog$8$MeFragment(this.f$1, this.f$2, view);
                }
            });*/
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            //alertDialog.getWindow().setLayout(TypedValues.Custom.TYPE_INT, 1000);
            try {
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            final EditText dialogHeight_txt = layoutView.findViewById(R.id.dialogHeight_txt);
            dialogHeight_txt.requestFocus();
            dialogHeight_txt.setText(this.getHeight_txt);
            this.height_txt = this.getHeight_txt;
            this.weight_txt = this.display_weight.getText().toString();
            ImageButton imageButton3 = layoutView.findViewById(R.id.btn_increment);
            this.btn_increment = imageButton3;
            imageButton3.setOnClickListener(v -> {
                float t = Float.parseFloat(dialogHeight_txt.getText().toString());
                if (t < 250.0f) {
                    dialogHeight_txt.setText(new DecimalFormat("###.#").format(((double) t) + 0.1d));
                    MeFragment.this.height_txt = dialogHeight_txt.getText().toString();
                }
            });
            ImageButton imageButton4 = layoutView.findViewById(R.id.btn_decrement);
            this.btn_decrement = imageButton4;
            imageButton4.setOnClickListener(v -> {
                float t = Float.parseFloat(dialogHeight_txt.getText().toString());
                if (t > 40.0f) {
                    dialogHeight_txt.setText(new DecimalFormat("###.#").format(((double) t) - 0.1d));
                    MeFragment.this.height_txt = dialogHeight_txt.getText().toString();
                }
            });
            AlertDialog alertDialog2 = alertdialogBuilder.create();
            alertDialog2.show();
            Button button2 = (Button) layoutView.findViewById(R.id.update_button);
            this.update_btn = button2;
            /*button2.setOnClickListener(new View.OnClickListener(dialogHeight_txt, alertDialog2) {
                public final EditText f$1;
                public final AlertDialog f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void onClick(View view) {
                    MeFragment.this.lambda$showAlertDialog$9$MeFragment(this.f$1, this.f$2, view);
                }
            });*/
            alertDialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            //alertDialog2.getWindow().setLayout(TypedValues.Custom.TYPE_INT, 1000);
            try {
                alertDialog2.show();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void lambda$showAlertDialog$8$MeFragment(EditText dialog_txt, AlertDialog alertDialog, View v) {
        if (TextUtils.isEmpty(dialog_txt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter Weight", Toast.LENGTH_SHORT).show();
            return;
        }
        this.display_weight.setText(this.weight_txt);
        this.documentReference.update("weight", this.weight_txt, new Object[0]);
        alertDialog.dismiss();
    }

    public void lambda$showAlertDialog$9$MeFragment(EditText dialogHeight_txt, AlertDialog alertDialog, View v) {
        if (TextUtils.isEmpty(dialogHeight_txt.getText().toString())) {
            dialogHeight_txt.setError("Enter Height");
        }
        String str = this.weight_txt;
        if (str != null) {
            String result = String.format("%.2f", Float.valueOf(calculateBMI(Float.parseFloat(str), Float.parseFloat(this.height_txt) / 100.0f)));
            this.display_bmi.setText(result);
            String str2 = this.height_txt;
            this.getHeight_txt = str2;
            this.documentReference.update(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, str2, new Object[0]);
            this.documentReference.update("bmi", result, new Object[0]);
            alertDialog.dismiss();
            return;
        }
        Toast.makeText(getActivity(), "Enter Your Weight First", Toast.LENGTH_SHORT).show();
    }

    private float calculateBMI(float weight2, float height) {
        return weight2 / (height * height);
    }
}