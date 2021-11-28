package com.example.newsproject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetroInterface {

    @GET("top-headlines")
    Call<OuterClass> getNews(@QueryMap Map<String, String> parameters);

}
