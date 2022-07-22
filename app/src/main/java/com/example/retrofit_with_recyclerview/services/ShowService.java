package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.CrewResponse;
import com.example.retrofit_with_recyclerview.responses.MediaDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShowService {
    @GET("tv/{tv_id}/credits")
    Call<CrewResponse> getCreditsByShow(@Path("tv_id") long movieId, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}")
    Call<MediaDetailsResponse> getShowDetails(@Path("tv_id") long id, @Query("api_key") String apiKey);
}
