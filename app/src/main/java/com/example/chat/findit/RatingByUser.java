package com.example.chat.findit;

import android.os.Parcel;
import android.os.Parcelable;


public class RatingByUser implements Parcelable {
    private String mAuthorName;
    private String mAuthorProfilePictureUrl;
    private Double mAuthorPlaceRating;
    private String mPlaceRatingRelativeTimeDescription;
    private String mAuthorReviewText;

    public RatingByUser(String mAuthorName, String mAuthorProfilePictureUrl,
                           Double mPlaceRating, String mPlaceRatingRelativeTimeDescription,
                           String mAuthorReviewText){
        this.mAuthorName = mAuthorName;
        this.mAuthorProfilePictureUrl = mAuthorProfilePictureUrl;
        this.mAuthorPlaceRating = mPlaceRating;
        this.mPlaceRatingRelativeTimeDescription = mPlaceRatingRelativeTimeDescription;
        this.mAuthorReviewText = mAuthorReviewText;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getAuthorProfilePictureUrl() {
        return mAuthorProfilePictureUrl;
    }

    public void setAuthorProfilePictureUrl(String authorProfilePictureUrl) {
        mAuthorProfilePictureUrl = authorProfilePictureUrl;
    }

    public Double getAuthorPlaceRating() {
        return mAuthorPlaceRating;
    }

    public void setAuthorPlaceRating(Double authorPlaceRating) {
        mAuthorPlaceRating = authorPlaceRating;
    }

    public String getPlaceRatingRelativeTimeDescription() {
        return mPlaceRatingRelativeTimeDescription;
    }

    public void setPlaceRatingRelativeTimeDescription(String placeRatingRelativeTimeDescription) {
        mPlaceRatingRelativeTimeDescription = placeRatingRelativeTimeDescription;
    }

    public String getAuthorReviewText() {
        return mAuthorReviewText;
    }

    public void setAuthorReviewText(String authorReviewText) {
        mAuthorReviewText = authorReviewText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RatingByUser> CREATOR = new Parcelable
            .Creator<RatingByUser>() {

        @Override
        public RatingByUser createFromParcel(Parcel source) {
            return new RatingByUser(source);
        }

        @Override
        public RatingByUser[] newArray(int size) {
            return new RatingByUser[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthorName);
        dest.writeString(mAuthorProfilePictureUrl);
        dest.writeDouble(mAuthorPlaceRating);
        dest.writeString(mPlaceRatingRelativeTimeDescription);
        dest.writeString(mAuthorReviewText);
    }
    private RatingByUser(Parcel in){
        this.mAuthorName = in.readString();
        this.mAuthorProfilePictureUrl = in.readString();
        this.mAuthorPlaceRating = in.readDouble();
        this.mPlaceRatingRelativeTimeDescription = in.readString();
        this.mAuthorReviewText = in.readString();
    }
}
