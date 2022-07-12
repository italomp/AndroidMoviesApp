package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.CrewResponse;
import com.example.retrofit_with_recyclerview.responses.movies.MovieDetailsResponse;
import com.example.retrofit_with_recyclerview.responses.movies.MovieResponseList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Os recursos consumidos por esse service são referentes aos filmes (Movie).
 */
public interface MovieService {
    @GET("movie/popular")
    Call<MovieResponseList> getMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetailsResponse> getMovieDetails(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<CrewResponse> getCreditsByMovie(@Path("movie_id") long movieId, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponseList> searchMovies(@Query("api_key") String apiKey, @Query("query") String query);
}
