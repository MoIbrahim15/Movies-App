package com.mohamedibrahim.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohamedibrahim.popularmovies.data.MovieContract.MovieEntry;


/**
 * Created by Mohamed Ibrahim on 9/15/2016.
 **/
@SuppressWarnings("all")
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "moviesManager";

    @SuppressWarnings("all")
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + MovieEntry.TABLE_MOVIES + "("
                + MovieEntry._ID + " INTEGER PRIMARY KEY, " + MovieEntry.KEY_MOVIE_ID + " INTEGER, "
                + MovieEntry.KEY_ORIGINAL_TITLE + " TEXT, " + MovieEntry.KEY_DATE + " TEXT, "
                + MovieEntry.KEY_VOTE + " TEXT, " + MovieEntry.KEY_OVERVIEW + " TEXT , "
                + MovieEntry.KEY_POSTER_PATH + " TEXT, " + MovieEntry.KEY_ADULT + " INTEGER, "
                + MovieEntry.KEY_BACK_PATH + " TEXT, " + MovieEntry.KEY_ORIGINAL_LANGUAGE + " TEXT, "
                + MovieEntry.KEY_POPULARITY + " REAL, " + MovieEntry.KEY_VIDEO + " INTEGER, "
                + MovieEntry.KEY_VOTE_COUNT + " INTEGER, " + MovieEntry.KEY_TITLE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_MOVIES);
        onCreate(db);
    }
}