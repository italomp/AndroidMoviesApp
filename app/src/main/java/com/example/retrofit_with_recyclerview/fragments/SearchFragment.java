package com.example.retrofit_with_recyclerview.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.activities.MediaDetailsActivity;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.models.Person;
import com.example.retrofit_with_recyclerview.models.Show;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.MediaMapper;
import com.example.retrofit_with_recyclerview.util.MyWindowMetrics;
import com.example.retrofit_with_recyclerview.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    SearchView searchView;
    ProgressBar progressBar;
    View view;
    GridLayout gridLayout;
    MyWindowMetrics.WindowSizeClass widthWindowSizeClass;
    MyWindowMetrics.WindowSizeClass heightWindowSizeClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        setViews();
        getMovies();
        setSearchViews();
        return view;
    }

    public void setViews(){
        this.progressBar = this.view.findViewById(R.id.progress_bar_search_fragment);
        this.setGridLayout();
    }

    public void setGridLayout(){
        gridLayout = view.findViewById(R.id.grid_layout);
        MyWindowMetrics myWindowMetrics = new MyWindowMetrics(getActivity());
        widthWindowSizeClass = myWindowMetrics.getWidthSizeClass();
        heightWindowSizeClass = myWindowMetrics.getHeightSizeClass();

        // Phone port
        if(widthWindowSizeClass == MyWindowMetrics.WindowSizeClass.COMPACT){
            gridLayout.setColumnCount(2);
        }
        // Phone land
        else if(heightWindowSizeClass == MyWindowMetrics.WindowSizeClass.COMPACT){
            gridLayout.setColumnCount(3);
        }
        // Tablet port
        else if(widthWindowSizeClass == MyWindowMetrics.WindowSizeClass.MEDIUM){
            gridLayout.setColumnCount(4);
        }
        // Tablet land
        else if(heightWindowSizeClass == MyWindowMetrics.WindowSizeClass.MEDIUM &&
            widthWindowSizeClass == MyWindowMetrics.WindowSizeClass.EXPANDED){
            gridLayout.setColumnCount(6);
        }
    }

    public void showErrorMessage(View view, String msg){
        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void renderingMediasOrNotFoundMessage(List<Media> mediaList){
        System.out.println("dentro do renderingMediasOrNotFoundMessage");
        if (!mediaList.isEmpty())
            fillItemList(mediaList);

        else
            showErrorMessage(this.view, "Nenhuma mídia foi encontrada.");
    }

    public void getMovies(){
        ScrollView scrollView = this.view.findViewById(R.id.scroll_search_views);
        Util.showProgressBarAndHiddenView(this.progressBar, new View[]{scrollView});
        ApiService.getMovieService().getMovies(Constants.API_KEY).enqueue(new Callback<MediaResponseList>() {
            @Override
            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                // Status code de 200 a 299
                if(response.isSuccessful()){
                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);
                    fillItemList(mediaList);

                    Util.hiddenProgressBarAndShowView(progressBar, new View[]{scrollView});
                }
                else{
                    // Poderia tratar alguns casos de erro específicos...
                    showErrorMessage(view, "HTTP Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MediaResponseList> call, Throwable t) {
                showErrorMessage(view,"Falha ao carregar filmes");
                throw new RuntimeException(t.getMessage());
            }
        });
    }

    public void fillItemList(List<Media> mediaList){
        int maxGridViewsAmount = 20;
        if(mediaList.size() < 20)
            maxGridViewsAmount = mediaList.size();

        for(int i = 0; i < maxGridViewsAmount; i++){
            Media currentMedia = mediaList.get(i);
            CardView newGridView = (CardView) LayoutInflater
                    .from(view.getContext())
                    .inflate(R.layout.media_card_view, gridLayout, false);
            TextView titleMediaView = newGridView.findViewById(R.id.media_title);
            ImageView posterMediaView = newGridView.findViewById(R.id.image_media_poster);

            setTitleMediaView(titleMediaView, currentMedia);
            setPosterMovie(posterMediaView, currentMedia);

            setOnClickListener(titleMediaView, currentMedia);
            setOnClickListener(posterMediaView, currentMedia);

            gridLayout.addView(newGridView);
        }
    }

    public void setOnClickListener(View view, Media media){
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MediaDetailsActivity.class);
                        intent.putExtra("media", media);
                        startActivity(intent);
                    }
                }
        );
    }

    public void setPosterMovie(ImageView posterMediaView, Media media){
        String posterPath = "";

        if(Util.isItMovie(media))
            posterPath = ((Movie) media).getPosterPath();
        else if(Util.isItShow(media))
            posterPath = ((Show) media).getPosterPath();

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                .into(posterMediaView);
    }

    public void setTitleMediaView(TextView titleMediaView, Media media){
        String mediaTitle = "";

        if(Util.isItMovie(media))
            mediaTitle = ((Movie) media).getTitle();
        else if(Util.isItShow(media))
            mediaTitle = ((Show) media).getName();

        titleMediaView.setText(mediaTitle);
    }

    public void setSearchViews(){
        this.searchView = this.view.findViewById(R.id.search_view);
        this.searchView.clearFocus();

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Pesquisando Filmes, Shows e Pessoas
                ApiService.getMediaService()
                        .multiSearch(Constants.API_KEY, newText)
                        .enqueue(new Callback<MediaResponseList>() {
                            @Override
                            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                                System.out.println("onResponse dentro do setSearchViews");
                                if(response.isSuccessful()){
                                    // Removendo itens da listagem anterior
                                    gridLayout.removeAllViews();

                                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);

                                    // Extraindo Movies e Shows de objetos Person
                                    mediaList = parseMedia(mediaList);

                                    renderingMediasOrNotFoundMessage(mediaList);
                                }
                                else{
                                    showErrorMessage(view, "HTTP Status Code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<MediaResponseList> call, Throwable t) {

                            }
                        });

                return true;
            }
        });
    }

    /**
     * Esse método recebe uma lista de Medias contendo Medias do tipo Person e retorna
     * uma lista de Medias contendo apenas Movies e Shows.
     */
    public List<Media> parseMedia(List<Media> mediaList){
        List<Media> result = new ArrayList<>();
        Set<Media> mediaSet = new HashSet<>();

        for(Media media : mediaList){
            if(Util.isItMovie(media) || Util.isItShow(media))
                mediaSet.add(media);

            else
                mediaSet.addAll(((Person) media).getMoviesAndShows());
        }

        result.addAll(mediaSet);
        return result;
    }


}