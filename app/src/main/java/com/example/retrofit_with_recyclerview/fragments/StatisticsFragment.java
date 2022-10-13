package com.example.retrofit_with_recyclerview.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.responses.MediaDetailsResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponse;
import com.example.retrofit_with_recyclerview.responses.MediaResponseList;
import com.example.retrofit_with_recyclerview.services.ApiService;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.example.retrofit_with_recyclerview.util.CustomMarkerView;
import com.example.retrofit_with_recyclerview.util.MediaMapper;
import com.example.retrofit_with_recyclerview.util.MyWindowMetrics;
import com.example.retrofit_with_recyclerview.util.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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
    private Observable topTenRevenue;
    private final String SORT_BY_REVENUE = "revenue.desc";
    private Context context;
    private ProgressBar progressBar;
    private Spinner spinnerYear;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private BarChart barChart;
    private int[] chartColorArray = new int[10];
    private CustomMarkerView customMarkerView;
    private TableLayout chartLegends;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setViewsAndVariables(inflater, container);
        this.setSpinnerYear(view);

        int year = Integer.parseInt(spinnerYear.getSelectedItem().toString());

        Util.showProgressBarAndHiddenView(this.progressBar, new View[]{this.barChart, this.chartLegends});
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
                            getMoviesRevenue(mediaResponseList, topTen);
                        }
                        else{
                            showMessageError("Status code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MediaResponseList> call, Throwable t) {
                        showMessageError("Falha ao pesquisar filmes do ano: " + year);
                        t.printStackTrace();
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
    public void getMoviesRevenue(List<MediaResponse> mediaResponseList, TopTen topTen){
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
                            t.printStackTrace();
                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Observable observable, Object obj1){
        if(observable instanceof TopTen){
            List<Movie> topTenMovieList = (List<Movie>) obj1;
            List<BarEntry> entries = new ArrayList<>();

            setEntriesToBarChar(topTenMovieList, entries);
            setBarChar(entries);
            Util.hiddenProgressBarAndShowView(progressBar, new View[] {barChart, chartLegends});
        }
    }

    public void setSpinnerYear(View view){
        spinnerYear = view.findViewById(R.id.spinner_year);
        spinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.select_year, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(spinnerAdapter);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Util.showProgressBarAndHiddenView(progressBar, new View[]{barChart, chartLegends});

                // limpar a listagem anterior
                ((TopTen) topTenRevenue).setTopTen(new ArrayList<>());

                int year = Integer.parseInt(spinnerYear.getSelectedItem().toString());
                getMoviesByYear(year, SORT_BY_REVENUE, (TopTen) topTenRevenue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setViewsAndVariables(LayoutInflater inflater, ViewGroup container){
        MyWindowMetrics myWindowMetrics = new MyWindowMetrics(getActivity());
        MyWindowMetrics.WindowSizeClass widthWindowSizeClass = myWindowMetrics.getWidthSizeClass();
        MyWindowMetrics.WindowSizeClass heightWindowSizeClass = myWindowMetrics.getHeightSizeClass();

        // Phone port
        if(widthWindowSizeClass == MyWindowMetrics.WindowSizeClass.COMPACT)
            view = inflater.inflate(R.layout.fragment_statistics, container, false);
        // Phone land
        else if(heightWindowSizeClass == MyWindowMetrics.WindowSizeClass.COMPACT)
            view = inflater.inflate(R.layout.fragment_statistics_phone_land, container, false);
        // Tablet port
        else if(widthWindowSizeClass == MyWindowMetrics.WindowSizeClass.MEDIUM)
            view = inflater.inflate(R.layout.fragment_statistics_tablet_port, container, false);
        // Tablet land
        else if(widthWindowSizeClass == MyWindowMetrics.WindowSizeClass.EXPANDED &&
                heightWindowSizeClass == MyWindowMetrics.WindowSizeClass.MEDIUM)
            view = inflater.inflate(R.layout.fragment_statistics_tablet_land, container, false);

        this.context = view.getContext();
        this.barChart = view.findViewById(R.id.bar_chart);
        this.topTenRevenue = new TopTen(SORT_BY_REVENUE);
        this.topTenRevenue.addObserver(this);
        this.progressBar = view.findViewById(R.id.progress_bar_statistics_fragment);
        this.customMarkerView = new CustomMarkerView(getContext(), R.layout.marker_view);
        this.chartLegends = view.findViewById(R.id.chart_legends);
    }

    public void setEntriesToBarChar(List<Movie> topTenMovieList, List<BarEntry> entries){
        for(int i = 0; i < topTenMovieList.size(); i++){
            Movie mv = topTenMovieList.get(i);
            long barValue = mv.getRevenue();
            entries.add(new BarEntry(i, barValue, mv));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setBarChar(List<BarEntry> entries){
        BarDataSet dataSet = new BarDataSet(entries, "");
        BarData data = new BarData(dataSet);

        barChart.getDescription().setEnabled(false);  // Removendo descrição
        barChart.getAxisRight().setEnabled(false); // Removendo valor à direita do  eixo Y
        barChart.getXAxis().setDrawLabels(false);  // Removendo valor do eixo X
        barChart.setFitBars(true); // Pondo as barras centralizadas aos pontos de X

        // Formatando valores à esquerda do eixo Y.
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setValueFormatter(new LargeValueFormatter());

        this.launchChartColorArray();
        dataSet.setColors(this.chartColorArray, this.context); // Adicionando cor às barras
        data.setDrawValues(false); // Removendo exibição de valores das barras
        data.setBarWidth(0.8f); // Espaço entre as barras

        this.addEventClickListenerOnTheChart();
        this.setChartLegends(barChart); // Configurando legendas do gráfico

        barChart.setData(data);
        barChart.invalidate(); // Fazer refresh
        barChart.animateY(500); //Adicionando animação vertical às barras do gráfico
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setChartLegends(BarChart barChart){
        this.setLegendIconColors();

        List<Movie> movieList = ((TopTen) this.topTenRevenue).getTopTenRevenueDesc();

        TextView txtRowOneColOne = this.view.findViewById(R.id.txt_row_1_col_1);
        txtRowOneColOne.setText(movieList.get(0).getTitle());

        TextView txtRowOneColTwo = this.view.findViewById(R.id.txt_row_1_col_2);
        txtRowOneColTwo.setText(movieList.get(1).getTitle());

        TextView txtTwoOneColOne = this.view.findViewById(R.id.txt_row_2_col_1);
        txtTwoOneColOne.setText(movieList.get(2).getTitle());

        TextView txtTwoOneColTwo = this.view.findViewById(R.id.txt_row_2_col_2);
        txtTwoOneColTwo.setText(movieList.get(3).getTitle());

        TextView txtRowThreeColOne = this.view.findViewById(R.id.txt_row_3_col_1);
        txtRowThreeColOne.setText(movieList.get(4).getTitle());

        TextView txtRowThreeColTwo = this.view.findViewById(R.id.txt_row_3_col_2);
        txtRowThreeColTwo.setText(movieList.get(5).getTitle());

        TextView txtRowFourColOne = this.view.findViewById(R.id.txt_row_4_col_1);
        txtRowFourColOne.setText(movieList.get(6).getTitle());

        TextView txtRowFourColTwo = this.view.findViewById(R.id.txt_row_4_col_2);
        txtRowFourColTwo.setText(movieList.get(7).getTitle());

        TextView txtRowFiveColOne = this.view.findViewById(R.id.txt_row_5_col_1);
        txtRowFiveColOne.setText(movieList.get(8).getTitle());

        TextView txtRowFiveColTwo = this.view.findViewById(R.id.txt_row_5_col_2);
        txtRowFiveColTwo.setText(movieList.get(9).getTitle());
    }

    public void launchChartColorArray(){
        chartColorArray[0] = R.color.bar_color_1;
        chartColorArray[1] = R.color.bar_color_2;
        chartColorArray[2] = R.color.bar_color_3;
        chartColorArray[3] = R.color.bar_color_4;
        chartColorArray[4] = R.color.bar_color_5;
        chartColorArray[5] = R.color.bar_color_6;
        chartColorArray[6] = R.color.bar_color_7;
        chartColorArray[7] = R.color.bar_color_8;
        chartColorArray[8] = R.color.bar_color_9;
        chartColorArray[9] = R.color.bar_color_10;
    }

    public void setLegendIconColors(){
        int BAR_COLOR_1 = 0xFFF21F26;
        int BAR_COLOR_2 = 0xFFF3D915;
        int BAR_COLOR_3 = 0xFFBA5252;
        int BAR_COLOR_5 = 0xFFD4D9A1;
        int BAR_COLOR_4 = 0xFFC09491;
        int BAR_COLOR_6 = 0xFFF8EDD1;
        int BAR_COLOR_7 = 0xFFD88A8A;
        int BAR_COLOR_8 = 0xFF474843;
        int BAR_COLOR_9 = 0xFF9D9D93;
        int BAR_COLOR_10 = 0xFFC5CFC6;

        ImageView imgRowOneColOne = this.view.findViewById(R.id.img_row_1_col_1);
        imgRowOneColOne.setColorFilter(BAR_COLOR_1);

        ImageView imgRowOneColTwo = this.view.findViewById(R.id.img_row_1_col_2);
        imgRowOneColTwo.setColorFilter(BAR_COLOR_2);

        ImageView imgRowTwoColOne = this.view.findViewById(R.id.img_row_2_col_1);
        imgRowTwoColOne.setColorFilter(BAR_COLOR_3);

        ImageView imgRowTwoColTwo = this.view.findViewById(R.id.img_row_2_col_2);
        imgRowTwoColTwo.setColorFilter(BAR_COLOR_4);

        ImageView imgRowThreeColOne = this.view.findViewById(R.id.img_row_3_col_1);
        imgRowThreeColOne.setColorFilter(BAR_COLOR_5);

        ImageView imgRowThreeColTwo = this.view.findViewById(R.id.img_row_3_col_2);
        imgRowThreeColTwo.setColorFilter(BAR_COLOR_6);

        ImageView imgRowFourColOne = this.view.findViewById(R.id.img_row_4_col_1);
        imgRowFourColOne.setColorFilter(BAR_COLOR_7);

        ImageView imgRowFourColTwo = this.view.findViewById(R.id.img_row_4_col_2);
        imgRowFourColTwo.setColorFilter(BAR_COLOR_8);

        ImageView imgRowFiveColOne = this.view.findViewById(R.id.img_row_5_col_1);
        imgRowFiveColOne.setColorFilter(BAR_COLOR_9);

        ImageView imgRowFiveColTwo = this.view.findViewById(R.id.img_row_5_col_2);
        imgRowFiveColTwo.setColorFilter(BAR_COLOR_10);
    }

    public void addEventClickListenerOnTheChart(){
            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                barChart.setMarker(customMarkerView);
            }

            @Override
            public void onNothingSelected() {
            }
        });
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

            if(this.topTen.size() == 10){
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

        public void setTopTen(List<Movie> topTen) {
            this.topTen = topTen;
        }
    }
}