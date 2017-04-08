package com.mohamedibrahim.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mohamedibrahim.popularmovies.data.MovieContract;
import com.mohamedibrahim.popularmovies.data.MovieContract.MovieEntry;
import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 2/25/2017.
 **/

public class DBUtils {


    public static ArrayList<Movie> getFavoriteMovies(Context context) {

        ArrayList<Movie> items = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI_FAVORITE,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(1)));
                movie.setOriginalTitle(cursor.getString(2));
                movie.setReleaseDate(cursor.getString(3));
                movie.setVoteAverage(cursor.getDouble(4));
                movie.setOverview(cursor.getString(5));
                movie.setPosterPath(cursor.getString(6));
                movie.setAdult(cursor.getInt(7) == 1);
                movie.setBackdropPath(cursor.getString(8));
                movie.setOriginalLanguage(cursor.getString(9));
                movie.setPopularity(cursor.getDouble(10));
                movie.setVideo(cursor.getInt(11) == 1);
                movie.setVoteCount(cursor.getInt(12));
                movie.setTitle(cursor.getString(13));
                items.add(movie);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return items;
    }

    public static void addFavoriteMovie(Movie movie, Context context) {

        ContentValues values = new ContentValues();
        values.put(MovieEntry.KEY_MOVIE_ID, movie.getId());
        values.put(MovieEntry.KEY_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.KEY_DATE, movie.getReleaseDate());
        values.put(MovieEntry.KEY_VOTE, movie.getVoteAverage());
        values.put(MovieEntry.KEY_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.KEY_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.KEY_ADULT, movie.getAdult() ? 1 : 0);
        values.put(MovieEntry.KEY_BACK_PATH, movie.getBackdropPath());
        values.put(MovieEntry.KEY_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(MovieEntry.KEY_POPULARITY, movie.getPopularity());
        values.put(MovieEntry.KEY_VIDEO, movie.getVideo() ? 1 : 0);
        values.put(MovieEntry.KEY_VOTE_COUNT, movie.getVoteCount());
        values.put(MovieEntry.KEY_TITLE, movie.getTitle());

        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI_FAVORITE,
                values);
    }


    // Deleting single movie
    public static void deleteFavoriteMovie(int id, Context context) {
        context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI_FAVORITE,
                MovieEntry.KEY_MOVIE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public static boolean isMovieFavorite(int id, Context context) {

        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI_FAVORITE,
                null,
                MovieEntry.KEY_MOVIE_ID + "=?",
                new String[]{String.valueOf(id)},
                null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getCount();
            cursor.close();
        }
        return count > 0;
    }


    public static ArrayList<Movie> getCachedMovies(Context context) {

        ArrayList<Movie> items = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(1)));
                movie.setOriginalTitle(cursor.getString(2));
                movie.setReleaseDate(cursor.getString(3));
                movie.setVoteAverage(cursor.getDouble(4));
                movie.setOverview(cursor.getString(5));
                movie.setPosterPath(cursor.getString(6));
                movie.setAdult(cursor.getInt(7) == 1);
                movie.setBackdropPath(cursor.getString(8));
                movie.setOriginalLanguage(cursor.getString(9));
                movie.setPopularity(cursor.getDouble(10));
                movie.setVideo(cursor.getInt(11) == 1);
                movie.setVoteCount(cursor.getInt(12));
                movie.setTitle(cursor.getString(13));
                items.add(movie);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return items;
    }

    // Deleting single movie
    public static void updateCachedMovies(ArrayList<Movie> movies, Context context) {

        context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

        ContentValues[] valuesArray = new ContentValues[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(MovieEntry.KEY_MOVIE_ID, movies.get(i).getId());
            values.put(MovieEntry.KEY_ORIGINAL_TITLE, movies.get(i).getOriginalTitle());
            values.put(MovieEntry.KEY_DATE, movies.get(i).getReleaseDate());
            values.put(MovieEntry.KEY_VOTE, movies.get(i).getVoteAverage());
            values.put(MovieEntry.KEY_OVERVIEW, movies.get(i).getOverview());
            values.put(MovieEntry.KEY_POSTER_PATH, movies.get(i).getPosterPath());
            values.put(MovieEntry.KEY_ADULT, movies.get(i).getAdult() ? 1 : 0);
            values.put(MovieEntry.KEY_BACK_PATH, movies.get(i).getBackdropPath());
            values.put(MovieEntry.KEY_ORIGINAL_LANGUAGE, movies.get(i).getOriginalLanguage());
            values.put(MovieEntry.KEY_POPULARITY, movies.get(i).getPopularity());
            values.put(MovieEntry.KEY_VIDEO, movies.get(i).getVideo() ? 1 : 0);
            values.put(MovieEntry.KEY_VOTE_COUNT, movies.get(i).getVoteCount());
            values.put(MovieEntry.KEY_TITLE, movies.get(i).getTitle());
            valuesArray[i] = values;
        }
        context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, valuesArray);
    }
}
