package com.example.retrofit_with_recyclerview.response;

import com.squareup.moshi.Json;

/**
 * Classe responsável pelo mapeamento de um JSON para uma entidade movie.
 */
public class MovieMapper {
    // Anotação que identifica o atributo do JSON que será mapeado para o atributo anotado.
    @Json(name = "original_tittle")
    private final String movieTittle;

    @Json(name = "poster_path")
    private final String postPath;

    public MovieMapper(String movieTittle, String postPath) {
        this.movieTittle = movieTittle;
        this.postPath = postPath;
    }

    public String getMovieTittle() {
        return movieTittle;
    }

    public String getPostPath() {
        return postPath;
    }
}
