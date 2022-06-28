package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MoviesAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciando o adapter
        moviesAdapter = new MoviesAdapter();

        //Configurando o recyclerView
        recyclerView = findViewById(R.id.recycler_movies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(moviesAdapter);
    }
}