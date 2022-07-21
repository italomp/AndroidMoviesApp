package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.CrewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShowService {
    @GET("tv/{tv_id}/credits")
    Call<CrewResponse> getCreditsByMovie(@Path("tv_id") long movieId, @Query("api_key") String apiKey);
}
