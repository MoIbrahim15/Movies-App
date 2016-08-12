package com.mohamedibrahim.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mohamedibrahim.popularmovies.fragments.DetailedMovieFragment;

/**
 * Created by Mohamed Ibrahim on 8/12/2016.
 */
public class DetailedMovieActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailedMovieFragment())
                    .commit();
        }
    }
}
