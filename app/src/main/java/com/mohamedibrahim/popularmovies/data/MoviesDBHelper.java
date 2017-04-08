package com.mohamedibrahim.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamed Ibrahim
 * on 4/8/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static MoviesDBHelper sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moviesManager";

    public static synchronized MoviesDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MoviesDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_MOVIES + "("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " + MovieContract.MovieEntry.KEY_MOVIE_ID + " INTEGER, "
                + MovieContract.MovieEntry.KEY_ORIGINAL_TITLE + " TEXT, " + MovieContract.MovieEntry.KEY_DATE + " TEXT, "
                + MovieContract.MovieEntry.KEY_VOTE + " TEXT, " + MovieContract.MovieEntry.KEY_OVERVIEW + " TEXT , "
                + MovieContract.MovieEntry.KEY_POSTER_PATH + " TEXT, " + MovieContract.MovieEntry.KEY_ADULT + " INTEGER, "
                + MovieContract.MovieEntry.KEY_BACK_PATH + " TEXT, " + MovieContract.MovieEntry.KEY_ORIGINAL_LANGUAGE + " TEXT, "
                + MovieContract.MovieEntry.KEY_POPULARITY + " REAL, " + MovieContract.MovieEntry.KEY_VIDEO + " INTEGER, "
                + MovieContract.MovieEntry.KEY_VOTE_COUNT + " INTEGER, " + MovieContract.MovieEntry.KEY_TITLE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
//    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
//            + MovieEntry.TABLE_FAV_MOVIES + " ADD COLUMN " + MovieEntry.COLUMN_... + " string;";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_MOVIES);
//        onCreate(db);
        //        TODO when updatind DB must make ulter query for updating table instead of drop table and recreeate it again:)
        if (oldVersion < 1) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
        }
    }
}