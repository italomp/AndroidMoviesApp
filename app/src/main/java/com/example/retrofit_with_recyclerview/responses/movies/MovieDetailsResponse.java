package com.example.retrofit_with_recyclerview.responses.movies;

import com.squareup.moshi.Json;

public class MovieDetailsResponse extends MovieResponse{
    @Json(name = "overview")
    private final String overview;

    @Json(name = "vote_average")
    private final Float voteAverage;

    public MovieDetailsResponse(long id, String movieTittle, String postPath,
                                String overview, Float voteAverage) {
        super(id, movieTittle, postPath);
        this.overview = overview;
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }
}
