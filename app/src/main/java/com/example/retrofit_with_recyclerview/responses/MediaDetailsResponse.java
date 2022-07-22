package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

public class MediaDetailsResponse extends MediaResponse {
    @Json(name = "overview")
    private final String overview;

    @Json(name = "vote_average")
    private final Float voteAverage; // range de 0 a 10

    public MediaDetailsResponse(long id, String movieTittle, String postPath,
                                String overview, Float voteAverage) {
        super(id, movieTittle, null, postPath, null, null);
        this.overview = overview;
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    /**
     * @return a média do filme num range de 0 a 100.
     */
    public Integer getVoteAverage() {
        return this.voteAverage.intValue() * 10;
    }
}
