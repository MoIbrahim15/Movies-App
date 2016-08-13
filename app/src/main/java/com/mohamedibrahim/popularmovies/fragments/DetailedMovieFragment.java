package com.mohamedibrahim.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;


public class DetailedMovieFragment extends Fragment {
    private Movie selectedMovie;

    final static String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
    final static String SIZE = "w185";


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
        TextView titleView = (TextView) mView.findViewById(R.id.title_label);
        TextView date_view = (TextView) mView.findViewById(R.id.date_label);
        TextView rateView = (TextView) mView.findViewById(R.id.rate_label);
        TextView descView = (TextView) mView.findViewById(R.id.desc_label);
        ImageView posterView = (ImageView) mView.findViewById(R.id.poster_img_detailed);

        //fill data
        if (selectedMovie != null) {
            titleView.setText(selectedMovie.getOriginalTitle());
            date_view.setText(selectedMovie.getReleaseDate());
            rateView.setText(selectedMovie.getVoteAverage().toString() + "/10");
            descView.setText(selectedMovie.getOverview());

            String POSTER_PATH = selectedMovie.getPosterPath();
            String FullPosterPath = BASE_POSTER_PATH + SIZE + POSTER_PATH;
            Picasso.with(getContext()).load(FullPosterPath).into(posterView);
        }
        return mView;
    }
}
