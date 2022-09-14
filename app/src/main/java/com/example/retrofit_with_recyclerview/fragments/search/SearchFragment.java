package com.example.retrofit_with_recyclerview.fragments.search;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.retrofit_with_recyclerview.adapters.MediaAdapter;
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
        String mediaTitle = null;
        String posterPath;
        TextView textView;
        ImageView imageView;

        for(Media media: mediaList){
            View item = LayoutInflater.from(getContext()).inflate(
                    R.layout.vh_media_adapter, (ViewGroup) this.view, false);
            textView = item.findViewById(R.id.media_title);
            imageView = (ImageView) item.findViewById(R.id.image_media_poster);
            posterPath = ((Movie) media).getPosterPath();

            if(Util.isItMovie(media)){
                mediaTitle = ((Movie) media).getTitle();
            }
            else if(Util.isItShow(media)){
                mediaTitle = ((Show) media).getName();
            }

            textView.setText(mediaTitle);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                    .into(imageView);

            this.moviesList.addView(item);
        }
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