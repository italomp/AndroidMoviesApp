package com.example.retrofit_with_recyclerview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_with_recyclerview.R;

import java.util.zip.Inflater;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_adapter, parent, false);
        return new MovieViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.movieTittle.setText(position + "º Título Teste");
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
