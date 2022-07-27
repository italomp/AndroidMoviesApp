package com.example.retrofit_with_recyclerview.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MediaAdapter;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Person;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.MediaMapper;
import com.example.retrofit_with_recyclerview.util.Util;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    MediaAdapter mediaAdapter;
    TextInputEditText inputSearch;
    Button searchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRecyclerView();
        this.setSearchViews();
        this.getMovies();
    }


    public void setRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        this.mediaAdapter = new MediaAdapter(getContext());
        this.mediaAdapter.setHasStableIds(true);
        this.recyclerView = this.getActivity().findViewById(R.id.recycler_movies);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.mediaAdapter);
    }

    public void getMovies(){
        ApiService.getMovieService().getMovies(Constants.API_KEY).enqueue(new Callback<MediaResponseList>() {
            @Override
            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                // Status code de 200 a 299
                if(response.isSuccessful()){
                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);
                    mediaAdapter.setMediaList(mediaList);
                }
                else{
                    // Poderia tratar alguns casos de erro específicos...
                    showErrorMessage("HTTP Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MediaResponseList> call, Throwable t) {
                showErrorMessage("Falha ao carregar filmes");
                throw new RuntimeException(t.getMessage());
            }
        });
    }

    public void showErrorMessage(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setSearchViews(){
        this.inputSearch = this.getActivity().findViewById(R.id.searchInput);
        this.searchButton = this.getActivity().findViewById(R.id.searchButton);

        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputSearch.getText().toString();

                // Pesquisando Filmes, Shows e Pessoas
                ApiService.getMediaService()
                        .multiSearch(Constants.API_KEY, inputValue)
                        .enqueue(new Callback<MediaResponseList>() {
                            @Override
                            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                                if(response.isSuccessful()){
                                    // Limpando o recycler view
                                    mediaAdapter.setMediaList(new ArrayList<Media>());

                                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);

                                    // Extraindo Movies e Shows de objetos Person
                                    mediaList = parseMedia(mediaList);

                                    renderingMediasOrNotFoundMessage(mediaList);
                                }
                                else{
                                    Toast.makeText(
                                            getContext(),
                                            "HTTP Status Code: " + response.code(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MediaResponseList> call, Throwable t) {

                            }
                        });
            }
        });
    }


    public void renderingMediasOrNotFoundMessage(List<Media> mediaList){
        if (!mediaList.isEmpty()){
            mediaAdapter.setMediaList(mediaList);
        }

        else
            Toast.makeText(
                    getContext(),
                    "Nenhuma mídia foi encontrada.",
                    Toast.LENGTH_LONG).show();
    }

    /**
     * Esse método recebe uma lista de Medias contendo Medias do tipo Person e retorna
     * uma lista de Medias contendo apenas Movies e Shows.
     */
    public List<Media> parseMedia(List<Media> mediaList){
        List<Media> result = new ArrayList<>();
        Set<Media> mediaSet = new HashSet<>();

        for(Media media : mediaList){
            if(Util.isItMovie(media) || Util.isItShow(media)){
                mediaSet.add(media);
            }
            else{
                mediaSet.addAll(((Person) media).getMoviesAndShows());
            }
        }

        result.addAll(mediaSet);
        return mediaList;
    }

}