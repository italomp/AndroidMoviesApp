package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.responses.MovieDetailsResponse;
import com.example.retrofit_with_recyclerview.responses.MovieResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.SecurityConstants;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resgatando dados passados pelo contexto da MainActivity, através do MoviesAdapter.
        Bundle received_data = getIntent().getExtras();
        this.movieId = received_data.getLong("movieId");

        // Obtendo referencias das views do activity_movie_details.
        ImageView posterView = findViewById(R.id.details_movie_poster);
        TextView titleView = findViewById(R.id.details_movie_title);
        TextView overviewView = findViewById(R.id.details_movie_sinopse);

        // Requisitando dados do filme
        ApiService.getInstance()
                .getMovieDetails(this.movieId, SecurityConstants.apiKey)
                .enqueue(new Callback<MovieDetailsResponse>() {
                    @Override
                    public void onResponse(Call<MovieDetailsResponse> call, Response<MovieDetailsResponse> response) {
                        // Status code de 200 a 299
                        if(response.isSuccessful()){
                            //id, movieTitle, postPath, overview, voteAvarage
                            String movieTitle = response.body().getMovieTittle();
                            String movieOverview = response.body().getOverview();
                            String postPath = response.body().getPostPath();

                            titleView.setText(movieTitle);
                            overviewView.setText(movieOverview);
                            Picasso.get()
                                    .load("https://image.tmdb.org/t/p/w342/" + postPath)
                                    .into(posterView);
                        }
                        // Demais status code
                        else{
                            // tratar aqui...
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetailsResponse> call, Throwable t) {

                    }

                });

    }
}