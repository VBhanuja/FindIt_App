package com.example.chat.findit;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<RatingByUser> mPlaceUserRatingArrayList;

    public RatingAdapter(Context context, ArrayList<RatingByUser> placeUserRatingArrayList) {
        mContext = context;
        mPlaceUserRatingArrayList = placeUserRatingArrayList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaceUserRatingViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.rating_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PlaceUserRatingViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mPlaceUserRatingArrayList.size();
    }

    private class PlaceUserRatingViewHolder extends RecyclerView.ViewHolder{
        private TextView mAuthorNameTextView;
        private CircleImageView mAuthorProfilePictureUrlImageView;
        private TextView mPlaceRatingRelativeTimeDescriptionTextView;
        private MaterialRatingBar mPlaceRatingBarView;
        private TextView mAuthorReviewTextTextView;

        private PlaceUserRatingViewHolder(View itemView) {
            super(itemView);

            mAuthorNameTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
            mPlaceRatingRelativeTimeDescriptionTextView = (TextView) itemView
                    .findViewById(R.id.user_rating_time_age_text_view);
            mPlaceRatingBarView = (MaterialRatingBar) itemView.findViewById(R.id.user_rating);
            mAuthorReviewTextTextView = (TextView) itemView.findViewById(R.id.user_rating_description);
        }

        private void bindView(int position){
            mAuthorNameTextView.setText(mPlaceUserRatingArrayList.get(position).getAuthorName());
            mAuthorNameTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            mPlaceRatingRelativeTimeDescriptionTextView.setText(
                    mPlaceUserRatingArrayList.get(position).getPlaceRatingRelativeTimeDescription());
            mPlaceRatingRelativeTimeDescriptionTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            String ratingValue = String.valueOf(mPlaceUserRatingArrayList.get(position).getAuthorPlaceRating());
            mPlaceRatingBarView.setRating(Float.parseFloat(ratingValue));
            mAuthorReviewTextTextView.setText(
                    mPlaceUserRatingArrayList.get(position).getAuthorReviewText());
            mAuthorReviewTextTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
        }
    }
}
