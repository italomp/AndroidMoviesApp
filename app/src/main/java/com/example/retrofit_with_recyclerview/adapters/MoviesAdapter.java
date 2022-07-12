package com.example.retrofit_with_recyclerview.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.activities.MovieDetailsActivity;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    List<Movie> movieList;
    Context context;

    public MoviesAdapter(Context context) {
        //instanciando só pra não ter que fazer a  verificação de null.
        this.movieList = new ArrayList<>();
        this.context = context;
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
        Movie currentMovie = this.movieList.get(position);
        String movieTittle = currentMovie.getTitle();
        String posterPath = currentMovie.getPosterPath();
        long movieId = currentMovie.getId();

        holder.movieTittle.setText(movieTittle);
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                .into(holder.imagePosterMovie);

        this.addClickEventListtenerOnView(holder.itemView, movieId);
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

    public void addClickEventListtenerOnView(View view, long id){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
                intent.putExtra("movieId", id);
                context.startActivity(intent);
            }
        });
    }

}
