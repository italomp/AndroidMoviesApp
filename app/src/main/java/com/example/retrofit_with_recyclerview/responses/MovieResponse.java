package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

/**
 * Classe responsável pelo mapeamento de um JSON para uma entidade movie.
 *
 * Dúvida: Que mal faz essa classe já ser o model?
 */
public class MovieResponse {
    // Anotação que identifica o atributo do JSON que será mapeado para o atributo anotado.
    @Json(name = "original_title")
    private final String movieTittle;

    @Json(name = "poster_path")
    private final String postPath;

    @Json(name = "id")
    private final long id;

    public MovieResponse(long id, String movieTittle, String postPath) {
        this.id = id;
        this.movieTittle = movieTittle;
        this.postPath = postPath;
    }

    public String getMovieTittle() {
        return movieTittle;
    }

    public String getPostPath() {
        return postPath;
    }

    public long getId() {
        return id;
    }
}
