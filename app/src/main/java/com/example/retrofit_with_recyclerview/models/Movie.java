package com.example.retrofit_with_recyclerview.models;

public class Movie {
    private String movieTittle;
    private String posterPath;

    public Movie(String movieTittle, String posterPath) {
        this.movieTittle = movieTittle;
        this.posterPath = posterPath;
    }

    public String getMovieTittle() {
        return movieTittle;
    }

    public void setMovieTittle(String movieTittle) {
        this.movieTittle = movieTittle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
