package com.example.retrofit_with_recyclerview.util;

import android.content.Context;
import android.widget.TextView;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.models.Media;
import com.example.retrofit_with_recyclerview.models.Movie;
import com.example.retrofit_with_recyclerview.models.Show;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {
    public TextView textView;
    private int xAxisValue;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        this.xAxisValue = 0;
        this.textView = findViewById(R.id.marker_view);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String title;
        String revenue;
        this.xAxisValue = (int) e.getX();

        title = ((Movie) e.getData()).getTitle();
        revenue = "U$ " + ((Movie) e.getData()).getRevenue() + ".";

        textView.setText(title + " - " + revenue);
        super.refreshContent(e, highlight); // Faz o marker ajustar-se ao tamanho do texto
    }

    /**
     * Retorna um MMPointF que posicionar o marker.
     * O primeiro parâmetro desse objeto posiciona o marker horizontalmente e o segundo posiciona
     * verticalmetne.
     *
     * -(getWidth() / 2), no primeiro parâmetro, centraliza o marker horizontalmente sobre a barra.
     * -getHeight(), posiciona o marker sobre a barra.
     *
     * Se a barra selecionada, for alguma da primeira (índice 0) à sétima (índice 6), centralizo o
     * marker horizontalmente sobre a barra.
     * Se for da sétima em diante, alinho o marker à esquerda da barra.
     */
    @Override
    public MPPointF getOffset() {
        if (this.xAxisValue < 7)
            return new MPPointF(-(getWidth() / 2), -getHeight());

        return new MPPointF(-getWidth(), -getHeight());
    }
}
