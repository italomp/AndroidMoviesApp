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
}
