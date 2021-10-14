package com.example.myfitmeapp.api;

import com.example.myfitmeapp.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<Headlines> getHeadlines(
            @Query("country") String str,
             @Query("apiKey") String str2);

    @GET("top-headlines")
    Call<Headlines> getSpecificData(
            @Query("country") String str,
            @Query("category") String str2,
            @Query("apiKey") String str3);
}
