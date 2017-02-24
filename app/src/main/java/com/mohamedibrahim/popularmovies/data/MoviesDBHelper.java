package com.mohamedibrahim.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohamedibrahim.popularmovies.data.MovieContract.MovieEntry;
import com.mohamedibrahim.popularmovies.models.Movie;

import java.util.ArrayList;


/**
 * Created by Mohamed Ibrahim on 9/15/2016.
 **/
public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moviesManager";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + MovieEntry.TABLE_MOVIES + "("
                + MovieEntry.KEY_PRIMARY_ID + " INTEGER PRIMARY KEY," + MovieEntry.KEY_MOVIE_ID + " INTEGER,"
                + MovieEntry.KEY_ORIGINAL_TITLE + " TEXT," + MovieEntry.KEY_DATE + " TEXT,"
                + MovieEntry.KEY_VOTE + " TEXT," + MovieEntry.KEY_OVERVIEW + " TEXT,"
                + MovieEntry.KEY_POSTER_PATH + " TEXT," + MovieEntry.KEY_ADULT + " INTEGER,"
                + MovieEntry.KEY_BACK_PATH + " TEXT," + MovieEntry.KEY_ORIGINAL_LANGUAGE + " TEXT,"
                + MovieEntry.KEY_POPULARITY + " REAL," + MovieEntry.KEY_VIDEO + " INTEGER,"
                + MovieEntry.KEY_VOTE_COUNT + " INTEGER," + MovieEntry.KEY_TITLE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_MOVIES);

        // Create tables again
        onCreate(db);
    }


    // Adding new movie
    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

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

        // Inserting Row
        db.insert(MovieEntry.TABLE_MOVIES, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single movie
    public void deleteMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MovieEntry.TABLE_MOVIES, MovieEntry.KEY_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
        db.close();
    }

    public boolean ifMovieFavorite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MovieEntry.TABLE_MOVIES, null, MovieEntry.KEY_MOVIE_ID + "=?",
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
        String selectQuery = "SELECT  * FROM " + MovieEntry.TABLE_MOVIES;

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