package com.example.retrofit_with_recyclerview.responses.movies;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Essa classe é responsável por mapear um JSON para uma lista de movies.
 */
public class MovieResponseList {
    // Anotação que identifica o atributo do JSON que será mapeado para o atributo anotado.
    @Json(name = "results")
    private final List<MovieResponse> movies;

    public MovieResponseList(List<MovieResponse> movies) {
        this.movies = movies;
    }

    public List<MovieResponse> getMovies() {
        return movies;
    }
}
