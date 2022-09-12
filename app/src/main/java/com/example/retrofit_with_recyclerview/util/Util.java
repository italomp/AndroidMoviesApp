package com.example.retrofit_with_recyclerview.util;

import android.view.View;

import com.example.retrofit_with_recyclerview.models.Media;

public class Util {

    public static boolean isItMovie(Media media){
        return Constants.MOVIE_TYPE.equals(media.getSubType()) ? true : false;
    }

    public static boolean isItShow(Media media){
        return Constants.SHOW_TYPE.equals(media.getSubType()) ? true : false;
    }

    public static boolean isItPerson(Media media){
        return Constants.PERSON_TYPE.equals(media.getSubType()) ? true : false;
    }

    public static void showProgressBarAndHiddenView(View progressBar, View[] views){
        progressBar.setVisibility(View.VISIBLE);
        for(int i = 0; i < views.length; i++){
            views[i].setVisibility(View.INVISIBLE);
        }
    }

    public static void hiddenProgressBarAndShowView(View progressBar, View[] views){
        progressBar.setVisibility(View.INVISIBLE);
        for(int i = 0; i < views.length; i++){
            views[i].setVisibility(View.VISIBLE);
        }
    }
}
