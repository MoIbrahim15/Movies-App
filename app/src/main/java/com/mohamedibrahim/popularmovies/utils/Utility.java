package com.mohamedibrahim.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.mohamedibrahim.popularmovies.R;

/**
 * Created by Mohamed Ibrahim on 9/13/2016.
 **/
public class Utility {

    /**
     * @param context context
     * @return PreferredMovies
     */
    public static String getPreferredMovies(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_popular));
    }

    /**
     * @param context context
     * @return isYouTubeInstalled or not
     */
    public static boolean isYouTubeInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        final String youtubeUri = "com.google.android.youtube";
        try {
            pm.getPackageInfo(youtubeUri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * @param context context
     * @return isOnline or not
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumns = (int) (dpWidth / 180);
        return numColumns > 2 ? numColumns : 2;
    }
}
