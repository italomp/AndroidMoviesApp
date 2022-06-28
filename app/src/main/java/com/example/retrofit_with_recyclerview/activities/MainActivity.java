package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Movie;
import android.os.Bundle;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MoviesAdapter;
import com.example.retrofit_with_recyclerview.response.MovieListMapper;
import com.example.retrofit_with_recyclerview.response.MovieMapper;
import com.example.retrofit_with_recyclerview.services.ApiService;

import java.util.List;

import okhttp3.ResponseBody;
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
        System.out.println("um passo antes do callback de sucesso");
        ApiService.getInstance().getMovies(this.apkiKey).enqueue(new Callback<MovieListMapper>() {
            @Override
            /**
             * Callback para caso de sucesso.
             * Obtenho a lista de movieMapper, passo-a para o adapter e passo o adapter para o
             * recyclerview.
             *
             * Para realizar os passos acima, preciso instanciar o adapter e o recyclerview aqui,
             * além de realizar algumas configurações no recyclerview.
             */
            public void onResponse(Call<MovieListMapper> call, Response<MovieListMapper> response) {
                if(response.isSuccessful()){
                    System.out.println("entrou no callback de sucesso");
                    //Obtendo Filmes
                    List<MovieMapper> movieMapperList = response.body().getMovies();

                    //Instanciando o adapter
                    moviesAdapter = new MoviesAdapter(movieMapperList);

                    // Configurando o recyclerview
                    recyclerView = findViewById(R.id.recycler_movies);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(moviesAdapter);
                }
            }

            @Override
            public void onFailure(Call<MovieListMapper> call, Throwable t) {

            }
        });

    }
}