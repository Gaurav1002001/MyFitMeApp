package com.example.myfitmeapp.BottomNavFragments;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.Adapter.NewsAdapter;
import com.example.myfitmeapp.Adapter.PostAdapter;
import com.example.myfitmeapp.CoronaData.CoronaPageActivity;
import com.example.myfitmeapp.Model.Articles;
import com.example.myfitmeapp.Model.Headlines;
import com.example.myfitmeapp.Model.Post;
import com.example.myfitmeapp.R;
import com.example.myfitmeapp.api.ApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    private NewsAdapter adapter;
    private ProgressBar mProgressCircle;
    private List<String> followingList;
    private List<Post> mPosts;
    List<Articles> articles = new ArrayList<>();

    final String API_KEY = "0eed3cbb09f346d7a8a86cf7e77c38c8";
    String category = "health";

    private RelativeLayout internetLayout;
    private RelativeLayout noInternetLayout;
    private LinearLayout linearLayout;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        linearLayout = view.findViewById(R.id.linearLayout);
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

        followingList = new ArrayList<>();
        checkFollowingUsers();

        return view;
    }

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPosts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)){
                            mPosts.add(post);
                        }
                    }
                }

                if (mPosts.isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
                }

                postAdapter = new PostAdapter(getActivity(), mPosts);
                recyclerViewPosts.setAdapter(postAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                    adapter = new NewsAdapter(getActivity(), articles);
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