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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.Adapter.Adapter;
import com.example.myfitmeapp.Adapter.PostAdapter;
import com.example.myfitmeapp.CoronaPageActivity;
import com.example.myfitmeapp.Model.Articles;
import com.example.myfitmeapp.Model.Headlines;
import com.example.myfitmeapp.Model.Post;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.api.ApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView, recyclerViewPosts;
    private PostAdapter postAdapter;
    private Adapter adapter;
    private ProgressBar mProgressCircle;
    private List<Post> mPosts;
    private DatabaseReference mDatabaseRef;
    List<Articles> articles = new ArrayList<>();

    final String API_KEY = "0eed3cbb09f346d7a8a86cf7e77c38c8";
    String category = "health";

    private LinearLayout internetLayout;
    private RelativeLayout noInternetLayout;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        internetLayout = view.findViewById(R.id.internetLayout);
        noInternetLayout = view.findViewById(R.id.noInternetLayout);
        Button tryAgainButton = view.findViewById(R.id.try_again_button);
        view.findViewById(R.id.toolbar);
        view.findViewById(R.id.coronaButton).setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), CoronaPageActivity.class)));

        /*recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/
        final String country = getCountry();

        recyclerViewPosts = view.findViewById(R.id.postRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        recyclerViewPosts.setHasFixedSize(true);

        mProgressCircle = view.findViewById(R.id.progress_circle);
        mPosts = new ArrayList<>();

        if (isNetworkAvailable()) {
            //retrieveJson(country, category, API_KEY);
            readPosts();
            internetLayout.setVisibility(View.VISIBLE);
            noInternetLayout.setVisibility(View.GONE);
        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            internetLayout.setVisibility(View.GONE);
        }

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    //retrieveJson(country, category, API_KEY);
                    readPosts();
                    internetLayout.setVisibility(View.VISIBLE);
                    noInternetLayout.setVisibility(View.GONE);
                } else {
                    noInternetLayout.setVisibility(View.VISIBLE);
                    internetLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void readPosts() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("posts");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPosts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = new Post();

                    post.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    post.setDescription(snapshot.child("description").getValue().toString());
                    post.setTitle(snapshot.child("title").getValue().toString());
                    post.setTime(snapshot.child("time").getValue().toString());
                    post.setCalorie(snapshot.child("calorie").getValue().toString());
                    post.setPostDate(snapshot.child("postDate").getValue().toString());

                    mPosts.add(post);
                }

                postAdapter = new PostAdapter(getActivity(), mPosts);
                recyclerViewPosts.setAdapter(postAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void retrieveJson(String country, String category, String apiKey) {
        ApiClient.getInstance().getApi().getSpecificData(country, category, apiKey).enqueue(new Callback<Headlines>() {

            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(getActivity(), articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                internetLayout.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}