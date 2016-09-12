package com.mohamedibrahim.popularmovies.managers.interfaces;

import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 7/29/2016.
 */
public interface MoviesListener {
    void onFinishMovies(ArrayList<Movie> movies);
}
