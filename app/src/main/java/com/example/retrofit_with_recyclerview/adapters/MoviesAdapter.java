package com.example.retrofit_with_recyclerview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.response.MovieMapper;

import java.util.List;
import java.util.zip.Inflater;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    List<MovieMapper> movieMapperList;

    public MoviesAdapter(List<MovieMapper> movieMapperList) {
        this.movieMapperList = movieMapperList;
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
        String movieTittle = this.movieMapperList.get(position).getMovieTittle();
        holder.movieTittle.setText(movieTittle);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTittle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.movieTittle = itemView.findViewById(R.id.movie_tittle);
        }
    }
}
