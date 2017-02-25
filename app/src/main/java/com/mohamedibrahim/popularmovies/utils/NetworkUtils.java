package com.mohamedibrahim.popularmovies.utils;

import android.net.Uri;

import com.mohamedibrahim.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Mohamed Ibrahim on 2/20/2017.
 **/
public class NetworkUtils {
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String... params) {
        String BASE_URL = "http://api.themoviedb.org/3/movie";
        Uri.Builder builtUri = Uri.parse(BASE_URL).buildUpon();
        for (String param : params) {
            builtUri.appendPath(param);
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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
