package com.mohamedibrahim.popularmovies.managers;

import android.net.Uri;

import com.mohamedibrahim.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mohamed Ibrahim on 2/20/2017.
 **/
public class NetworkUtils {
    private static final String API_KEY_PARAM = "api_key";
    private static String BASE_URL = "http://api.themoviedb.org/3/movie";

    public static URL buildUrl(String... params) {
        Uri.Builder builtUri = Uri.parse(BASE_URL).buildUpon();
        for (int i = 0; i < params.length; i++) {
            builtUri.appendPath(params[i]);
        }
        builtUri.appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIES_API_KEY);
        builtUri.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
