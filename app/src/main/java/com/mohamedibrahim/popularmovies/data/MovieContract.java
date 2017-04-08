package com.mohamedibrahim.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mohamed Ibrahim on 2/24/2017.
 **/

public class MovieContract {

    public static final String AUTHORITY = "com.mohamedibrahim.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favoriteMovies";
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI_FAVORITE =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        public static final String TABLE_FAV_MOVIES = "fav_movies";
        public static final String TABLE_MOVIES = "movies";
        public static final String KEY_MOVIE_ID = "movie_id";
        public static final String KEY_ORIGINAL_TITLE = "original_title";
        public static final String KEY_DATE = "date";
        public static final String KEY_VOTE = "vote";
        public static final String KEY_OVERVIEW = "overview";
        public static final String KEY_POSTER_PATH = "poster_path";
        public static final String KEY_ADULT = "adult";
        public static final String KEY_BACK_PATH = "back_drop_path";
        public static final String KEY_ORIGINAL_LANGUAGE = "original_language";
        public static final String KEY_POPULARITY = "popularity";
        public static final String KEY_VIDEO = "video";
        public static final String KEY_VOTE_COUNT = "vote_count";
        public static final String KEY_TITLE = "title";
    }
}