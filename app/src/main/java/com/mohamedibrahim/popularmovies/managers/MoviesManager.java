package com.mohamedibrahim.popularmovies.managers;

import android.os.AsyncTask;

import com.mohamedibrahim.popularmovies.managers.interfaces.ConnectionListener;
import com.mohamedibrahim.popularmovies.managers.interfaces.MoviesListener;
import com.mohamedibrahim.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 7/29/2016.
 **/
public class MoviesManager extends AsyncTask<Void, Void, ArrayList<Movie>> implements ConnectionListener {

    private MoviesListener delegate = null;
    private String jsonResponse = null;
    private String sortBy;
    private ArrayList<Movie> moviesArrayList = new ArrayList<>();

    public MoviesManager(String sortBy, MoviesListener delegate) {
        this.sortBy = sortBy;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {

        new ConnectionManager(NetworkUtils.buildUrl(sortBy), this);
        return getMoviesDataFromJson(jsonResponse);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        delegate.onFinishMovies(movies);
    }

    private ArrayList<Movie> getMoviesDataFromJson(String jsonResponse) {

        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String ADULT = "adult";
        final String OVERVIEW = "overview";
        final String REALSE_DATE = "release_date";
        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String TITLE = "title";
        final String BACKDROP_PATH = "backdrop_path";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        final String VIDEO = "video";
        final String VOTE_AVE = "vote_average";

        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray resultArray = json.getJSONArray(RESULTS);

            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject movieObject = resultArray.getJSONObject(i);

                Movie movie = new Movie();
                movie.setAdult(movieObject.getBoolean(ADULT));
                movie.setBackdropPath(movieObject.getString(BACKDROP_PATH));
                movie.setOverview(movieObject.getString(OVERVIEW));
                movie.setId(movieObject.getInt(ID));
                movie.setOriginalLanguage(movieObject.getString(ORIGINAL_LANGUAGE));
                movie.setPosterPath(movieObject.getString(POSTER_PATH));
                movie.setPopularity(movieObject.getDouble(POPULARITY));
                movie.setReleaseDate(movieObject.getString(REALSE_DATE));
                movie.setOriginalTitle(movieObject.getString(ORIGINAL_TITLE));
                movie.setVoteCount(movieObject.getInt(VOTE_COUNT));
                movie.setTitle(movieObject.getString(TITLE));
                movie.setVoteAverage(movieObject.getDouble(VOTE_AVE));
                movie.setVideo(movieObject.getBoolean(VIDEO));
                moviesArrayList.add(movie);
            }
            return moviesArrayList;
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
