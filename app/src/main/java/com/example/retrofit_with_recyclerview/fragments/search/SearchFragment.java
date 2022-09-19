package com.example.retrofit_with_recyclerview.fragments.search;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
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
//    RecyclerView recyclerView;
//    MediaAdapter mediaAdapter;
    SearchView searchView;
    ProgressBar progressBar;
    GridLayout moviesList;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        this.moviesList = view.findViewById(R.id.movies_list);

        setViews();
        setSearchViews();
        getMovies();
        return view;
    }

    public void setViews(){
        this.progressBar = this.view.findViewById(R.id.progress_bar_search_fragment);
        this.setRecyclerView(view);
    }

    public void setRecyclerView(View view){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
//        this.mediaAdapter = new MediaAdapter(view.getContext());
//        this.mediaAdapter.setHasStableIds(true);

//        this.recyclerView = view.findViewById(R.id.recycler_medias);
//
//        this.recyclerView.setLayoutManager(layoutManager);
//        this.recyclerView.setHasFixedSize(true);
//        this.recyclerView.setAdapter(this.mediaAdapter);
    }

    public void showErrorMessage(View view, String msg){
        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void renderingMediasOrNotFoundMessage(List<Media> mediaList){
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
//                    mediaAdapter.setMediaList(mediaList);
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
        TextView titleMediaView = new TextView(getContext());
        ImageView posterMediaView = new ImageView(getContext());
        int amountMediaInGridView = 20;

        for(int i = 0; i < amountMediaInGridView; i++){
            Media currentMedia = mediaList.get(i);

            titleMediaView = (TextView) getMediaTitleView(i)[0];
            posterMediaView = (ImageView) getMediaTitleView(i)[1];
            setTitleMediaView(titleMediaView, currentMedia);
            setPosterMovie(posterMediaView, currentMedia);

            if(i == 15){
                System.out.println("itme 15 " + currentMedia.getTitle());
            }
        }
    }

    public View[] getMediaTitleView(int i){
        System.out.println("valor de i: " + i);
        switch(i){
            case 0:
                System.out.println("entrou no case 0");
                return new View[] {view.findViewById(R.id.media_title_1), view.findViewById(R.id.image_media_poster_1)};

            case 1:
                System.out.println("entrou no case 1");
                return new View[] {view.findViewById(R.id.media_title_2), view.findViewById(R.id.image_media_poster_2)};

            case 2:
                System.out.println("entrou no case 2");
                return new View[] {view.findViewById(R.id.media_title_3), view.findViewById(R.id.image_media_poster_3)};

            case 3:
                return new View[] {view.findViewById(R.id.media_title_4), view.findViewById(R.id.image_media_poster_4)};

            case 4:
                return new View[] {view.findViewById(R.id.media_title_5), view.findViewById(R.id.image_media_poster_5)};

            case 5:
                return new View[] {view.findViewById(R.id.media_title_6), view.findViewById(R.id.image_media_poster_6)};

            case 6:
                return new View[] {view.findViewById(R.id.media_title_7), view.findViewById(R.id.image_media_poster_7)};

            case 7:
                return new View[] {view.findViewById(R.id.media_title_8), view.findViewById(R.id.image_media_poster_8)};

            case 8:
                return new View[] {view.findViewById(R.id.media_title_9), view.findViewById(R.id.image_media_poster_9)};

            case 9:
                return new View[] {view.findViewById(R.id.media_title_10), view.findViewById(R.id.image_media_poster_10)};

            case 10:
                return new View[] {view.findViewById(R.id.media_title_11), view.findViewById(R.id.image_media_poster_11)};

            case 11:
                return new View[] {view.findViewById(R.id.media_title_12), view.findViewById(R.id.image_media_poster_12)};

            case 12:
                return new View[] {view.findViewById(R.id.media_title_13), view.findViewById(R.id.image_media_poster_13)};

            case 13:
                return new View[] {view.findViewById(R.id.media_title_14), view.findViewById(R.id.image_media_poster_14)};

            case 14:
                return new View[] {view.findViewById(R.id.media_title_15), view.findViewById(R.id.image_media_poster_15)};

            case 15:
                return new View[] {view.findViewById(R.id.media_title_16), view.findViewById(R.id.image_media_poster_16)};

            case 16:
                return new View[] {view.findViewById(R.id.media_title_17), view.findViewById(R.id.image_media_poster_17)};

            case 17:
                return new View[] {view.findViewById(R.id.media_title_18), view.findViewById(R.id.image_media_poster_18)};

            case 18:
                return new View[] {view.findViewById(R.id.media_title_19), view.findViewById(R.id.image_media_poster_19)};

            case 19:
                System.out.println("entrou no case 19");
                return new View[] {view.findViewById(R.id.media_title_20), view.findViewById(R.id.image_media_poster_20)};
        }
        return null;
    }

    public void setPosterMovie(ImageView posterMediaView, Media media){
        String posterPath = ((Movie) media).getPosterPath();
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                .into(posterMediaView);
    }

    public void setTitleMediaView(TextView titleMediaView, Media media){
        String mediaTitle = "";

        if(Util.isItMovie(media)){
            mediaTitle = ((Movie) media).getTitle();
        }
        else if(Util.isItShow(media)){
            mediaTitle = ((Show) media).getName();
        }

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
                                if(response.isSuccessful()){
                                    // Limpando o recycler view
//                                    mediaAdapter.setMediaList(new ArrayList<Media>());
                                    moviesList.removeAllViews();

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