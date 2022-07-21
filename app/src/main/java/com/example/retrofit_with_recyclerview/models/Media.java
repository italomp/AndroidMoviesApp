package com.example.retrofit_with_recyclerview.models;

import com.example.retrofit_with_recyclerview.util.Constants;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class Media implements Serializable {
    protected final long id;
    protected final String mediaType;

    protected final String title; //Movie
    protected final List<Media> moviesAndShows; // Person

    public Media(long id, String mediaType) {
        this.id = id;
        this.mediaType = mediaType;
        this.moviesAndShows = null;
        this.title = null;
    }

    public Media(long id, String mediaType, String title) {
        this.id = id;
        this.mediaType = mediaType;
        this.title = title;
        this.moviesAndShows = null;
    }

    public Media(long id, String mediaType, List<Media> moviesAndShows) {
        this.id = id;
        this.mediaType = mediaType;
        this.moviesAndShows = moviesAndShows;
        this.title = null;
    }

    public String getTitle() {
        return title;
    }

    public List<Media> getMoviesAndShows() {
        return moviesAndShows;
    }

    public String getSubType(){
        if (title != null || Constants.MOVIE_TYPE.equals(mediaType))
            return Constants.MOVIE_TYPE;

        else if (Constants.PERSON_TYPE.equals(mediaType) || moviesAndShows != null)
            return Constants.PERSON_TYPE;

        else
            return Constants.SHOW_TYPE;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return id == media.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
