package com.example.retrofit_with_recyclerview.util;

import android.app.Activity;

import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

public class MyWindowMetrics {
    Activity activity;

    public MyWindowMetrics(Activity activity){
        this.activity = activity;
    }

    public WindowSizeClass getWidthSizeClass(){
        WindowMetrics windowMetrics = WindowMetricsCalculator.getOrCreate()
                .computeCurrentWindowMetrics(activity);
        float density = activity.getResources().getDisplayMetrics().density;
        float widthDp = windowMetrics.getBounds().width() / density;
        WindowSizeClass widthWindowSizeClass;

        if(widthDp < 600f)
            widthWindowSizeClass = WindowSizeClass.COMPACT;
        else if(widthDp < 840f)
            widthWindowSizeClass = WindowSizeClass.MEDIUM;
        else
            widthWindowSizeClass = WindowSizeClass.EXPANDED;

        return widthWindowSizeClass;
    }

    public WindowSizeClass getHeightSizeClass(){
        WindowMetrics windowMetrics = WindowMetricsCalculator.getOrCreate()
                .computeCurrentWindowMetrics(activity);
        float density = activity.getResources().getDisplayMetrics().density;
        float heightDp = windowMetrics.getBounds().height() / density;
        WindowSizeClass heightWindowSizeClass;

        if(heightDp < 480f)
            heightWindowSizeClass = WindowSizeClass.COMPACT;
        else if(heightDp < 900)
            heightWindowSizeClass = WindowSizeClass.MEDIUM;
        else
            heightWindowSizeClass = WindowSizeClass.EXPANDED;

        return heightWindowSizeClass;
    }

    public enum WindowSizeClass { COMPACT, MEDIUM, EXPANDED }
}
