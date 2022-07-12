package com.example.retrofit_with_recyclerview.responses.movies;

import com.squareup.moshi.Json;

/**
 * Classe responsável pelo mapeamento de um JSON para uma entidade movie.
 *
 * Dúvida: Que mal faz essa classe já ser o model?
 */
public class MovieResponse{
    // Anotação que identifica o atributo do JSON que será mapeado para o atributo anotado.
    @Json(name = "id")
    protected final long id;

    @Json(name = "original_title")
    private final String title;

    @Json(name = "poster_path")
    protected final String postPath;

    public MovieResponse(long id, String title, String postPath1) {
        this.id = id;
        this.title = title;
        this.postPath = postPath1;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPostPath() {
        return postPath;
    }
}
