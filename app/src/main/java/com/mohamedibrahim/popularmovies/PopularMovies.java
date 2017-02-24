package com.mohamedibrahim.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Mohamed Ibrahim on 2/24/2017.
 **/


public class PopularMovies extends Application {
    @Override
    public void onCreate() {
        Stetho.initializeWithDefaults(this);
    }

}