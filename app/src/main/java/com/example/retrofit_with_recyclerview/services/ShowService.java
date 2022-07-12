package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.shows.ShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Os recursos consumidos por esse service são referentes aos shows.
 */
public interface ShowService {
    @GET("/search/tv")
    Call<ShowResponse> searchShows(@Query("api_key") String apiKey, @Query("query") String query);
}
