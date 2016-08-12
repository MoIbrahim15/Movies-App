package com.mohamedibrahim.popularmovies.managers;

import android.net.Uri;

import com.mohamedibrahim.popularmovies.managers.interfaces.ConnectionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mohamed Ibrahim on 7/31/2016.
 */
public class ConnectionManager {
    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;
    private String jsonResponse = null;

    public ConnectionManager(Uri buildUri, ConnectionListener listener) {
        jsonResponse = Connector(buildUri);
        listener.FinishConnection(jsonResponse);
    }

    public String Connector(Uri buildUri) {
        try {
            URL url = new URL(buildUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream input = urlConnection.getInputStream();
            if (input == null) {
                return null;
            }
            return getStringData(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String getStringData(InputStream input) {
        try {
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            return buffer.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
