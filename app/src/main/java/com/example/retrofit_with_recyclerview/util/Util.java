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

    public static void showProgressBarAndHiddenView(View progressBar, View view){
        progressBar.setVisibility(View.VISIBLE);
        view.setVisibility(View.INVISIBLE);
    }

    public static void hiddenProgressBarAndShowView(View progressBar, View view){
        progressBar.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
    }
}
