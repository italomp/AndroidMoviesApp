package com.example.retrofit_with_recyclerview.services;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiService {
    private static MovieService movieService;
    private static MediaService mediaService;
    private static ShowService showService;

    public static MovieService getMovieService(){
        if(movieService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            movieService = retrofit.create(MovieService.class);
        }
        return movieService;
    }

    public static MediaService getMediaService(){
        if(mediaService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            mediaService = retrofit.create(MediaService.class);
        }
        return mediaService;
    }

    public static ShowService getShowService(){
        if(showService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            showService = retrofit.create(ShowService.class);
        }
        return showService;
    }
}
