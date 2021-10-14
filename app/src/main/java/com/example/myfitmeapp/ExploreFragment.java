package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myfitmeapp.Meditation.Accepting_Activity;
import com.example.myfitmeapp.Meditation.Body_Activity;
import com.example.myfitmeapp.Meditation.Breathing_Activity;
import com.example.myfitmeapp.Meditation.Connect_Activity;
import com.example.myfitmeapp.Meditation.Cultivative_Activity;
import com.example.myfitmeapp.Meditation.Stay_Activity;
import com.example.myfitmeapp.Meditation.Training_Activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExploreFragment extends Fragment {

    DocumentReference documentReference;
    FirebaseFirestore fStore;
    String userID;
    TextView userName;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_explore, container, false);

        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        userName = view.findViewById(R.id.userName);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName.setText(documentSnapshot.getString("fullName"));
            }
        });

        SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public final void onRefresh() {
                //ExploreFragment.lambda$onCreateView$1(SwipeRefreshLayout.this);
            }
        });

        view.findViewById(R.id.indiaButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),India_Activity.class));
            }
        });

        view.findViewById(R.id.golfButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Golf_Activity.class));
            }
        });

        view.findViewById(R.id.deltaButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Delta_Activity.class));
            }
        });

        view.findViewById(R.id.echoButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),EchoActivity.class));
            }
        });

        view.findViewById(R.id.motherhoodButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Motherhood_Fragment.class));
            }
        });

        view.findViewById(R.id.menstrualButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Menstrual_Fragment.class));
            }
        });

        view.findViewById(R.id.soothingButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Soothing_Fragment.class));
            }
        });

        view.findViewById(R.id.breathingButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Breathing_Fragment.class));
            }
        });

        view.findViewById(R.id.morningButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(),Morning_Fragment.class));
            }
        });

        view.findViewById(R.id.forwardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Meditation_PageActivity.class));
            }
        });

        view.findViewById(R.id.stay_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Stay_Activity.class));
            }
        });

        view.findViewById(R.id.body_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Body_Activity.class));
            }
        });

        view.findViewById(R.id.breathing_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Breathing_Activity.class));
            }
        });

        view.findViewById(R.id.training_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Training_Activity.class));
            }
        });

        view.findViewById(R.id.accepting_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Accepting_Activity.class));
            }
        });

        view.findViewById(R.id.cultivative_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Cultivative_Activity.class));
            }
        });

        view.findViewById(R.id.connect_circle).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                startActivity(new Intent(getActivity(), Connect_Activity.class));
            }
        });

        return view;
    }
}