package com.example.retrofit_with_recyclerview.models;

public class Movie {
    private String movieTittle;
    private String postPath;

    public Movie(String movieTittle, String postPath) {
        this.movieTittle = movieTittle;
        this.postPath = postPath;
    }

    public String getMovieTittle() {
        return movieTittle;
    }

    public void setMovieTittle(String movieTittle) {
        this.movieTittle = movieTittle;
    }

    public String getPostPath() {
        return postPath;
    }

    public void setPostPath(String postPath) {
        this.postPath = postPath;
    }
}
