package com.example.retrofit_with_recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.responses.CrewResponse;
import com.example.retrofit_with_recyclerview.responses.MediaDetailsResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.LoadingDialog;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaDetailsActivity extends AppCompatActivity {
    private Media media;
    ImageView posterView;
    TextView titleView;
    TextView noteAverageView;
    TextView overviewView;
    LinearLayout layoutCrewList;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        receivingMovieId();
        launchingTheMovieDetailsViews();
        layoutCrewList = findViewById(R.id.crew_list);
        loadingDialog = new LoadingDialog(MediaDetailsActivity.this);

        Runnable getMoviesRunnable = new Runnable() {
            @Override
            public void run() {
                getMediaDetails();
            }
        };

        Thread backgroundThread = new Thread(getMoviesRunnable);
        backgroundThread.start();
        loadingDialog.startLoadingDialog();
    }

    public void receivingMovieId(){
        Bundle received_data = getIntent().getExtras();
        this.media = (Media) received_data.getSerializable("media");
    }

    public void launchingTheMovieDetailsViews(){
        this.posterView = findViewById(R.id.details_movie_poster);
        this.titleView = findViewById(R.id.details_movie_title);
        this.noteAverageView = findViewById(R.id.vote_avarage);
        this.overviewView = findViewById(R.id.details_movie_sinopse);
    }

    public void getMediaDetails(){
        if(Util.isItMovie(this.media)){
            ApiService.getMovieService()
                    .getMovieDetails(this.media.getId(), Constants.API_KEY)
                    .enqueue(new Callback<MediaDetailsResponse>() {
                        @Override
                        public void onResponse(Call<MediaDetailsResponse> call, Response<MediaDetailsResponse> response) {
                            // Status code de 200 a 299
                            if(response.isSuccessful()){
                                setDetailViews(response);
                                getCrew(media);
                            }
                            // Demais status code
                            else{
                                showMessageError("HTTP status code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaDetailsResponse> call, Throwable t) {
                            showMessageError("Falha ao carreegar página de detalhes");
                            throw new RuntimeException(t.getMessage());
                        }
                    });
        }
        else if (Util.isItShow(this.media)) {
            ApiService.getShowService()
                    .getShowDetails(media.getId(), Constants.API_KEY)
                    .enqueue(new Callback<MediaDetailsResponse>() {
                        @Override
                        public void onResponse(Call<MediaDetailsResponse> call, Response<MediaDetailsResponse> response) {
                            if (response.isSuccessful()) {
                                setDetailViews(response);
                                getCrew(media);
                            }
                            else{
                                showMessageError("HTTP status code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaDetailsResponse> call, Throwable t) {
                            showMessageError("Falha ao carreegar página de detalhes");
                            throw new RuntimeException(t.getMessage());
                        }
                    });
        }

    }

    public void getCrew(Media media) {
        if(Util.isItMovie(this.media)){
            ApiService.getMovieService()
                    .getCreditsByMovie(media.getId(), Constants.API_KEY)
                    .enqueue(new Callback<CrewResponse>() {
                        @Override
                        public void onResponse(Call<CrewResponse> call, Response<CrewResponse> response) {
                            // HTTP status code de 200 a 299
                            if (response.isSuccessful()) setCrewList(response);

                            // HTTP status code diferente de 200 a 299
                            else showMessageError("HTTP status code: " + response.code());

                            // Desbloqueando a tela principal
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<CrewResponse> call, Throwable t) {
                            // Desbloqueando a tela principal
                            loadingDialog.dismiss();

                            showMessageError("Falha ao carreegar visualização de integrantes da equipe.");
                            throw new RuntimeException(t.getMessage());
                        }
                    });
        }
        else if(Util.isItShow(this.media)){
            //REQUISITAR SHOW CREW AQUI
            ApiService.getShowService()
                    .getCreditsByShow(media.getId(), Constants.API_KEY)
                    .enqueue(new Callback<CrewResponse>() {
                        @Override
                        public void onResponse(Call<CrewResponse> call, Response<CrewResponse> response) {
                            if(response.isSuccessful()) setCrewList(response);

                            else showMessageError("HTTP status code: " + response.code());

                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<CrewResponse> call, Throwable t) {
                            loadingDialog.dismiss();

                            showMessageError("Falha ao carreegar visualização de integrantes da equipe.");
                            throw new RuntimeException(t.getMessage());
                        }
                    });
        }

    }

    public void setPosterView(String postPath) {
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w342/" + postPath)
                .into(this.posterView);
    }

    public void setTitleView(String title) {
        this.titleView.setText(title);
    }

    public void setNoteAverageView(float average) {
        this.noteAverageView.setText("Avaliação do usuário: " + String.valueOf(average) + "%");
    }

    public void setOverviewView(String overviewView) {
        this.overviewView.setText(overviewView);
    }

    public void setDetailViews(Response<MediaDetailsResponse> response){
        String mediaTitle = Util.isItMovie(media) ? response.body().getTitle() : response.body().getName();
        String mediaOverview = response.body().getOverview();
        String postPath = response.body().getPostPath();
        Float noteAverage = response.body().getVoteAverage();

        setTitleView(mediaTitle);
        setNoteAverageView(noteAverage);
        setOverviewView(mediaOverview);
        setPosterView(postPath);
    }

    public void showMessageError(String msg){
        Toast.makeText(MediaDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void setCrewList(Response<CrewResponse> response){
        CrewResponse crewResponse = response.body();
        List<String> departments = crewResponse.getAllDepartments();

        LinearLayout layoutCrewList = findViewById(R.id.crew_list);

        for(String department: departments){
            // Obtendo funcionários do departamento
            int maxLength = 4;
            String crewEmployees = crewResponse.getToStringEmployeesByDepartment(department, maxLength);

            // Setando valores do crew_list_item (departamento e funcionários)
            View crewListItem = getLayoutInflater().inflate(R.layout.crew_list_item, null, false);
            TextView departmentView = crewListItem.findViewById(R.id.department);
            TextView employeesView = crewListItem.findViewById(R.id.department_employees);

            // Adicionando o crew_list_item à crew_list
            if(!crewEmployees.equals("")){
                departmentView.setText(department + ": ");
                employeesView.setText(crewEmployees);

                layoutCrewList.addView(crewListItem);
            }
        }
    }
}