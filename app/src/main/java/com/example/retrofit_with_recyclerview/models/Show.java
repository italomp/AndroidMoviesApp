package com.example.retrofit_with_recyclerview.models;
public class Show extends Media {
    private final long id;
    private final String name;
    private final String posterPath;

    public Show(long id, String name, String posterPath, String mediaType) {
        super(id, mediaType);
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
