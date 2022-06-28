package com.example.retrofit_with_recyclerview.response;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Essa classe é responsável por mapear um JSON para uma lista de movies.
 */
public class MovieListMapper {
    // Anotação que identifica o atributo do JSON que será mapeado para o atributo anotado.
    @Json(name = "results")
    private final List<MovieMapper> movies;

    public MovieListMapper(List<MovieMapper> movies) {
        this.movies = movies;
    }

    public List<MovieMapper> getMovies() {
        return movies;
    }
}
