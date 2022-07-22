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
import com.example.retrofit_with_recyclerview.activities.MediaDetailsActivity;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.models.Show;
import com.example.retrofit_with_recyclerview.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MovieViewHolder> {

    List<Media> mediaList;
    Context context;

    public MediaAdapter(Context context) {
        //instanciando só pra não ter que fazer a  verificação de null.
        this.mediaList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_adapter, parent, false);
        return new MovieViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Media currentMedia = this.mediaList.get(position);
        parseMedia(holder, currentMedia);
    }

    @Override
    public int getItemCount() {
        return this.mediaList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTittle;
        ImageView imagePosterMovie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.movieTittle = itemView.findViewById(R.id.media_tittle);
            this.imagePosterMovie = itemView.findViewById(R.id.image_media_poster);
        }
    }

    public void setMediaList(@NonNull List<Media> mediaList){
        this.mediaList = mediaList;
        /**
         * O android possui algum observer monitorando esse adapter.
         * Eu não sei qual é, mas sei que o método abaixo notifica todos os observers
         * que monitoram esse adpter sobre uma mudança em seus dados
         */
        notifyDataSetChanged();
    }

    public void addClickEventListtenerOnView(View view, Media media){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MediaDetailsActivity.class);
                intent.putExtra("media", media);
                context.startActivity(intent);
            }
        });
    }

    /**
     * Esse método recebe uma Media, verifica se ela é um Movie ou um Show e seta
     * o parâmetro recebido correspondente (Media ou Show).
     */
    public void parseMedia(MovieViewHolder holder, Media media){
        String mediaTitle = null;
        String posterPath = null;

        // Media é Movie
        if(Util.isItMovie(media)){
            mediaTitle = ((Movie) media).getTitle();
            posterPath = ((Movie) media).getPosterPath();

            holder.movieTittle.setText(mediaTitle);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                    .into(holder.imagePosterMovie);

            this.addClickEventListtenerOnView(holder.itemView, media);
        }

        // Media é Show
        else if(Util.isItShow(media)){
            mediaTitle = ((Show) media).getName();
            posterPath = ((Show) media).getPosterPath();

            holder.movieTittle.setText(mediaTitle);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/" + posterPath)
                    .into(holder.imagePosterMovie);

            this.addClickEventListtenerOnView(holder.itemView, media);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

}
