package com.mohamedibrahim.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedibrahim.popularmovies.R;
import com.mohamedibrahim.popularmovies.Utility;
import com.mohamedibrahim.popularmovies.models.Review;
import com.mohamedibrahim.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 9/14/2016.
 **/
public class DetailsAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_TRAILER = 0;
    private static final int VIEW_TYPE_REVIEW = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private ArrayList<Object> movieDetailesList = new ArrayList<>();
    private Context context;

    public DetailsAdapter(Context context, ArrayList<Object> movieDetailesList) {
        this.context = context;
        this.movieDetailesList = movieDetailesList;
    }

    @Override
    public int getCount() {
        return movieDetailesList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieDetailesList.get(position);
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
                trailerViewHolder.titleView.setText(((Trailer) movieDetailesList.get(position)).getName());
                trailerViewHolder.playView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String videoKEY = ((Trailer) movieDetailesList.get(position)).getKey();
                        final String videoURLforApp = "vnd.youtube:";
                        final String videoURLforBrowser = "https://www.youtube.com/watch?v=";
                        Intent trailerIntent;
                        if (Utility.isYouTubeInstalled(context)) {
                            trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURLforApp + videoKEY));
                        } else {
                            trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURLforBrowser + videoKEY));
                        }
                        context.startActivity(trailerIntent);
                    }
                });
                mView.setTag(trailerViewHolder);
                break;
            }
            case VIEW_TYPE_REVIEW: {
                mView = LayoutInflater.from(context).inflate(R.layout.list_item_review, viewGroup, false);
                ReviewViewHolder reviewViewHolder = new ReviewViewHolder(mView);
                reviewViewHolder.authorView.setText(((Review) movieDetailesList.get(position)).getAuthor() + " :");
                reviewViewHolder.contentView.setText(((Review) movieDetailesList.get(position)).getContent());
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
        return (movieDetailesList.get(position)
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
        public final ImageView playView;
        public final TextView titleView;


        public TrailerViewHolder(View view) {
            playView = (ImageView) view.findViewById(R.id.trailer_list_item_play);
            titleView = (TextView) view.findViewById(R.id.trailer_list_item_title);
        }
    }

    /**
     * ReviewViewHolder
     */
    public static class ReviewViewHolder {
        public final TextView authorView;
        public final TextView contentView;


        public ReviewViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.review_list_item_author);
            contentView = (TextView) view.findViewById(R.id.review_list_item_content);
        }
    }
}
