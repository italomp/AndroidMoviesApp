package com.example.retrofit_with_recyclerview.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.responses.MediaDetailsResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.MediaMapper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticsFragment extends Fragment implements Observer {
    private Observable topTenBudget;
    private Observable topTenRevenue;
    private final String SORT_BY_REVENUE = "revenue.desc";
    private final String SORT_BY_BUDGET = "budget.desc";
    BarChart barChart;
    Context context;

    ToolTipsManager toolTipsManager;
    ToolTip.Builder toolTipBuilder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        this.context = view.getContext();
        this.barChart = view.findViewById(R.id.bar_chart);
        this.topTenBudget = new TopTen(SORT_BY_BUDGET);
        this.topTenRevenue = new TopTen(SORT_BY_REVENUE);
        this.topTenBudget.addObserver(this);
        this.topTenRevenue.addObserver(this);
        this.toolTipsManager = new ToolTipsManager();

        int year = 2020;

        //this.getMoviesByYear(year, this.SORT_BY_BUDGET, (TopTen) this.topTenBudget);
        this.getMoviesByYear(year, this.SORT_BY_REVENUE, (TopTen) this.topTenRevenue);

        return view;
    }

    /**
     * @param year é o ano dos filmes retornados
     * @param sortBy é a concatenação do atributo pelo qual eu quero que a lista esteja
     *              ordenada com o tipo de ordenação (crescente ou decrescente).
     *               ex: revenue.asc ou revenue.desc
     * @param topTen é um observable que encapsula a lista à qual eu quero adicionar os filmes retornados
     */
    public void getMoviesByYear(int year, String sortBy, TopTen topTen){
        ApiService.getMovieService()
                .getMoviesByYear(Constants.API_KEY, year, sortBy)
                .enqueue(new Callback<MediaResponseList>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<MediaResponseList> call, Response<MediaResponseList> response) {
                        if(response.isSuccessful()){
                            List<MediaResponse> mediaResponseList = response.body().getMediaList();
                            getBudgetAndRevenue(mediaResponseList, topTen);
                        }
                        else{
                            showMessageError("Status code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MediaResponseList> call, Throwable t) {

                    }
                });
    }

    public void showMessageError(String msg){
        Toast.makeText(
            getContext(),
            msg,
            Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getBudgetAndRevenue(List<MediaResponse> mediaResponseList, TopTen topTen){
        int totalAmount = 10;

        for(int i = 0; i < mediaResponseList.size(); i++){
            if (i == totalAmount)
                break;

            MediaResponse current = mediaResponseList.get(i);
            ApiService.getMovieService()
                    .getMovieDetails(current.getId(), Constants.API_KEY)
                    .enqueue(new Callback<MediaDetailsResponse>() {
                        @Override
                        public void onResponse(Call<MediaDetailsResponse> call, Response<MediaDetailsResponse> response) {
                            if(response.isSuccessful()){
                                Movie movie = MediaMapper.fromMediaDetailsToMovie(response.body());
                                topTen.addMovie(movie);
                            }
                            else{
                                showMessageError("Http Status code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaDetailsResponse> call, Throwable t) {
                            showMessageError("Falha ao pesquisar detalhes do filme: " + current.getTitle());
                        }
                    });
        }
    }

    // POSTERIORMENTE IREI CHAMAR O MTODO QUE PLOTA OS GRFICOS NESSE METODO
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Observable observable, Object obj1){
        if(observable instanceof TopTen){
            List<Movie> topTenMovieList = (List<Movie>) obj1;

            System.out.println("TOP TEN. Size: " + topTenMovieList.size());
            topTenMovieList.stream().forEach(mv -> System.out.println(mv.getTitle() +
                    " budget: " + mv.getBudget() + " revenue:" + mv.getRevenue()));
            System.out.println("\n");

            List<BarEntry> entries = new ArrayList<>();

            for(int i = 0; i < topTenMovieList.size(); i++){
                Movie mv = topTenMovieList.get(i);
                long barValue = mv.getRevenue();
                entries.add(new BarEntry(i, barValue, mv));
            }

            BarDataSet dataSet = new BarDataSet(entries, "Top 10 - Filmes Mais Lucrativos");
            BarData data = new BarData(dataSet);

            // Adicionando cor às barras
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            // Removendo exibição de valores das barras
            data.setDrawValues(false);

            // Espaço entre as barras
            data.setBarWidth(0.9f);

            barChart.setData(data);

            // Pondo as barras centralizadas aos pontos de X
            barChart.setFitBars(true);

            // Fazer refresh
            barChart.invalidate();

            // Adicionar evento de click às barras
            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String movieTitle = ((Movie) barChart
                            .getDataSetByTouchPoint(e.getX(), e.getY())
                            .getEntryForIndex((int) e.getX()).getData()).getTitle();

                    System.out.println(movieTitle + " - " + e.getY());

                    toolTipBuilder = new ToolTip.Builder(context,
                            barChart,
                            (ViewGroup) barChart.getParent(),
                            movieTitle,
                            ToolTip.POSITION_ABOVE);
                    toolTipBuilder.setBackgroundColor(getResources().getColor(R.color.gray));
                    toolTipsManager.show(toolTipBuilder.build());

                }

                @Override
                public void onNothingSelected() {

                }
            });

        }
    }


    public class TopTen extends Observable {
        private List<Movie> topTen;
        private String orderBy;

        public TopTen(String orderBy){
            this.topTen = new ArrayList<>();
            this.orderBy = orderBy;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void addMovie(Movie movie) {
            this.topTen.add(movie);

            if(this.topTen.size() == 10 && this.orderBy == SORT_BY_REVENUE){
                setChanged();                           // O estado mudou
                notifyObservers(getTopTenRevenueDesc());   // Notificando objetos
            }
            else if(this.topTen.size() == 10 && this.orderBy == SORT_BY_BUDGET){
                setChanged();                           // O estado mudou
                notifyObservers(getTopTenRevenueDesc());   // Notificando objetos
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public List<Movie> getTopTenRevenueDesc(){
            return this.topTen.stream().sorted(new Comparator<Movie>() {
                @Override
                public int compare(Movie mv1, Movie mv2) {
                    if (mv1.getRevenue() < mv2.getRevenue())
                        return 1;
                    else if (mv1.getRevenue() == mv2.getRevenue())
                        return 0;
                    else{
                        return -1;
                    }
                }
            }).collect(Collectors.toList());
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public List<Movie> sortDescByBudget(){
            return this.topTen.stream().sorted(new Comparator<Movie>() {
                @Override
                public int compare(Movie mv1, Movie mv2) {
                    if (mv1.getBudget() < mv2.getBudget())
                        return 1;
                    else if (mv1.getBudget() == mv2.getBudget())
                        return 0;
                    else{
                        return -1;
                    }
                }
            }).collect(Collectors.toList());
        }
    }

}