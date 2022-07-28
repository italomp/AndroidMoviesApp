package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.MediaAdapter;
import com.example.retrofit_with_recyclerview.adapters.ViewPagerAdapter;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Person;
import com.example.retrofit_with_recyclerview.util.MediaMapper;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MediaAdapter mediaAdapter;
    TextInputEditText inputSearch;
    Button searchButton;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        /**
         * Adicionando evento de seleção à TabLayout.
         *
         * Importante:
         *  Se o usuário clicar nas guias, o comportamento funciona perfeitamente.
         *
         *  Se o usuário arrastar a tela para o lado, o conteúdo mudará de um fragmento
         *  para outro. Entretanto,  a estilo da guia não muda para indicar que ela foi
         *  selecionada.
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Se for selecionada
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Informando ao viewPager qual aba foi selecionada
                viewPager.setCurrentItem(tab.getPosition());
            }

            // Se for deselecionada/perder o foco.
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            // Se for selecionada novamente
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /**
         *  Método usado para selecionar o fragmento do tabLayout, cujo seu conteúdo já está
         *  sendo exibido na tela, entretanto o fragmento ainda não foi selecionado/clickado.
         *
         *  Esse tipo de evento que motiva a criação desse método, é causado quando o usuário
         *  arrasta a tela para o lado, ao invés de selecionar uma guia do tabLayout.
         */
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Informando ao tabLayout a página/tela/fragmento selecionada.
                tabLayout.getTabAt(position).select();
            }
        });



        // iniciando views
        // Ao invés de chamar essemétodo, configurarei o fragment
        //this.setSearchViews();
        //this.setRecyclerView();

        //this.getMovies();
    }

    // Deveria ser algo do tipo "configurar Fragment"
    public void setRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        this.mediaAdapter = new MediaAdapter(MainActivity.this);
        this.mediaAdapter.setHasStableIds(true);

        this.recyclerView = findViewById(R.id.recycler_movies);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.mediaAdapter);
    }

    // Deve ser do fragment
    public void showErrorMessage(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    // Deve ser do Fragment
    public void renderingMediasOrNotFoundMessage(List<Media> mediaList){
        if (!mediaList.isEmpty()){
            mediaAdapter.setMediaList(mediaList);
        }

        else
            Toast.makeText(
                    getApplicationContext(),
                    "Nenhuma mídia foi encontrada.",
                    Toast.LENGTH_LONG).show();
    }

    // Posso passar os dados para o fragmento listá-los ou pedir para ele printar o not found
    public void getMovies(){
        ApiService.getMovieService().getMovies(Constants.API_KEY).enqueue(new Callback<MediaResponseList>() {
            @Override
            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                // Status code de 200 a 299
                if(response.isSuccessful()){
                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);
                    mediaAdapter.setMediaList(mediaList);
                }
                else{
                    // Poderia tratar alguns casos de erro específicos...
                    showErrorMessage("HTTP Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MediaResponseList> call, Throwable t) {
                showErrorMessage("Falha ao carregar filmes");
                throw new RuntimeException(t.getMessage());
            }
        });
    }



    // Posso passar os dados para o fragmento listá-los ou pedir para ele printar o not found
    public void setSearchViews(){
        this.inputSearch = findViewById(R.id.searchInput);
        this.searchButton = findViewById(R.id.searchButton);

        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputSearch.getText().toString();

                // Pesquisando Filmes, Shows e Pessoas
                ApiService.getMediaService()
                        .multiSearch(Constants.API_KEY, inputValue)
                        .enqueue(new Callback<MediaResponseList>() {
                            @Override
                            public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                                if(response.isSuccessful()){
                                    // Limpando o recycler view
                                    mediaAdapter.setMediaList(new ArrayList<Media>());

                                    List<MediaResponse> mediaResponseList = response.body().getMediaList();
                                    List<Media> mediaList = MediaMapper.fromMediaResponseToMedia(mediaResponseList);

                                    // Extraindo Movies e Shows de objetos Person
                                    mediaList = parseMedia(mediaList);

                                    renderingMediasOrNotFoundMessage(mediaList);
                                }
                                else{
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "HTTP Status Code: " + response.code(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MediaResponseList> call, Throwable t) {

                            }
                        });
            }
        });
    }


    /**
     * Esse método recebe uma lista de Medias contendo Medias do tipo Person e retorna
     * uma lista de Medias contendo apenas Movies e Shows.
     */
    public List<Media> parseMedia(List<Media> mediaList){
        List<Media> result = new ArrayList<>();
        Set<Media> mediaSet = new HashSet<>();

        for(Media media : mediaList){
            if(Util.isItMovie(media) || Util.isItShow(media)){
                mediaSet.add(media);
            }
            else{
                mediaSet.addAll(((Person) media).getMoviesAndShows());
            }
        }

        result.addAll(mediaSet);
        return mediaList;
    }
}