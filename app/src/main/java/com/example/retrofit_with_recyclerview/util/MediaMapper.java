package com.example.retrofit_with_recyclerview.util;

import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.responses.movies.MovieResponse;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {

    public static List<Movie> fromMovieResponseToMovie(List<MovieResponse> movieResponseList){
        List<Movie> movieList = new ArrayList<>();
        for(MovieResponse movieResponse: movieResponseList){
            Movie newMovie = new Movie(
                    movieResponse.getId(),
                    movieResponse.getTitle(),
                    movieResponse.getPostPath());
            movieList.add(newMovie);
        }
        return movieList;
    }
}
