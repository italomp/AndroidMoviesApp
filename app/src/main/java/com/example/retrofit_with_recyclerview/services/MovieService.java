package com.example.retrofit_with_recyclerview.services;

import com.example.retrofit_with_recyclerview.responses.CrewResponse;
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

    // Talvez dê erro querer acessar a listagem diretamente.
    // Posso precisar criar um objeto que ecapsule esse atributo crew que está presente na response.
    @GET("movie/{movie_id}/credits")
    Call<CrewResponse> getCreditsByMovie(@Path("movie_id") long movieId, @Query("api_key") String apiKey);
}
