package com.mohamedibrahim.popularmovies.managers;

import android.os.AsyncTask;

import com.mohamedibrahim.popularmovies.managers.interfaces.ConnectionListener;
import com.mohamedibrahim.popularmovies.managers.interfaces.TrailerReviewsListener;
import com.mohamedibrahim.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 7/29/2016.
 **/
public class TrailersManager extends AsyncTask<Void, Void, ArrayList<Trailer>> implements ConnectionListener {

    private TrailerReviewsListener delegate = null;
    private String movieID;
    private String jsonResponse = null;
    private ArrayList<Trailer> trailersArrayList = new ArrayList<>();

    public TrailersManager(String movieID, TrailerReviewsListener delegate) {
        this.movieID = movieID;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Trailer> doInBackground(Void... voids) {
        String VIDEOS_PARAM = "videos";
        new ConnectionManager(NetworkUtils.buildUrl(movieID, VIDEOS_PARAM), this);
        return getTrailersDataFromJson(jsonResponse);
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        if (trailers != null) delegate.onFinishTrailers(trailers);
    }

    private ArrayList<Trailer> getTrailersDataFromJson(String jsonResponse) {
        final String RESULTS = "results";
        final String ID = "id";
        final String ISO_639 = "iso_639_1";
        final String ISO_3166 = "iso_3166_1";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String TYPE = "type";

        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray resultArray = json.getJSONArray(RESULTS);

            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject movieObject = resultArray.getJSONObject(i);

                Trailer trailer = new Trailer();
                trailer.setId(movieObject.getString(ID));
                trailer.setIso6391(movieObject.getString(ISO_639));
                trailer.setIso31661(movieObject.getString(ISO_3166));
                trailer.setKey(movieObject.getString(KEY));
                trailer.setName(movieObject.getString(NAME));
                trailer.setSite(movieObject.getString(SITE));
                trailer.setSize(movieObject.getInt(SIZE));
                trailer.setType(movieObject.getString(TYPE));
                trailersArrayList.add(trailer);
            }
            return trailersArrayList;

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void FinishConnection(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }
}
