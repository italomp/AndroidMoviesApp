package com.example.retrofit_with_recyclerview.models;

public class Movie extends Media {
    private final long id;
    private final String title;
    private final String posterPath;
    private final int revenue;
    private final int budget;

    public Movie(long id, String title, String posterPath, String mediaType) {
        super(id, mediaType, title);
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.revenue = 0;
        this.budget = 0;
    }

    public Movie(long id, String title, String posterPath, String mediaType, int revenue, int budget) {
        super(id, mediaType, title);
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.revenue = revenue;
        this.budget = budget;
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

    public int getRevenue() {
        return revenue;
    }

    public int getBudget() {
        return budget;
    }
}
