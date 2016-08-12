package com.mohamedibrahim.popularmovies.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.SettingsActivity;
import com.mohamedibrahim.popularmovies.adapters.MoviesAdapter;
import com.mohamedibrahim.popularmovies.managers.MoviesManager;
import com.mohamedibrahim.popularmovies.managers.interfaces.AsyncListener;
import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

public class MoviesFragment extends Fragment implements AsyncListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private  String sortedBy;
    private SharedPreferences preferences;
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
        switch (item.getItemId()){
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

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.movies_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }


    private void updateMovies(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortedBy = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        MoviesManager moviesManager = new MoviesManager(this,sortedBy);
        moviesManager.execute();
    }

    @Override
    public void FinishAsync(ArrayList<Movie> movies) {
        mAdapter = new MoviesAdapter(getContext(), movies);
        mRecyclerView.setAdapter(mAdapter);
    }
}
