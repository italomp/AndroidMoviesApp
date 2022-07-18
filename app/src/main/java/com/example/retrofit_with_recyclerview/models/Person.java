package com.example.retrofit_with_recyclerview.models;

import java.util.List;

public class Person extends Media{
    private final String name;
    private final List<Media> moviesAndShows;

    public Person(String name, List<Media> moviesAndShows, String mediaType) {
        super(mediaType, moviesAndShows);
        this.name = name;
        this.moviesAndShows = moviesAndShows;
    }

    public String getName() {
        return name;
    }

    public List<Media> getMoviesAndShows() {
        return moviesAndShows;
    }
}
