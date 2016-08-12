package com.mohamedibrahim.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.adapters.MoviesAdapter;
import com.mohamedibrahim.popularmovies.managers.MoviesManager;
import com.mohamedibrahim.popularmovies.managers.interfaces.AsyncListener;
import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

public class MoviesFragment extends Fragment implements AsyncListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        MoviesManager moviesManager = new MoviesManager(this);
        moviesManager.execute();
    }

    @Override
    public void FinishAsync(ArrayList<Movie> movies) {
        mAdapter = new MoviesAdapter(getContext(), movies);
        mRecyclerView.setAdapter(mAdapter);
    }
}
