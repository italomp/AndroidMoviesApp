package com.example.retrofit_with_recyclerview.responses.shows;

import com.squareup.moshi.Json;

public class ShowResponse {
    @Json(name = "original_name")
    private final String title;

    @Json(name = "poster_path")
    private final String postPath;

    @Json(name = "id")
    private final long id;

    public ShowResponse(String title, String postPath, long id) {
        this.title = title;
        this.postPath = postPath;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPostPath() {
        return postPath;
    }

    public long getId() {
        return id;
    }
}
