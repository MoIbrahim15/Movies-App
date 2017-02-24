package com.mohamedibrahim.popularmovies.data;

/**
 * Created by Mohamed Ibrahim on 2/24/2017.
 **/

public class MovieContract {

    public static final class MovieEntry /*implements BaseColumns */ {

        public static final String TABLE_MOVIES = "movies";
        public static final String KEY_PRIMARY_ID = "primary_id";
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