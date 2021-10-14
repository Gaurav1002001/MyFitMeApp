package com.example.myfitmeapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URl = "https://newsapi.org/v2/";
    private static ApiClient apiClient;
    private static Retrofit retrofit;

    private ApiClient() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URl).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized ApiClient getInstance() {
        ApiClient apiClient2;
        synchronized (ApiClient.class) {
            if (apiClient == null) {
                apiClient = new ApiClient();
            }
            apiClient2 = apiClient;
        }
        return apiClient2;
    }

    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }
}
