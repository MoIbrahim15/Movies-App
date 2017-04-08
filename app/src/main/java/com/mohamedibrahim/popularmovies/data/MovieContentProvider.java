package com.mohamedibrahim.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.mohamedibrahim.popularmovies.data.MovieContract.MovieEntry.TABLE_FAV_MOVIES;
import static com.mohamedibrahim.popularmovies.data.MovieContract.MovieEntry.TABLE_MOVIES;

/**
 * Created by Mohamed Ibrahim on 2/24/2017.
 **/
@SuppressWarnings("ConstantConditions")
public class MovieContentProvider extends ContentProvider {

    private static final int FAV_MOVIES = 100;
    private static final int FAV_MOVIE_WITH_ID = 101;
    private static final int MOVIES = 102;
    private static final int MOVIE_WITH_ID = 103;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITE_MOVIES, FAV_MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITE_MOVIES + "/#", FAV_MOVIE_WITH_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    private FavoriteDBHelper mFavoriteDBHelper;
    private MoviesDBHelper mMoviesDBHelper;

    @Override
    public boolean onCreate() {
        mFavoriteDBHelper = FavoriteDBHelper.getInstance(getContext());
        mMoviesDBHelper = MoviesDBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db;

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAV_MOVIES:
                db = mFavoriteDBHelper.getReadableDatabase();
                retCursor = db.query(TABLE_FAV_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES:
                db = mMoviesDBHelper.getReadableDatabase();
                retCursor = db.query(TABLE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mFavoriteDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAV_MOVIES:
                long id = db.insert(TABLE_FAV_MOVIES, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_FAVORITE, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db;
        int match = sUriMatcher.match(uri);

        int tasksDeleted;

        switch (match) {
            case FAV_MOVIES:
                db = mFavoriteDBHelper.getWritableDatabase();
                tasksDeleted = db.delete(TABLE_FAV_MOVIES, selection, selectionArgs);
                break;
            case MOVIES:
                db = mMoviesDBHelper.getWritableDatabase();
                tasksDeleted = db.delete(TABLE_MOVIES, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted = 0;
        String table = "";
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                table = TABLE_MOVIES;
                break;
        }
        SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues cv : values) {
                long newID = db.insertOrThrow(table, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }
}
