package com.mohamedibrahim.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.SettingsActivity;
import com.mohamedibrahim.popularmovies.adapters.MoviesAdapter;
import com.mohamedibrahim.popularmovies.data.MoviesDBHelper;
import com.mohamedibrahim.popularmovies.interfaces.ClickListener;
import com.mohamedibrahim.popularmovies.models.Movie;
import com.mohamedibrahim.popularmovies.utils.JsonUtils;
import com.mohamedibrahim.popularmovies.utils.NetworkUtils;
import com.mohamedibrahim.popularmovies.utils.Utility;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<String> {

    private static final String SELECTED_KEY = "selected_position";
    private static final String URL_EXTRA = "URL";
    private static final int LOADER_ID = 1;
    private int mPosition = RecyclerView.NO_POSITION;
    private boolean isTwoPane;

    @BindView(R.id.movies_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_movies_label)
    TextView mNoMoviesNoConnectionView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, mView);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        initRecyclerView();

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utility.isOnline(getContext())) {
            updateMovies();
        } else {
            setError(R.string.no_connection);
        }
    }

    public void onFinishMovies(ArrayList<Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            MoviesAdapter mAdapter = new MoviesAdapter(getContext(), movies);
            mRecyclerView.setAdapter(mAdapter);
            mNoMoviesNoConnectionView.setVisibility(View.GONE);
            if (mPosition != RecyclerView.NO_POSITION) {
                mRecyclerView.scrollToPosition(mPosition);
            } else {
                //first start && two pane
                if (isTwoPane) {
                    openFirstMovie(movies);
                }
            }
        } else {
            setError(R.string.no_movies);
        }
        stopRefreshing();
    }

    private void initRecyclerView() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorGray));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                Utility.calculateNoOfColumns(getContext())));
    }

    private void updateMovies() {
        String sortedBy = Utility.getPreferredMovies(getContext());
        if (sortedBy.equalsIgnoreCase(getString(R.string.pref_sort_favorite))) {
            MoviesDBHelper moviesDBHelper = new MoviesDBHelper(getContext());
            ArrayList<Movie> movies = moviesDBHelper.getAllMovies();
            this.onFinishMovies(movies);
        } else {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(URL_EXTRA, String.valueOf(NetworkUtils.buildUrl(sortedBy)));
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            Loader<String> moviesLoader = loaderManager.getLoader(LOADER_ID);
            if (moviesLoader == null) {
                loaderManager.initLoader(LOADER_ID, queryBundle, this);
            } else {
                loaderManager.restartLoader(LOADER_ID, queryBundle, this);
            }
        }
    }

    private void openFirstMovie(ArrayList<Movie> movies) {
        final int FIRST_ARRAYLIST_POSITION = 0;
        ((ClickListener) getActivity())
                .onItemSelected(movies.get(FIRST_ARRAYLIST_POSITION),
                        FIRST_ARRAYLIST_POSITION);
    }

    public void setItemPosition(int position) {
        this.mPosition = position;
    }

    public void setIsTwoPane(boolean isTwoPane) {
        this.isTwoPane = isTwoPane;
    }

    public void onSortedByChanged() {
        mPosition = RecyclerView.NO_POSITION;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent settingIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        onStart();
    }

    public void setError(int errorResID) {
        mRecyclerView.setAdapter(null);
        mNoMoviesNoConnectionView.setText(getString(errorResID));
        mNoMoviesNoConnectionView.setVisibility(View.VISIBLE);
        stopRefreshing();
    }

    public void stopRefreshing() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

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
                    String urlString = args.getString(URL_EXTRA);
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
        if (null == data) {
            setError(R.string.no_connection);
        } else {
            onFinishMovies(JsonUtils.getMoviesDataFromJson(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        /*
         * We aren't using this method in application, but i required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

}
