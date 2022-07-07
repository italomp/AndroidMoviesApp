package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

public class MovieDetailsResponse extends MovieResponse{
    //sinopse, directories, stories, screenplays,
    @Json(name = "overview")
    private final String overview;

    //private final List<String> directors;
    //private final List<String> screenwriters;
    //private final List<String> storyCreators;

    @Json(name = "vote_average")
    private final Float voteAverage;

    public MovieDetailsResponse(long id, String movieTittle, String postPath, String overview /*,
                                List<String> directors, List<String> screenwriters,
                                List<String> storyCreatos*/, Float voteAverage) {
        super(id, movieTittle, postPath);
        this.overview = overview;
        //this.directors = directors;
        //this.screenwriters = screenwriters;
        //this.storyCreators = storyCreatos;
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }
}
