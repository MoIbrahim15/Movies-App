package com.mohamedibrahim.popularmovies.managers.interfaces;

import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 9/12/2016.
 */
public interface TrailerReviewsListener {
    void onFinishTrailers(ArrayList<Trailer> trailesList);
    void onFinishReviews(ArrayList<Review> reviewsList);
}
