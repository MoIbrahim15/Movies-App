package com.mohamedibrahim.popularmovies.managers;

import android.os.AsyncTask;

import com.mohamedibrahim.popularmovies.managers.interfaces.ConnectionListener;
import com.mohamedibrahim.popularmovies.managers.interfaces.TrailerReviewsListener;
import com.mohamedibrahim.popularmovies.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 9/12/2016.
 **/

public class ReviewsManager extends AsyncTask<Void, Void, ArrayList<Review>> implements ConnectionListener {

    private TrailerReviewsListener delegate = null;
    private String jsonResponse = null;
    private String movieID;
    private ArrayList<Review> reviewsArrayList = new ArrayList<>();

    public ReviewsManager(String movieID, TrailerReviewsListener delegate) {
        this.movieID = movieID;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {

        String REVIEWS_PARAM = "reviews";
        new ConnectionManager(NetworkUtils.buildUrl(movieID, REVIEWS_PARAM), this);
        return getReviewsDataFromJson(jsonResponse);
    }


    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        if (reviews != null) delegate.onFinishReviews(reviews);
    }

    private ArrayList<Review> getReviewsDataFromJson(String jsonResponse) {
        final String RESULTS = "results";
        final String ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray resultArray = json.getJSONArray(RESULTS);

            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject movieObject = resultArray.getJSONObject(i);

                Review review = new Review();
                review.setId(movieObject.getString(ID));
                review.setAuthor(movieObject.getString(AUTHOR));
                review.setContent(movieObject.getString(CONTENT));
                review.setUrl(movieObject.getString(URL));
                reviewsArrayList.add(review);
            }
            return reviewsArrayList;

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
