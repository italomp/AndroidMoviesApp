package com.example.retrofit_with_recyclerview.models;

public class Movie {
    private final long id;
    private final String title;
    private final String posterPath;

    public Movie(long id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
