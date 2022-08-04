package com.example.retrofit_with_recyclerview.responses;

import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.models.Person;
import com.example.retrofit_with_recyclerview.models.Show;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo mapeamento de um JSON para uma entidade movie.
 *
 * Dúvida: Que mal faz essa classe já ser o model?
 */
public class MediaResponse {
    @Json(name = "id")
    private final long id;

    @Json(name = "original_title")
    private final String title;

    @Json(name = "original_name")
    private final String name;

    @Json(name = "poster_path")
    private final String postPath;

    @Json(name = "know_for")
    private final List<MediaResponse> moviesAndShows;

    @Json(name = "media_type")
    private final String mediaType;

    @Json(name = "revenue")
    private final int revenue;

    @Json(name = "budget")
    private final int budget;

    public MediaResponse(long id, String title, String name, String postPath,
                         List<MediaResponse> mediaResponse, String mediaType, int revenue, int budget) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.postPath = postPath;
        this.moviesAndShows = mediaResponse;
        this.mediaType = mediaType;
        this.budget = budget;
        this.revenue = revenue;
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

    public String getName() {
        return name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public List<MediaResponse> getMoviesAndShows() {
        return moviesAndShows;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getBudget() {
        return budget;
    }

    /**
     * Retorna o tipo específico (subtipo) de Media.
     *
     * obs:
     *  As vezes, a Media obtida do themoviedb, vem com o mediaType nulo.
     *  Aqui, eu faço com que a Media sempre tenha um mediaType não nulo.
     */
    public Media getEntity(){
        // Media é Movie
        if ((Constants.MOVIE_TYPE.equals(mediaType) || title != null) && moviesAndShows == null)
            return new Movie(id, title, postPath, Constants.MOVIE_TYPE, this.revenue, this.budget);

        // Media é Person
        else if(Constants.PERSON_TYPE.equals(mediaType) || moviesAndShows != null)
            return new Person(id, name, mapperMediaResponseListToMediaList(), Constants.PERSON_TYPE);

        //Media é Show
        else if ((Constants.SHOW_TYPE.equals(mediaType) || name != null) && moviesAndShows == null)
            return new Show(id, name, postPath, Constants.SHOW_TYPE);

        else
            throw new RuntimeException("This media is an unknown media type");
    }

    private List<Media> mapperMediaResponseListToMediaList(){
        List<Media> meediaList = new ArrayList<>();

        if(this.moviesAndShows == null || this.moviesAndShows.isEmpty()) return meediaList;

        for(MediaResponse mediaResponse : this.moviesAndShows){
            Media newMedia;
            String currMediaType = mediaResponse.getMediaType();

            if("movie".equals(currMediaType))
                newMedia = new Movie(
                mediaResponse.getId(),
                mediaResponse.getTitle(),
                mediaResponse.getPostPath(),
                Constants.MOVIE_TYPE);

            else if("tv".equals(currMediaType))
                newMedia = new Show(
                mediaResponse.getId(),
                mediaResponse.getName(),
                mediaResponse.getPostPath(),
                Constants.SHOW_TYPE);

            else throw new RuntimeException("The person have a unknown media type");

            meediaList.add(newMedia);
        }

        return meediaList;
    }

    public String toString(){
        return "id: " + id + " tittle: " + title + " name: " + name + " postPath: " + postPath +
                " moviesAndShows: " + moviesAndShows + " mediaType: " + mediaType;
    }
}
