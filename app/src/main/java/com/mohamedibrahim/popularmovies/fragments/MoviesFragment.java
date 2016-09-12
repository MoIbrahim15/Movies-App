package com.mohamedibrahim.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.SettingsActivity;
import com.mohamedibrahim.popularmovies.adapters.MoviesAdapter;
import com.mohamedibrahim.popularmovies.managers.MoviesManager;
import com.mohamedibrahim.popularmovies.managers.interfaces.ClickListener;
import com.mohamedibrahim.popularmovies.managers.interfaces.MoviesListener;
import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

public class MoviesFragment extends Fragment implements MoviesListener {

    private int mPosition = RecyclerView.NO_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences preferences;
    private boolean isTwoPane;
    private String sortedBy;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_movies, container, false);

        mLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.movies_columns));
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.movies_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline()) {
            updateMovies();
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    private void updateMovies() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortedBy = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        MoviesManager moviesManager = new MoviesManager(getContext(), sortedBy, this);
        moviesManager.execute();
    }

    @Override
    public void onFinishMovies(ArrayList<Movie> movies) {

        mAdapter = new MoviesAdapter(getContext(), movies);
        mRecyclerView.setAdapter(mAdapter);

        if (mPosition != RecyclerView.NO_POSITION) {
            mRecyclerView.scrollToPosition(mPosition);
        } else {
            //first start && two pane
            if (getIsTwoPane()) {
                ((ClickListener) getActivity())
                        .onItemSelected(movies.get(0), 0);
            }
        }
    }

    public void setItemPosition(int position) {
        this.mPosition = position;
    }

    public void setIsTwoPane(boolean isTwoPane) {
        this.isTwoPane = isTwoPane;
    }

    public boolean getIsTwoPane() {
        return isTwoPane;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
