package com.mohamedibrahim.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.SettingsActivity;
import com.mohamedibrahim.popularmovies.Utility;
import com.mohamedibrahim.popularmovies.adapters.MoviesAdapter;
import com.mohamedibrahim.popularmovies.data.MoviesDBHelper;
import com.mohamedibrahim.popularmovies.managers.MoviesManager;
import com.mohamedibrahim.popularmovies.managers.interfaces.ClickListener;
import com.mohamedibrahim.popularmovies.managers.interfaces.MoviesListener;
import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment implements MoviesListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String SELECTED_KEY = "selected_position";
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

    @Override
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
                getResources().getInteger(R.integer.movies_columns)));
    }

    private void updateMovies() {
        String sortedBy = Utility.getPreferredMovies(getContext());
        if (sortedBy.equalsIgnoreCase(getString(R.string.pref_sort_favorite))) {
            MoviesDBHelper moviesDBHelper = new MoviesDBHelper(getContext());
            ArrayList<Movie> movies = moviesDBHelper.getAllMovies();
            this.onFinishMovies(movies);
        } else {
            MoviesManager moviesManager = new MoviesManager(sortedBy, this);
            moviesManager.execute();
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
}
