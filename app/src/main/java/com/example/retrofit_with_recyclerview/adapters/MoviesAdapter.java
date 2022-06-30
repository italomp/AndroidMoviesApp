package com.example.retrofit_with_recyclerview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    List<Movie> movieList;

    public MoviesAdapter() {
        //instanciando só pra não ter que fazer a  verificação de null.
        this.movieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_adapter, parent, false);
        return new MovieViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String movieTittle = this.movieList.get(position).getMovieTittle();
        String posterPath = this.movieList.get(position).getPosterPath();
        holder.movieTittle.setText(movieTittle);
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                .into(holder.imagePosterMovie);
    }

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTittle;
        ImageView imagePosterMovie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.movieTittle = itemView.findViewById(R.id.movie_tittle);
            this.imagePosterMovie = itemView.findViewById(R.id.image_movie_poster);
        }
    }

    public void setMovieList(@NonNull List<Movie> movieList){
        this.movieList = movieList;
        /**
         * O android possui algum observer monitorando esse adapter.
         * Eu não sei qual é, mas sei que o método abaixo notifica todos os observers
         * que monitoram esse adpter sobre uma mudança em seus dados
         */
        notifyDataSetChanged();
    }
}
