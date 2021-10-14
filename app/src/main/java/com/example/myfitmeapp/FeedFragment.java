package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.Model.Articles;
import com.example.myfitmeapp.Model.Headlines;
import com.example.myfitmeapp.api.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    final String API_KEY = "0eed3cbb09f346d7a8a86cf7e77c38c8";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    private Toolbar toolbar;
    String category = "health";

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        view.findViewById(R.id.coronaButton).setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(),CoronaPageActivity.class)));

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String country = getCountry();

        retrieveJson(country, category, API_KEY);
        return view;
    }

    public void retrieveJson(String country, String category, String apiKey) {
        ApiClient.getInstance().getApi().getSpecificData(country, category, apiKey).enqueue(new Callback<Headlines>() {

            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new com.example.myfitmeapp.Adapter(getActivity(),articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}