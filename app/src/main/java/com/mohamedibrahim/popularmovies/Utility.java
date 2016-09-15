package com.mohamedibrahim.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by Mohamed Ibrahim on 9/13/2016.
 */
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
}
