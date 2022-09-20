package com.example.retrofit_with_recyclerview.fragments.search;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    ArrayList<View[]> cardsComponentsList;

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
        this.fillCardsComponentsList();
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
        System.out.println("dentro do fillItemList");
        TextView titleMediaView;// = new TextView(getContext());
        ImageView posterMediaView;// = new ImageView(getContext());
        int amountMediaInGridView = 20;

        for(int i = 0; i < amountMediaInGridView; i++){
            Media currentMedia = mediaList.get(i);
//            View[] views = getMediaTitleViewAndMediaPosterView(i);
            titleMediaView = (TextView) this.cardsComponentsList.get(i)[0];   // views[0];
            posterMediaView = (ImageView) this.cardsComponentsList.get(i)[1];    // views[1];

            System.out.println("i dentro do fillItemList: " + i);
            System.out.println("titleMediaView == null: " + (titleMediaView == null));
            System.out.println("posterMediaView == null: " + (posterMediaView == null));

            setTitleMediaView(titleMediaView, currentMedia);
            setPosterMovie(posterMediaView, currentMedia);

            setOnClickListener(titleMediaView, currentMedia);
            setOnClickListener(posterMediaView, currentMedia);
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

    public void fillCardsComponentsList(){
        this.cardsComponentsList = new ArrayList<>();
        int maxAmountItems = 20;

        for(int i = 0; i < maxAmountItems; i++){
            switch(i){
                case 0:
                     this.cardsComponentsList.add(new View[] {
                             view.findViewById(R.id.media_title_1),
                                view.findViewById(R.id.image_media_poster_1)});

                case 1:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_2),
                            view.findViewById(R.id.image_media_poster_2)});

                case 2:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_3),
                            view.findViewById(R.id.image_media_poster_3)});

                case 3:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_4),
                            view.findViewById(R.id.image_media_poster_4)});

                case 4:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_5),
                            view.findViewById(R.id.image_media_poster_5)});

                case 5:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_6),
                            view.findViewById(R.id.image_media_poster_6)});

                case 6:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_7),
                            view.findViewById(R.id.image_media_poster_7)});

                case 7:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_8),
                            view.findViewById(R.id.image_media_poster_8)});

                case 8:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_9),
                            view.findViewById(R.id.image_media_poster_9)});

                case 9:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_10),
                            view.findViewById(R.id.image_media_poster_10)});

                case 10:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_11),
                            view.findViewById(R.id.image_media_poster_11)});

                case 11:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_12),
                            view.findViewById(R.id.image_media_poster_12)});

                case 12:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_13),
                            view.findViewById(R.id.image_media_poster_13)});

                case 13:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_14),
                            view.findViewById(R.id.image_media_poster_14)});

                case 14:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_15),
                            view.findViewById(R.id.image_media_poster_15)});

                case 15:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_16),
                            view.findViewById(R.id.image_media_poster_16)});

                case 16:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_17),
                            view.findViewById(R.id.image_media_poster_17)});

                case 17:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_18),
                            view.findViewById(R.id.image_media_poster_18)});

                case 18:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_19),
                            view.findViewById(R.id.image_media_poster_19)});

                case 19:
                    this.cardsComponentsList.add(new View[] {
                            view.findViewById(R.id.media_title_20),
                            view.findViewById(R.id.image_media_poster_20)});
            }
        }

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
                                    // removendo grid items
                                    GridLayout gridLayout = view.findViewById(R.id.movies_list);
                                    gridLayout.removeAllViews();
                                    cardsComponentsList.clear();
                                    fillCardsComponentsList();

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