package com.mohamedibrahim.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.adapters.DetailsAdapter;
import com.mohamedibrahim.popularmovies.data.MoviesDBHelper;
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
    private ArrayList<Object> movieDetailList = new ArrayList<>();
    private ListView mListView;
    private DetailsAdapter detailsAdapter;

    private ToggleButton favoriteBtn;
    private MoviesDBHelper moviesDBHelper;

    static final String MOVIE_DATA = "MOVIE_DATA";
    static final String SELECTED_MOVIE = "SELECTED_MOVIE";

    static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
    static final String SIZE = "w185";
    static final String FROM_TEN = "/10";


    public DetailsMovieFragment() {
        // Required empty public constructor
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
        moviesDBHelper = new MoviesDBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_details_movie, container, false);

        /**
         * movie detail as a header in listview
         */
        View headerView = inflater.inflate(
                R.layout.details_header, null);

        mListView = (ListView) rootView.findViewById(R.id.detailes_fragment_listview);
        mListView.addHeaderView(headerView);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_MOVIE)) {
            selectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE);
        }

        fillingDetailsHeaderData(headerView);

        return rootView;
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
    public void onSaveInstanceState(Bundle outState) {
        if (selectedMovie != null) {
            outState.putParcelable(SELECTED_MOVIE, selectedMovie);
        }
        super.onSaveInstanceState(outState);
    }

    private void fillingDetailsHeaderData(View headerView) {
        TextView titleView = (TextView) headerView.findViewById(R.id.title_label);
        TextView date_view = (TextView) headerView.findViewById(R.id.date_label);
        TextView rateView = (TextView) headerView.findViewById(R.id.rate_label);
        TextView descView = (TextView) headerView.findViewById(R.id.desc_label);
        ImageView posterView = (ImageView) headerView.findViewById(R.id.poster_img_detailed);
        favoriteBtn = (ToggleButton) headerView.findViewById(R.id.favorite_btn);

        if (selectedMovie != null) {
            String POSTER_PATH = selectedMovie.getPosterPath();
            String FullPosterPath = BASE_POSTER_PATH + SIZE + POSTER_PATH;

            Picasso.with(getContext()).load(FullPosterPath).into(posterView);
            titleView.setText(selectedMovie.getOriginalTitle());
            date_view.setText(selectedMovie.getReleaseDate());
            rateView.setText(selectedMovie.getVoteAverage().toString() + FROM_TEN);
            descView.setText(selectedMovie.getOverview());


            if (moviesDBHelper.ifMovieFavorite(selectedMovie.getId())) {
                favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite);
            } else {
                favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite_outline);
            }

            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moviesDBHelper.ifMovieFavorite(selectedMovie.getId())) {
                        moviesDBHelper.deleteMovie(selectedMovie);
                        favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite_outline);
                    } else {
                        moviesDBHelper.addMovie(selectedMovie);
                        favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite);
                    }
                }
            });
        }
    }


    @Override
    public void onFinishTrailers(ArrayList<Trailer> trailersArrayList) {
        movieDetailList.addAll(trailersArrayList);
    }

    @Override
    public void onFinishReviews(ArrayList<Review> reviewsArrayList) {
        movieDetailList.addAll(reviewsArrayList);
        detailsAdapter = new DetailsAdapter(getContext(), movieDetailList);
        mListView.setAdapter(detailsAdapter);
    }
}
