package com.mohamedibrahim.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mohamedibrahim.popularmovies.fragments.DetailedMovieFragment;
import com.mohamedibrahim.popularmovies.fragments.MoviesFragment;
import com.mohamedibrahim.popularmovies.managers.ReviewsManager;
import com.mohamedibrahim.popularmovies.managers.TrailersManager;
import com.mohamedibrahim.popularmovies.managers.interfaces.ClickListener;
import com.mohamedibrahim.popularmovies.managers.interfaces.TrailerReviewsListener;
import com.mohamedibrahim.popularmovies.models.Movie;
import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ClickListener, TrailerReviewsListener {

    static final String MOVIE_DATA = "MOVIE_DATA";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private ArrayList<Trailer> trailersArrayList = new ArrayList<>();
    private ArrayList<Review> reviewsArrayList = new ArrayList<>();
    private boolean mTwoPane;
    private MoviesFragment moviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movies_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new DetailedMovieFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        moviesFragment = ((MoviesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movies));
        moviesFragment.setIsTwoPane(mTwoPane);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortedBy = Utility.getPreferredMovies(this);
        if (sortedBy.equals(getString(R.string.pref_sort_popular))) {
            getSupportActionBar().setTitle(R.string.pref_sort_popular_label);
        } else if (sortedBy.equals(getString(R.string.pref_sort_top))) {
            getSupportActionBar().setTitle(R.string.pref_sort_top_label);
        } else if (sortedBy.equals(getString(R.string.pref_sort_favorite))) {
            getSupportActionBar().setTitle(R.string.pref_sort_favorite_label);
        }
    }

    @Override
    public void onItemSelected(Movie movie, int position) {
        TrailersManager trailersManager = new TrailersManager(this, movie.getId().toString(), this);
        trailersManager.execute();

        ReviewsManager reviewsManager = new ReviewsManager(this, movie.getId().toString(), this);
        reviewsManager.execute();

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MOVIE_DATA, movie);
            DetailedMovieFragment fragment = new DetailedMovieFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent detailedIntent = new Intent(this, DetaileActivity.class);
            detailedIntent.putExtra(MOVIE_DATA, movie);
            startActivity(detailedIntent);
        }
        moviesFragment.setItemPosition(position);
    }

    @Override
    public void onFinishTrailers(ArrayList<Trailer> trailersArrayList) {
        this.trailersArrayList = trailersArrayList;
        for (int i = 0; i < trailersArrayList.size(); i++) {
            Log.v("keeey", trailersArrayList.get(i).getKey());
        }
    }

    @Override
    public void onFinishReviews(ArrayList<Review> reviewsArrayList) {
        this.reviewsArrayList = reviewsArrayList;
        for (int i = 0; i < reviewsArrayList.size(); i++) {
            Log.v("author", reviewsArrayList.get(i).getAuthor());
        }
    }
}
