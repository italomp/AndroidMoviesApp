package com.example.retrofit_with_recyclerview.util;

import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;

import java.util.ArrayList;
import java.util.List;

public class MediaMapper {

    public static List<Media> fromMediaResponseToMedia(List<MediaResponse> mediaResponseList){
        List<Media> mediaList = new ArrayList<>();
        for(MediaResponse mediaResponse : mediaResponseList){
            Media newMovie = mediaResponse.getEntity();
            mediaList.add(newMovie);
        }
        return mediaList;
    }

    /*
    private final long id;
    private final String title;
    private final String name;
    private final String posterPath;
    private final List<Media> moviesAndShows;
    private final String mediaType;

    public Media(long id, String title, String name, String posterPath, List<Media> moviesAndShows, String mediaType) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.posterPath = posterPath;
        this.moviesAndShows = moviesAndShows;
        this.mediaType = mediaType;
    }

    public String getMediaType(){
        return this.mediaType;
    }

    public Movie getMovie(){
        return new Movie(id, title, posterPath);
    }

    public Show getShow(){
        return new Show(id, name, posterPath);
    }

    public Person getPerson(){
        return  new Person(name, moviesAndShows);
    }
     */
}
