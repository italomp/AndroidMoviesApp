package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

public class MediaDetailsResponse extends MediaResponse {
    @Json(name = "overview")
    private final String overview;

    @Json(name = "vote_average")
    private final Float voteAverage;

    public MediaDetailsResponse(long id, String movieTittle, String postPath,
                                String overview, Float voteAverage) {
        super(id, movieTittle, null, postPath, null, null);
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
