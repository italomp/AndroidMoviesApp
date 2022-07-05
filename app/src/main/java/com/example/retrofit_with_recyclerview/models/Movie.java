package com.example.retrofit_with_recyclerview.models;

public class Movie {
    private final long id;
    private final String movieTittle;
    private final String posterPath;

    public Movie(long id, String movieTittle, String posterPath) {
        this.id = id;
        this.movieTittle = movieTittle;
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }
    public String getMovieTittle() {
        return movieTittle;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
