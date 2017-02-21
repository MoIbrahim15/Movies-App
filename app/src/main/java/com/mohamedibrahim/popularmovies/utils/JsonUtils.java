package com.mohamedibrahim.popularmovies.utils;

import com.mohamedibrahim.popularmovies.models.Movie;
import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 2/21/2017.
 **/

public class JsonUtils {
    private static ArrayList<Movie> moviesArrayList = new ArrayList<>();
    private static ArrayList<Review> reviewsArrayList = new ArrayList<>();
    private static ArrayList<Trailer> trailersArrayList = new ArrayList<>();

    public static ArrayList<Movie> getMoviesDataFromJson(String jsonResponse) {

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
            moviesArrayList.clear();
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

    public static ArrayList<Review> getReviewsDataFromJson(String jsonResponse) {
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


    public static ArrayList<Trailer> getTrailersDataFromJson(String jsonResponse) {
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
}
