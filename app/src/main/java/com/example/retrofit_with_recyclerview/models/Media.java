package com.example.retrofit_with_recyclerview.models;

import com.example.retrofit_with_recyclerview.util.Constants;

import java.util.List;

public abstract class Media {
    protected final String mediaType;

    protected final String title; //Movie
    protected final List<Media> moviesAndShows; // Person

    public Media(String mediaType) {
        this.mediaType = mediaType;
        this.moviesAndShows = null;
        this.title = null;
    }

    public Media(String mediaType, String title) {
        this.mediaType = mediaType;
        this.title = title;
        this.moviesAndShows = null;
    }

    public Media(String mediaType, List<Media> moviesAndShows) {
        this.mediaType = mediaType;
        this.moviesAndShows = moviesAndShows;
        this.title = null;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getTitle() {
        return title;
    }

    public List<Media> getMoviesAndShows() {
        return moviesAndShows;
    }

    public String getSubType(){
        if (title != null) return Constants.MOVIE_TYPE;
        else if (!moviesAndShows.isEmpty()) return Constants.PERSON_TYPE;
        else return Constants.SHOW_TYPE;
    }
}
