package com.example.myfitmeapp.BottomNav_Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myfitmeapp.Breathing_Fragment;
import com.example.myfitmeapp.Delta_Activity;
import com.example.myfitmeapp.EchoActivity;
import com.example.myfitmeapp.Golf_Activity;
import com.example.myfitmeapp.Health_NutritionActivity;
import com.example.myfitmeapp.India_Activity;
import com.example.myfitmeapp.Meditation.Accepting_Activity;
import com.example.myfitmeapp.Meditation.Body_Activity;
import com.example.myfitmeapp.Meditation.Breathing_Activity;
import com.example.myfitmeapp.Meditation.Connect_Activity;
import com.example.myfitmeapp.Meditation.Cultivative_Activity;
import com.example.myfitmeapp.Meditation.Stay_Activity;
import com.example.myfitmeapp.Meditation.Training_Activity;
import com.example.myfitmeapp.Meditation_PageActivity;
import com.example.myfitmeapp.Menstrual_Fragment;
import com.example.myfitmeapp.Morning_Fragment;
import com.example.myfitmeapp.Motherhood_Fragment;
import com.example.myfitmeapp.Payment_PageActivity;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.Soothing_Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ExploreFragment extends Fragment {

    DocumentReference documentReference;
    FirebaseFirestore fStore;
    String userID;
    TextView userName;

    private RelativeLayout internetLayout;
    private RelativeLayout noInternetLayout;

    private ImageView revive,india,delta,echo,golf,mother,menstrual,soothing,breathing,morning,nutrition;
    private ProgressBar progressBar,progressBar2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);

        internetLayout = view.findViewById(R.id.internetLayout);
        noInternetLayout = view.findViewById(R.id.noInternetLayout);
        Button tryAgainButton = view.findViewById(R.id.try_again_button);

        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        userName = view.findViewById(R.id.userName);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar2 = view.findViewById(R.id.progress_bar2);

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullname")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userName.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        drawLayout();

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLayout();
            }
        });

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        TextView textView = view.findViewById(R.id.greetText);
        if(timeOfDay < 12){
            textView.setText("GOOD MORNING, ");
        } else {
            textView.setText("GOOD EVENING, ");
        }

        view.findViewById(R.id.revive).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), Payment_PageActivity.class)));

        view.findViewById(R.id.indiaButton).setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), India_Activity.class)));

        view.findViewById(R.id.golfButton).setOnClickListener(view12 ->
                startActivity(new Intent(getActivity(), Golf_Activity.class)));

        view.findViewById(R.id.deltaButton).setOnClickListener(view13 ->
                startActivity(new Intent(getActivity(), Delta_Activity.class)));

        view.findViewById(R.id.echoButton).setOnClickListener(view14 ->
                startActivity(new Intent(getActivity(), EchoActivity.class)));

        view.findViewById(R.id.motherhoodButton).setOnClickListener(view15 ->
                startActivity(new Intent(getActivity(), Motherhood_Fragment.class)));

        view.findViewById(R.id.menstrualButton).setOnClickListener(view16 ->
                startActivity(new Intent(getActivity(), Menstrual_Fragment.class)));

        view.findViewById(R.id.soothingButton).setOnClickListener(view17 ->
                startActivity(new Intent(getActivity(), Soothing_Fragment.class)));

        view.findViewById(R.id.breathingButton).setOnClickListener(view18 ->
                startActivity(new Intent(getActivity(), Breathing_Fragment.class)));

        view.findViewById(R.id.morningButton).setOnClickListener(view19 ->
                startActivity(new Intent(getActivity(), Morning_Fragment.class)));

        view.findViewById(R.id.forwardButton).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), Meditation_PageActivity.class)));

        view.findViewById(R.id.nutritionButton).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), Health_NutritionActivity.class)));

        view.findViewById(R.id.stay_circle).setOnClickListener(view110 ->
                startActivity(new Intent(getActivity(), Stay_Activity.class)));

        view.findViewById(R.id.body_circle).setOnClickListener(view111 ->
                startActivity(new Intent(getActivity(), Body_Activity.class)));

        view.findViewById(R.id.breathing_circle).setOnClickListener(view112 ->
                startActivity(new Intent(getActivity(), Breathing_Activity.class)));

        view.findViewById(R.id.training_circle).setOnClickListener(view113 ->
                startActivity(new Intent(getActivity(), Training_Activity.class)));

        view.findViewById(R.id.accepting_circle).setOnClickListener(view114 ->
                startActivity(new Intent(getActivity(), Accepting_Activity.class)));

        view.findViewById(R.id.cultivative_circle).setOnClickListener(view115 ->
                startActivity(new Intent(getActivity(), Cultivative_Activity.class)));

        view.findViewById(R.id.connect_circle).setOnClickListener(view116 ->
                startActivity(new Intent(getActivity(), Connect_Activity.class)));

        revive = view.findViewById(R.id.revive);
        india = view.findViewById(R.id.india);
        delta = view.findViewById(R.id.delta);
        echo = view.findViewById(R.id.echo);
        golf = view.findViewById(R.id.golf);
        mother = view.findViewById(R.id.mother);
        menstrual = view.findViewById(R.id.menstrual);
        soothing = view.findViewById(R.id.soothing);
        breathing = view.findViewById(R.id.breathing);
        morning = view.findViewById(R.id.morning);
        nutrition = view.findViewById(R.id.nutritionButton);

        //loadContent();

        return view;
    }

    private void loadContent() {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("drawables");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(requireActivity())
                        .load(snapshot.child("revive").getValue().toString())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(revive);
                Glide.with(requireActivity()).load(snapshot.child("india").getValue().toString()).into(india);
                Glide.with(requireActivity()).load(snapshot.child("delta").getValue().toString()).into(delta);
                Glide.with(requireActivity()).load(snapshot.child("echo").getValue().toString()).into(echo);
                Glide.with(requireActivity()).load(snapshot.child("golf").getValue().toString()).into(golf);
                Glide.with(requireActivity()).load(snapshot.child("mother").getValue().toString()).into(mother);
                Glide.with(requireActivity()).load(snapshot.child("menstrual").getValue().toString()).into(menstrual);
                Glide.with(requireActivity()).load(snapshot.child("soothing").getValue().toString()).into(soothing);
                Glide.with(requireActivity()).load(snapshot.child("breathing_circulation").getValue().toString()).into(breathing);
                Glide.with(requireActivity()).load(snapshot.child("morning").getValue().toString()).into(morning);
                Glide.with(requireActivity())
                        .load(snapshot.child("nutrition").getValue().toString())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar2.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar2.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(nutrition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void drawLayout() {
        if (isNetworkAvailable()) {
            internetLayout.setVisibility(View.VISIBLE);
            noInternetLayout.setVisibility(View.GONE);
        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            internetLayout.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}