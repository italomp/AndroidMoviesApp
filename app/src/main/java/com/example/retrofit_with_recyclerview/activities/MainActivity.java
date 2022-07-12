package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MoviesAdapter;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.responses.shows.ShowResponse;
import com.example.retrofit_with_recyclerview.util.MovieMapper;
import com.example.retrofit_with_recyclerview.responses.movies.MovieResponseList;
import com.example.retrofit_with_recyclerview.responses.movies.MovieResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.SecurityConstants;
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
    MoviesAdapter moviesAdapter;
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
        this.moviesAdapter = new MoviesAdapter(MainActivity.this);

        this.recyclerView = findViewById(R.id.recycler_movies);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.moviesAdapter);
    }

    public void getMovies(){
        ApiService.getMovieService().getMovies(SecurityConstants.apiKey).enqueue(new Callback<MovieResponseList>() {
            @Override
            public void onResponse(Call<MovieResponseList> call, Response<MovieResponseList> response) {
                // Status code de 200 a 299
                if(response.isSuccessful()){
                    List<MovieResponse> movieResponseList = response.body().getMovies();
                    List<Movie> movieList = MovieMapper.fromMovieResponseToMovie(movieResponseList);
                    moviesAdapter.setMovieList(movieList);
                }
                else{
                    // Poderia tratar alguns casos de erro específicos...
                    showErrorMessage("HTTP Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MovieResponseList> call, Throwable t) {
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
                // Obter o valor do input
                String searchValue = inputSearch.getText().toString();

                // Pesquisar filmes e shows com base na query.
                // obs: a query pode ser o nome de uma pessoa (ator ou diretor, eu acho)

                // Pesquisando movies
                ApiService.getMovieService()
                        .searchMovies(SecurityConstants.apiKey, searchValue)
                        .enqueue(new Callback<MovieResponseList>() {
                            @Override
                            public void onResponse(Call<MovieResponseList> call, Response<MovieResponseList> response) {
                                // HTTP status code de 200 a 299
                                if(response.isSuccessful()){
                                    List<MovieResponse> result = response.body().getMovies();
                                    List<Movie> movies = MovieMapper.fromMovieResponseToMovie(result);
                                    renderingMoviesOrNotFoundMessage(movies);
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieResponseList> call, Throwable t) {
                                // mostrar alguma mensagem de erro
                            }
                        });

                /*ApiService.getShowService()
                        .searchShows(SecurityConstants.apiKey, searchValue)
                        .enqueue(new Callback<ShowResponse>() {
                            @Override
                            public void onResponse(Call<ShowResponse> call, Response<ShowResponse> response) {
                                if(response.isSuccessful()){

                                }
                            }

                            @Override
                            public void onFailure(Call<ShowResponse> call, Throwable t) {
                                // mostrar alguma mensagem de erro
                            }
                        });*/
            }
        });
    }

    public void renderingMoviesOrNotFoundMessage(List<Movie> movieList){
        if (!movieList.isEmpty()) moviesAdapter.setMovieList(movieList);
        else Toast.makeText(
                MainActivity.this,
                "Nenhum filme foi encontrado, com base nesse input.",
                Toast.LENGTH_LONG).show();
    }
}