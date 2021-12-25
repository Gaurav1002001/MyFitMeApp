package com.example.myfitmeapp.BottomNav_Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
        documentReference.get().addOnSuccessListener(documentSnapshot ->
                userName.setText(documentSnapshot.getString("fullName")));

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

        view.findViewById(R.id.imageView5).setOnClickListener(v ->
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

        return view;
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