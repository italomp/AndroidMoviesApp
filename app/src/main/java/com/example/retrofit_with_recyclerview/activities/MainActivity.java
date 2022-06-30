package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MoviesAdapter;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.util.MovieMapper;
import com.example.retrofit_with_recyclerview.response.MovieResponseList;
import com.example.retrofit_with_recyclerview.response.MovieResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    private final String apkiKey = "246bda00932bacb8f512a68b3af13f71";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRecyclerView();
        this.getMovies();
    }

    public void setRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        this.moviesAdapter = new MoviesAdapter();

        this.recyclerView = findViewById(R.id.recycler_movies);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.moviesAdapter);
    }

    public void getMovies(){
        ApiService.getInstance().getMovies(this.apkiKey).enqueue(new Callback<MovieResponseList>() {
            @Override
            /**
             * Callback para caso de sucesso.
             * Obtenho a lista de movieMapper, passo-a para o adapter e passo o adapter para o
             * recyclerview.
             *
             * Para realizar os passos acima, preciso instanciar o adapter e o recyclerview aqui,
             * além de realizar algumas configurações no recyclerview.
             */
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
}