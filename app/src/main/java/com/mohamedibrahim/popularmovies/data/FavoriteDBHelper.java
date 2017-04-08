package com.mohamedibrahim.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohamedibrahim.popularmovies.data.MovieContract.MovieEntry;


/**
 * Created by Mohamed Ibrahim on 9/15/2016.
 **/
@SuppressWarnings("all")
public class FavoriteDBHelper extends SQLiteOpenHelper {

    private static FavoriteDBHelper sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favMoviesManager";

    public static synchronized FavoriteDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new FavoriteDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + MovieEntry.TABLE_FAV_MOVIES + "("
                + MovieEntry._ID + " INTEGER PRIMARY KEY, " + MovieEntry.KEY_MOVIE_ID + " INTEGER, "
                + MovieEntry.KEY_ORIGINAL_TITLE + " TEXT, " + MovieEntry.KEY_DATE + " TEXT, "
                + MovieEntry.KEY_VOTE + " TEXT, " + MovieEntry.KEY_OVERVIEW + " TEXT , "
                + MovieEntry.KEY_POSTER_PATH + " TEXT, " + MovieEntry.KEY_ADULT + " INTEGER, "
                + MovieEntry.KEY_BACK_PATH + " TEXT, " + MovieEntry.KEY_ORIGINAL_LANGUAGE + " TEXT, "
                + MovieEntry.KEY_POPULARITY + " REAL, " + MovieEntry.KEY_VIDEO + " INTEGER, "
                + MovieEntry.KEY_VOTE_COUNT + " INTEGER, " + MovieEntry.KEY_TITLE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
//    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
//            + MovieEntry.TABLE_FAV_MOVIES + " ADD COLUMN " + MovieEntry.COLUMN_... + " string;";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_FAV_MOVIES);
//        onCreate(db);
        //        TODO when updatind DB must make ulter query for updating table instead of drop table and recreeate it again:)
        if (oldVersion < 1) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
        }
    }
}