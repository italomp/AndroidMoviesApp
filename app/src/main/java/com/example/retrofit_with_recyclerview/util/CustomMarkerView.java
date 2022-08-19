package com.example.retrofit_with_recyclerview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.example.retrofit_with_recyclerview.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class CustomMarkerView extends MarkerView {
    public TextView textView;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        this.textView = findViewById(R.id.marker_view);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //tvContent.setText("" + e.getVal()); // set the entry-value as the display text
    }


    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }


    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }

    public void setTextView(String text) {
        this.textView.setText(text);
    }
}
