package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Essa classe é responsável por mapear um JSON para uma lista de movies.
 */
public class MediaResponseList {
    // Anotação que identifica o atributo do JSON que será mapeado para o atributo anotado.
    @Json(name = "results")
    private final List<MediaResponse> mediaList;

    public MediaResponseList(List<MediaResponse> mediaList) {
        this.mediaList = mediaList;
    }

    public List<MediaResponse> getMediaList() {
        return mediaList;
    }
}
