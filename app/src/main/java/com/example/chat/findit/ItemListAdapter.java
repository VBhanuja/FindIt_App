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


public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private String[] mPlacesListTag;

    public ItemListAdapter(Context context, String[] placesListTag) {
        mContext = context;
        mPlacesListTag = placesListTag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeScreenItemListHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.main_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeScreenItemListHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mPlacesListTag.length;
    }


    private class HomeScreenItemListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mPlaceTextView;
        private ImageView mPlaceImageView;
        private int mItemPosition;

        private HomeScreenItemListHolder(View itemView) {
            super(itemView);
            mPlaceTextView = (TextView) itemView.findViewById(R.id.place_text_view);
            mPlaceImageView = (ImageView) itemView.findViewById(R.id.place_icon);

            mPlaceImageView.setOnClickListener(this);
        }

        private void bindView(int position) {
            mPlaceTextView.setText(mPlacesListTag[position]);

            mPlaceImageView.setImageDrawable(ContextCompat.getDrawable(mContext,
                    DetailProvider.popularPlaceTagIcon[position]));

            mPlaceTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            mItemPosition = position;
        }

        @Override
        public void onClick(View v) {

            if (isNetworkAvailable()) {
                String locationTag = mPlacesListTag[mItemPosition];

                if (locationTag.equals("Bus Stand"))
                    locationTag = "bus_station";
                else if (locationTag.equals("Government Office"))
                    locationTag = "local_government_office";
                else if (locationTag.equals("Railway Station"))
                    locationTag = "train_station";
                else if (locationTag.equals("Hotel"))
                    locationTag = "restaurant";
                else if (locationTag.equals("Police Station"))
                    locationTag = "police";
                else
                    locationTag = locationTag.replace(' ', '_').toLowerCase();
                Intent placeTagIntent = new Intent(mContext, MapsListActivity.class);
                placeTagIntent.putExtra(Constants.LOCATION_NAME_EXTRA_TEXT,
                        DetailProvider.popularPlaceTagName[mItemPosition]);
                placeTagIntent.putExtra(Constants.LOCATION_TYPE_EXTRA_TEXT, locationTag);
                mContext.startActivity(placeTagIntent);
            } else
                Snackbar.make(mPlaceImageView, R.string.no_connection_string,
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
