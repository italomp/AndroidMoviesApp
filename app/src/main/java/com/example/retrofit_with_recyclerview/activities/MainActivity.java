package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MediaAdapter;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.util.MediaMapper;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Essa classe representa aactivity principal.
 *
 * recyclerView é o item de listagem dos movies.
 *
 * moviesAdapter é o adaptador do recyclerView.
 *
 * inputSearch e searchButton são elementos que compõem a ferramenta de pesquisa
 * de filmese shows com base num input do usuário.
 *
 * thereIsNotResultFoundInSearch é uma variável de controle para saber se houve algum resultado
 * na busca por filmes através de um input do usuário. O result ou uma mensagem de not found serão
 * exibidos condicionalmente com base nessa variável.
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MediaAdapter mediaAdapter;
    TextInputEditText inputSearch;
    Button searchButton;
    boolean thereIsNotResultFoundInSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciando variável de controle.
        this.thereIsNotResultFoundInSearch = false;

        // iniciando views
        this.setSearchViews();
        this.setRecyclerView();

        this.getMovies();
    }

    public void setRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        this.mediaAdapter = new MediaAdapter(MainActivity.this);

        this.recyclerView = findViewById(R.id.recycler_movies);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.mediaAdapter);
    }

    public void getMovies(){
        ApiService.getMovieService().getMovies(Constants.apiKey).enqueue(new Callback<MediaResponseList>() {
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
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void setSearchViews(){
        this.inputSearch = findViewById(R.id.searchInput);
        this.searchButton = findViewById(R.id.searchButton);

        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputSearch.getText().toString();

                // Pesquisando Filmes, Shows e Pessoas
                ApiService.getMediaService()
                        .multiSearch(Constants.apiKey, inputValue)
                        .enqueue(new Callback<MediaResponseList>() {
                            @Override
                            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                                if(response.isSuccessful()){
                                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);
                                    renderingMediasOrNotFoundMessage(mediaList);
                                }
                                else{
                                    Toast.makeText(
                                            getApplicationContext(),
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
                    getApplicationContext(),
                    "Nenhuma mídia foi encontrada.",
                    Toast.LENGTH_LONG).show();
    }


}