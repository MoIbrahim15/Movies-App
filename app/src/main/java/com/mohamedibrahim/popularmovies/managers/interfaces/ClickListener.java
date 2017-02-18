package com.mohamedibrahim.popularmovies.managers.interfaces;

import com.mohamedibrahim.popularmovies.models.Movie;

/**
 * Created by Mohamed Ibrahim on 9/12/2016.
 **/
public interface ClickListener {
    void onItemSelected(Movie movie, int position);
}
