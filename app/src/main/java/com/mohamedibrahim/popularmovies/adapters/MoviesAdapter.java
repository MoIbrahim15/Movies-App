package com.mohamedibrahim.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.managers.interfaces.ClickListener;
import com.mohamedibrahim.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 7/31/2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
        final String SIZE = "w185";
        String POSTER_PATH = movies.get(position).getPosterPath();

        String FullPosterPath = BASE_POSTER_PATH + SIZE + POSTER_PATH;
        Picasso.with(context).load(FullPosterPath).into(holder.posterMovie);
        holder.posterMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClickListener) context)
                        .onItemSelected(movies.get(position),position);
            }

        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            posterMovie = (ImageView) itemView.findViewById(R.id.movie_poster_img);
        }
    }
}
