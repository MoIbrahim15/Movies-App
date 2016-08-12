package com.mohamedibrahim.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.models.Movie;


public class DetailedMovieFragment extends Fragment {
    private Movie selectedMovie;

    public DetailedMovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final String MoveExtra = "MovieExtra";
            Bundle data = getActivity().getIntent().getExtras();
            selectedMovie = data.getParcelable(MoveExtra);
            Log.v("movieDesc", selectedMovie.getOverview());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_detailed_movie, container, false);
        return mView;
    }
}
