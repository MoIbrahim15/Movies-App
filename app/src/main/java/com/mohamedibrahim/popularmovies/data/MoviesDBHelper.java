package com.mohamedibrahim.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 9/15/2016.
 **/
public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moviesManager";
    private static final String TABLE_MOVIES = "movies";
    private static final String KEY_PRIMARY_ID = "primary_id";
    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_DATE = "date";
    private static final String KEY_VOTE = "vote";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_BACK_PATH = "back_drop_path";
    private static final String KEY_ORIGINAL_LANGUAGE = "original_language";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_TITLE = "title";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_PRIMARY_ID + " INTEGER PRIMARY KEY," + KEY_MOVIE_ID + " INTEGER,"
                + KEY_ORIGINAL_TITLE + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_VOTE + " TEXT," + KEY_OVERVIEW + " TEXT,"
                + KEY_POSTER_PATH + " TEXT," + KEY_ADULT + " INTEGER,"
                + KEY_BACK_PATH + " TEXT," + KEY_ORIGINAL_LANGUAGE + " TEXT,"
                + KEY_POPULARITY + " REAL," + KEY_VIDEO + " INTEGER,"
                + KEY_VOTE_COUNT + " INTEGER," + KEY_TITLE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        // Create tables again
        onCreate(db);
    }


    // Adding new movie
    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_ID, movie.getId());
        values.put(KEY_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(KEY_DATE, movie.getReleaseDate());
        values.put(KEY_VOTE, movie.getVoteAverage());
        values.put(KEY_OVERVIEW, movie.getOverview());
        values.put(KEY_POSTER_PATH, movie.getPosterPath());
        values.put(KEY_ADULT, movie.getAdult() ? 1 : 0);
        values.put(KEY_BACK_PATH, movie.getBackdropPath());
        values.put(KEY_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(KEY_POPULARITY, movie.getPopularity());
        values.put(KEY_VIDEO, movie.getVideo() ? 1 : 0);
        values.put(KEY_VOTE_COUNT, movie.getVoteCount());
        values.put(KEY_TITLE, movie.getTitle());

        // Inserting Row
        db.insert(TABLE_MOVIES, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single movie
    public void deleteMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
        db.close();
    }

    public boolean ifMovieFavorite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIES, null, KEY_MOVIE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Getting All Movies
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> MovieDetailList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
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

                // Adding movie to list
                MovieDetailList.add(movie);
            } while (cursor.moveToNext());
        }
        // return contact list
        return MovieDetailList;
    }
}