package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.response.MovieListMapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Call<MovieListMapper> getMovies(@Query("api_key") String apiKey);
}
