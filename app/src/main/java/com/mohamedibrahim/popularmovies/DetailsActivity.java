package com.mohamedibrahim.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mohamedibrahim.popularmovies.fragments.DetailsMovieFragment;

/**
 * Created by Mohamed Ibrahim on 8/12/2016.
 **/
public class DetailsActivity extends AppCompatActivity {

    static final String MOVIE_DATA = "MOVIE_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MOVIE_DATA,
                    getIntent().getExtras().getParcelable(MOVIE_DATA));
            DetailsMovieFragment fragment = new DetailsMovieFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_detail_container, fragment)
                    .commit();
        }
    }
}