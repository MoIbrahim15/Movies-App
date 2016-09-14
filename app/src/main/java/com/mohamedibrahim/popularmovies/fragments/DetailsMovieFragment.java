package com.mohamedibrahim.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.adapters.DetailsAdapter;
import com.mohamedibrahim.popularmovies.managers.ReviewsManager;
import com.mohamedibrahim.popularmovies.managers.TrailersManager;
import com.mohamedibrahim.popularmovies.managers.interfaces.TrailerReviewsListener;
import com.mohamedibrahim.popularmovies.models.Movie;
import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailsMovieFragment extends Fragment implements TrailerReviewsListener {

    private Movie selectedMovie;
    private ArrayList<Object> movieDetailesList = new ArrayList<>();

    static final String MOVIE_DATA = "MOVIE_DATA";

    static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
    static final String SIZE = "w185";
    static final String FROMTEN = "/10";

    private ListView mListView;
    DetailsAdapter detailsAdapter;

    public DetailsMovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                selectedMovie = arguments.getParcelable(MOVIE_DATA);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedMovie != null && detailsAdapter == null) {
            TrailersManager trailersManager = new TrailersManager(getContext(), selectedMovie.getId().toString(), this);
            trailersManager.execute();

            ReviewsManager reviewsManager = new ReviewsManager(getContext(), selectedMovie.getId().toString(), this);
            reviewsManager.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_details_movie, container, false);

        View headerView = inflater.inflate(
                R.layout.details_header, null);

        mListView = (ListView) rootView.findViewById(R.id.detailes_fragment_listview);
        mListView.addHeaderView(headerView);

        fillingDetailsHeaderData(headerView);

        return rootView;
    }

    private void fillingDetailsHeaderData(View headerView) {
        TextView titleView = (TextView) headerView.findViewById(R.id.title_label);
        TextView date_view = (TextView) headerView.findViewById(R.id.date_label);
        TextView rateView = (TextView) headerView.findViewById(R.id.rate_label);
        TextView descView = (TextView) headerView.findViewById(R.id.desc_label);
        ImageView posterView = (ImageView) headerView.findViewById(R.id.poster_img_detailed);

        if (selectedMovie != null) {
            String POSTER_PATH = selectedMovie.getPosterPath();
            String FullPosterPath = BASE_POSTER_PATH + SIZE + POSTER_PATH;

            Picasso.with(getContext()).load(FullPosterPath).into(posterView);
            titleView.setText(selectedMovie.getOriginalTitle());
            date_view.setText(selectedMovie.getReleaseDate());
            rateView.setText(selectedMovie.getVoteAverage().toString() + FROMTEN);
            descView.setText(selectedMovie.getOverview());
        }
    }


    @Override
    public void onFinishTrailers(ArrayList<Trailer> trailersArrayList) {
        movieDetailesList.addAll(trailersArrayList);
    }

    @Override
    public void onFinishReviews(ArrayList<Review> reviewsArrayList) {
        movieDetailesList.addAll(reviewsArrayList);
        for (int i = 0; i < movieDetailesList.size(); i++) {
            if (movieDetailesList.get(i) instanceof Trailer) {
                Log.v("Trailer", ((Trailer) movieDetailesList.get(i)).getKey());
            }
            if (movieDetailesList.get(i) instanceof Review) {
                Log.v("Review", ((Review) movieDetailesList.get(i)).getContent());
            }
        }

        detailsAdapter = new DetailsAdapter(getContext(), movieDetailesList);
        mListView.setAdapter(detailsAdapter);
    }
}
