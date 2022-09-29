package com.example.retrofit_with_recyclerview.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.adapters.CrewListItemViewPagerAdapter;
import com.example.retrofit_with_recyclerview.models.Crew;
import com.example.retrofit_with_recyclerview.models.Employee;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.responses.CrewResponse;
import com.example.retrofit_with_recyclerview.responses.MediaDetailsResponse;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.CrewMapper;
import com.example.retrofit_with_recyclerview.util.Util;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaDetailsActivity extends AppCompatActivity {
    private Media media;
    ShapeableImageView posterView;
    TextView titleView;
    TextView noteAverageView;
    TextView overviewView;
    LinearLayout layoutCrewList;
    ProgressBar loadScreen;
    ScrollView mediaDetailsScroll;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        getViewsReferences();

        this.hiddenContent();
        this.showProgressBar();

        receivingMedia();
        getMediaDetails();
    }

    public void receivingMedia(){
        Bundle received_data = getIntent().getExtras();
        this.media = (Media) received_data.getSerializable("media");
    }

    public void getViewsReferences(){
        this.posterView = findViewById(R.id.details_media_poster);
        this.titleView = findViewById(R.id.details_media_title);
        this.noteAverageView = findViewById(R.id.vote_average);
        this.overviewView = findViewById(R.id.details_media_overview);
        this.loadScreen = findViewById(R.id.load_screen);
        this.mediaDetailsScroll = findViewById(R.id.media_details_scroll);
        this.layoutCrewList = findViewById(R.id.crew_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                                hiddenProgressBar();
                                showMessageError("HTTP status code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaDetailsResponse> call, Throwable t) {
                            hiddenProgressBar();
                            showMessageError("Falha ao carreegar tela de detalhes");
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
                                hiddenProgressBar();
                                showMessageError("HTTP status code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaDetailsResponse> call, Throwable t) {
                            hiddenProgressBar();
                            showMessageError("Falha ao carreegar página de detalhes");
                            throw new RuntimeException(t.getMessage());
                        }
                    });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCrew(Media media) {
        if(Util.isItMovie(this.media)){
            ApiService.getMovieService()
                    .getCreditsByMovie(media.getId(), Constants.API_KEY)
                    .enqueue(new Callback<CrewResponse>() {
                        @Override
                        public void onResponse(Call<CrewResponse> call, Response<CrewResponse> response) {
                            // HTTP status code de 200 a 299
                            if (response.isSuccessful()){
                                Crew crew = CrewMapper.mapperCrewResponseToCrew(response.body());
                                setCrewList(crew);
                            }

                            // HTTP status code diferente de 200 a 299
                            else showMessageError("HTTP status code: " + response.code());

                            // Desbloqueando a tela principal
                            hiddenProgressBar();
                            showContent();
                        }

                        @Override
                        public void onFailure(Call<CrewResponse> call, Throwable t) {
                            hiddenProgressBar();
                            showMessageError("Falha ao carreegar visualização de integrantes da equipe.");
                            throw new RuntimeException(t.getMessage());
                        }
                    });
        }
        else if(Util.isItShow(this.media)){
            ApiService.getShowService()
                    .getCreditsByShow(media.getId(), Constants.API_KEY)
                    .enqueue(new Callback<CrewResponse>() {
                        @Override
                        public void onResponse(Call<CrewResponse> call, Response<CrewResponse> response) {
                            if(response.isSuccessful()){
                                Crew crew = CrewMapper.mapperCrewResponseToCrew(response.body());
                                setCrewList(crew);
                            }

                            else showMessageError("HTTP status code: " + response.code());

                            hiddenProgressBar();
                            showContent();
                        }

                        @Override
                        public void onFailure(Call<CrewResponse> call, Throwable t) {
                            hiddenProgressBar();
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

    public void setNoteAverageView(int average) {
        this.noteAverageView.setText("Avaliação do usuário: " + String.valueOf(average) + "%");
    }

    public void setOverviewView(String overviewView) {
        this.overviewView.setText(overviewView);
    }

    public void setDetailViews(Response<MediaDetailsResponse> response){
        String mediaTitle = Util.isItMovie(media) ? response.body().getTitle() : response.body().getName();
        String mediaOverview = response.body().getOverview();
        String postPath = response.body().getPostPath();
        Integer noteAverage = response.body().getVoteAverage();

        setTitleView(mediaTitle);
        setNoteAverageView(noteAverage);
        setOverviewView(mediaOverview);
        setPosterView(postPath);
    }

    public void showMessageError(String msg){
        Toast.makeText(MediaDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCrewList(Crew crew){
        List<String> departments = crew.getAllDepartments();
        LinearLayout layoutCrewListView = findViewById(R.id.crew_list);

        for(String department: departments){
            RelativeLayout departmentSectionView = (RelativeLayout) LayoutInflater
                    .from(getApplicationContext())
                    .inflate(R.layout.department_crew_section, layoutCrewListView, false);
            TextView departmentNameView = departmentSectionView.findViewById(R.id.department_name);
            ViewPager2 viewPager = departmentSectionView.findViewById(R.id.department_view_pager);

            List<Employee> employeeListFromCurrentDepartment = crew.getEmployeesByDepartment(department);
            CrewListItemViewPagerAdapter adapter = new CrewListItemViewPagerAdapter(employeeListFromCurrentDepartment);

            departmentNameView.setText(department);
            viewPager.setAdapter(adapter);

            viewPager.setOffscreenPageLimit(3);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });

            viewPager.setPageTransformer(compositePageTransformer);
            layoutCrewListView.addView(departmentSectionView);
        }
    }

    private void showProgressBar(){
        this.loadScreen.setVisibility(View.VISIBLE);
    }

    private void hiddenContent(){
        this.mediaDetailsScroll.setVisibility(View.INVISIBLE);
    }

    private void hiddenProgressBar(){
        this.loadScreen.setVisibility(View.INVISIBLE);
    }

    private void showContent(){
        this.mediaDetailsScroll.setVisibility(View.VISIBLE);
    }
}