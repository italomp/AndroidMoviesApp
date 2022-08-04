package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.CrewResponse;
import com.example.retrofit_with_recyclerview.responses.MediaDetailsResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Os recursos consumidos por esse service são referentes aos filmes (Movie).
 */
public interface MovieService {
    @GET("movie/popular")
    Call<MediaResponseList> getMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MediaDetailsResponse> getMovieDetails(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<CrewResponse> getCreditsByMovie(@Path("movie_id") long movieId, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MediaResponseList> getMoviesByYear(@Query("api_key") String apiKey, @Query("year") int year);

    @GET("discover/movie")
    Call<MediaResponseList> getMoviesByYear(@Query("api_key") String apiKey,
                                            @Query("primary_release_year") int year, @Query("sort_by") String sortBy);

}
