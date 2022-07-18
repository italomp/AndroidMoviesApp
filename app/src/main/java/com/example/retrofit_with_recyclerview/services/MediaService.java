package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.MediaResponseList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MediaService {

    @GET("search/multi")
    Call<MediaResponseList> multiSearch(@Query("api_key") String apiKey, @Query("query") String query);
}
