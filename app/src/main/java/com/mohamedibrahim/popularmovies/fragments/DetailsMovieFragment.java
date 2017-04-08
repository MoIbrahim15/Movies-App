package com.mohamedibrahim.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.adapters.DetailsAdapter;
import com.mohamedibrahim.popularmovies.models.Movie;
import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;
import com.mohamedibrahim.popularmovies.utils.DBUtils;
import com.mohamedibrahim.popularmovies.utils.JsonUtils;
import com.mohamedibrahim.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsMovieFragment extends Fragment /*implements*/ /*TrailerReviewsListener,*/ {

    private Movie selectedMovie;
    private ArrayList<Object> movieDetailList = new ArrayList<>();

    @BindView(R.id.detailes_fragment_listview)
    ListView mListView;

    private DetailsAdapter detailsAdapter;

    private static final String MOVIE_DATA = "MOVIE_DATA";
    private static final String SELECTED_MOVIE = "SELECTED_MOVIE";
    private static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w185";
    private static final String FROM_TEN = "/10";

    private static final int TRAILER_LOADER_ID = 2;
    private static final int REVIEWS_LOADER_ID = 3;
    private static final String TRAILER_URL_EXTRA = "TRAILER_URL";
    private static final String REVIEW_URL_EXTRA = "REVIEW_URL";


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

    @SuppressWarnings("all")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_details_movie, container, false);

        ButterKnife.bind(this, rootView);
        /**
         * movie detail as a header in listview
         */
        View headerView = inflater.inflate(
                R.layout.details_header, null);
        mListView.addHeaderView(headerView);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_MOVIE)) {
            selectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE);
        }

        fillHeader(new HeaderViewHolder(headerView));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedMovie != null && detailsAdapter == null) {

            Bundle queryBundle = new Bundle();
            URL trailerURL = NetworkUtils.buildUrl(selectedMovie.getId().toString(), getString(R.string.videos_param));
            URL reviewURL = NetworkUtils.buildUrl(selectedMovie.getId().toString(), getString(R.string.reviews_param));

            queryBundle.putString(TRAILER_URL_EXTRA, String.valueOf(trailerURL));
            queryBundle.putString(REVIEW_URL_EXTRA, String.valueOf(reviewURL));

            LoaderManager loaderManager = getActivity().getSupportLoaderManager();

            Loader<String> trailersLoader = loaderManager.getLoader(TRAILER_LOADER_ID);
            if (trailersLoader == null) {
                loaderManager.initLoader(TRAILER_LOADER_ID, queryBundle, trailerLoaderCallbacks);
            } else {
                loaderManager.restartLoader(TRAILER_LOADER_ID, queryBundle, trailerLoaderCallbacks);
            }

            Loader<String> reviewsLoader = loaderManager.getLoader(REVIEWS_LOADER_ID);
            if (reviewsLoader == null) {
                loaderManager.initLoader(REVIEWS_LOADER_ID, queryBundle, reviewsLoaderCallbacks);
            } else {
                loaderManager.restartLoader(REVIEWS_LOADER_ID, queryBundle, reviewsLoaderCallbacks);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (selectedMovie != null) {
            outState.putParcelable(SELECTED_MOVIE, selectedMovie);
        }
        super.onSaveInstanceState(outState);
    }

    private void fillHeader(final HeaderViewHolder holder) {

        if (selectedMovie != null) {
            String POSTER_PATH = selectedMovie.getPosterPath();
            String FullPosterPath = BASE_POSTER_PATH + SIZE + POSTER_PATH;

            Picasso.with(getContext()).load(FullPosterPath).into(holder.posterView);
            holder.titleView.setText(selectedMovie.getOriginalTitle());
            holder.date_view.setText(selectedMovie.getReleaseDate());
            String Vote = selectedMovie.getVoteAverage().toString() + FROM_TEN;
            holder.rateView.setText(Vote);
            holder.descView.setText(selectedMovie.getOverview());


            if (DBUtils.isMovieFavorite(selectedMovie.getId(), getContext())) {
                holder.favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite);
            } else {
                holder.favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite_outline);
            }

            holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DBUtils.isMovieFavorite(selectedMovie.getId(), getContext())) {
                        DBUtils.deleteFavoriteMovie(selectedMovie.getId(), getContext());
                        holder.favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite_outline);
                    } else {
                        DBUtils.addFavoriteMovie(selectedMovie, getContext());
                        holder.favoriteBtn.setBackgroundResource(R.drawable.ic_action_favorite);
                    }
                }
            });
        }
    }


    private void onFinishTrailers(ArrayList<Trailer> trailersArrayList) {
        movieDetailList.addAll(trailersArrayList);
        detailsAdapter = new DetailsAdapter(getContext(), movieDetailList);
        mListView.setAdapter(detailsAdapter);
    }

    private void onFinishReviews(ArrayList<Review> reviewsArrayList) {
        movieDetailList.addAll(reviewsArrayList);
        detailsAdapter = new DetailsAdapter(getContext(), movieDetailList);
        mListView.setAdapter(detailsAdapter);
    }


    public class HeaderViewHolder {
        @BindView(R.id.favorite_btn)
        ToggleButton favoriteBtn;
        @BindView(R.id.title_label)
        TextView titleView;
        @BindView(R.id.date_label)
        TextView date_view;
        @BindView(R.id.rate_label)
        TextView rateView;
        @BindView(R.id.desc_label)
        TextView descView;
        @BindView(R.id.poster_img_detailed)
        ImageView posterView;

        public HeaderViewHolder(View headerView) {
            ButterKnife.bind(this, headerView);
        }
    }

    /**
     * -------------------------------trailerLoaderCallbacks------------------------------------
     **/

    private LoaderCallbacks<String> trailerLoaderCallbacks = new LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getActivity()) {
                String mJsonResult;

                @Override
                protected void onStartLoading() {
                    if (args == null) {
                        return;
                    }
                    if (mJsonResult != null) {
                        deliverResult(mJsonResult);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public String loadInBackground() {
                    try {
                        String urlString = args.getString(TRAILER_URL_EXTRA);
                        if (urlString == null || TextUtils.isEmpty(urlString)) {
                            return null;
                        } else {
                            URL url = new URL(urlString);
                            return NetworkUtils.getResponseFromHttpUrl(url);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(String jsonResult) {
                    mJsonResult = jsonResult;
                    super.deliverResult(mJsonResult);
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (null != data) {
                onFinishTrailers(JsonUtils.getTrailersDataFromJson(data));
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
        /*
         * We aren't using this method in application, but i required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
        }
    };

    /**
     * -------------------------------reviewsLoaderCallbacks------------------------------------
     **/
    private LoaderCallbacks<String> reviewsLoaderCallbacks = new LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getActivity()) {
                String mJsonResult;

                @Override
                protected void onStartLoading() {
                    if (args == null) {
                        return;
                    }
                    if (mJsonResult != null) {
                        deliverResult(mJsonResult);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public String loadInBackground() {
                    try {
                        String urlString = args.getString(REVIEW_URL_EXTRA);
                        if (urlString == null || TextUtils.isEmpty(urlString)) {
                            return null;
                        } else {
                            URL url = new URL(urlString);
                            return NetworkUtils.getResponseFromHttpUrl(url);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(String jsonResult) {
                    mJsonResult = jsonResult;
                    super.deliverResult(mJsonResult);
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (null != data) {
                onFinishReviews(JsonUtils.getReviewsDataFromJson(data));
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
        /*
         * We aren't using this method in application, but i required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
        }
    };
}
