package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.MovieDetailsResponse;
import com.example.retrofit_with_recyclerview.responses.MovieResponse;
import com.example.retrofit_with_recyclerview.responses.MovieResponseList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Call<MovieResponseList> getMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetailsResponse> getMovieDetails(@Path("id") long id, @Query("api_key") String apiKey);
}
