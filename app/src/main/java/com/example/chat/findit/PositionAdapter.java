package com.example.chat.findit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class PositionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<Position> mNearByPlaceArrayList = new ArrayList<>();

    public PositionAdapter(Context context, ArrayList<Position> nearByPlaceArrayList) {
        mContext = context;
        mNearByPlaceArrayList = nearByPlaceArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaceListAdapterViewHolder(LayoutInflater
                .from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PlaceListAdapterViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mNearByPlaceArrayList.size();
    }

    private class PlaceListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int mItemPosition;
        private TextView mPlaceDistanceTextView;
        private TextView mPlaceNameTextView;
        private TextView mPlaceAddressTextView;
        private TextView mPlaceOpenStatusTextView;
        private MaterialRatingBar mPlaceRating;
        private ImageView mLocationIcon;

        private PlaceListAdapterViewHolder(View itemView) {
            super(itemView);

            mPlaceDistanceTextView = (TextView) itemView.findViewById(R.id.place_distance_text_view);
            mPlaceNameTextView = (TextView) itemView.findViewById(R.id.place_name);
            mPlaceAddressTextView = (TextView) itemView.findViewById(R.id.place_address);
            mPlaceOpenStatusTextView = (TextView) itemView.findViewById(R.id.place_open_status);
            mPlaceRating = (MaterialRatingBar) itemView.findViewById(R.id.place_rating);
            mLocationIcon = (ImageView) itemView.findViewById(R.id.location_icon);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position) {
            mItemPosition = position;

            mPlaceNameTextView.setText(mNearByPlaceArrayList.get(mItemPosition).getPlaceName());
            mPlaceNameTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            mPlaceAddressTextView.setText(mNearByPlaceArrayList.get(mItemPosition).getPlaceAddress());
            mPlaceAddressTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            if (mNearByPlaceArrayList.get(mItemPosition).getPlaceOpeningHourStatus().equals("true")) {
                mPlaceOpenStatusTextView.setText(R.string.open);
                mPlaceOpenStatusTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                        "Roboto-Regular.ttf"));
            } else if (mNearByPlaceArrayList.get(mItemPosition).getPlaceOpeningHourStatus().equals("false")) {
                mPlaceOpenStatusTextView.setText(R.string.closed);
                mPlaceOpenStatusTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                        "Roboto-Regular.ttf"));
            } else {
                mPlaceOpenStatusTextView.setText(mNearByPlaceArrayList.get(mItemPosition)
                        .getPlaceOpeningHourStatus());
                mPlaceOpenStatusTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                        "Roboto-Regular.ttf"));
            }
            mPlaceRating.setRating(Float.parseFloat(String.valueOf(mNearByPlaceArrayList.get(mItemPosition)
                    .getPlaceRating())));

            mLocationIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));

            String currentLocation = mContext.getSharedPreferences(
                    Constants.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                    .getString(Constants.CURRENT_LOCATION_DATA_KEY, null);

            String[] currentLocationLatitudeAndLongitude = currentLocation.split(",");

            Double distance = Helper.getDistanceFromLatLonInKm(
                    Double.parseDouble(currentLocationLatitudeAndLongitude[0]),
                    Double.parseDouble(currentLocationLatitudeAndLongitude[1]),
                    mNearByPlaceArrayList.get(mItemPosition).getPlaceLatitude(),
                    mNearByPlaceArrayList.get(mItemPosition).getPlaceLongitude()) / 1000;

            String distanceBetweenTwoPlace = String.valueOf(distance);
            mPlaceDistanceTextView.setText("~ " + distanceBetweenTwoPlace.substring(0, 3) + " Km");
        }
        @Override
        public void onClick(View v) {

            if (isNetworkAvailable()) {
                Intent currentLocationDetailIntent = new Intent(mContext, DetailsActivity.class);
                currentLocationDetailIntent.putExtra(Constants.LOCATION_ID_EXTRA_TEXT,
                        mNearByPlaceArrayList.get(mItemPosition).getPlaceId());
                mContext.startActivity(currentLocationDetailIntent);

            } else
                Snackbar.make(mLocationIcon, R.string.no_connection_string,
                        Snackbar.LENGTH_SHORT).show();
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
    }
}
