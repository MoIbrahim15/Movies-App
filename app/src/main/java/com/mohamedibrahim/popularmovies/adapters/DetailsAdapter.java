package com.mohamedibrahim.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;
import com.mohamedibrahim.popularmovies.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 9/14/2016.
 **/
public class DetailsAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_TRAILER = 0;
    private static final int VIEW_TYPE_REVIEW = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private ArrayList<Object> movieDetailsList = new ArrayList<>();
    private Context context;

    public DetailsAdapter(Context context, ArrayList<Object> movieDetailsList) {
        this.context = context;
        this.movieDetailsList = movieDetailsList;
    }

    @Override
    public int getCount() {
        return movieDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position  position
     * @param view      view
     * @param viewGroup viewGroup
     * @return trailer or review view
     */

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        int viewType = getItemViewType(position);
        View mView = null;
        switch (viewType) {
            case VIEW_TYPE_TRAILER: {
                mView = LayoutInflater.from(context).inflate(R.layout.list_item_trailer, viewGroup, false);
                TrailerViewHolder trailerViewHolder = new TrailerViewHolder(mView);
                trailerViewHolder.titleView.setText(((Trailer) movieDetailsList.get(position)).getName());
                trailerViewHolder.titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String videoKEY = ((Trailer) movieDetailsList.get(position)).getKey();
                        final String videoUrlForApp = "vnd.youtube:";
                        final String videoUrlForBrowser = "https://www.youtube.com/watch?v=";
                        Intent trailerIntent;
                        if (Utility.isYouTubeInstalled(context)) {
                            trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrlForApp + videoKEY));
                        } else {
                            trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrlForBrowser + videoKEY));
                        }
                        if (trailerIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(trailerIntent);
                        }
                    }
                });
                mView.setTag(trailerViewHolder);
                break;
            }
            case VIEW_TYPE_REVIEW: {
                mView = LayoutInflater.from(context).inflate(R.layout.list_item_review, viewGroup, false);
                ReviewViewHolder reviewViewHolder = new ReviewViewHolder(mView);
                String authorName = ((Review) movieDetailsList.get(position)).getAuthor() + context.getString(R.string.double_points);
                reviewViewHolder.authorView.setText(authorName);
                reviewViewHolder.contentView.setText(((Review) movieDetailsList.get(position)).getContent());
                mView.setTag(reviewViewHolder);
                break;
            }

        }
        return mView;
    }

    /**
     * @param position position
     * @return view_type
     */

    @Override
    public int getItemViewType(int position) {
        return (movieDetailsList.get(position)
                instanceof Trailer) ? VIEW_TYPE_TRAILER : VIEW_TYPE_REVIEW;
    }

    /**
     * @return view types number
     */
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * TrailerViewHolder
     */
    public static class TrailerViewHolder {
        @BindView(R.id.trailer_list_item_title)
        TextView titleView;

        public TrailerViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * ReviewViewHolder
     */
    public static class ReviewViewHolder {
        @BindView(R.id.review_list_item_author)
        TextView authorView;
        @BindView(R.id.review_list_item_content)
        TextView contentView;

        public ReviewViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
