package com.example.chat.findit;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    public static final String TAG = ListActivity.class.getSimpleName();
    private ArrayList<Position> mNearByPlaceArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private PositionAdapter mPlaceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String locationTag = getIntent().getStringExtra(Constants.LOCATION_TYPE_EXTRA_TEXT);
        String locationName = getIntent().getStringExtra(Constants.LOCATION_NAME_EXTRA_TEXT);
        String currentLocation = getSharedPreferences(
                Constants.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                .getString(Constants.CURRENT_LOCATION_DATA_KEY, null);

        String locationQueryStringUrl = Constants.base_url + Constants.nearby + "/" +
                Constants.json + "?" + Constants.location + "=" +
                currentLocation + "&" + Constants.radius + "=" +
                Constants.value + "&" + Constants.place_type + "=" + locationTag +
                "&" + Constants.key + "=" + Constants.API_KEY;

        Log.d(TAG, locationQueryStringUrl);
        String actionBarTitleText = getResources().getString(R.string.near_by_tag) +
                " " + locationName + " " + getString(R.string.list_tag);
        setTitle(actionBarTitleText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNearByPlaceArrayList = getIntent()
                .getParcelableArrayListExtra(Constants.ALL_NEARBY_LOCATION_KEY);
        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);
        if (mNearByPlaceArrayList.size() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGridLayoutManager = new GridLayoutManager(this, 1);
            mPlaceListAdapter = new PositionAdapter(this, mNearByPlaceArrayList);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setAdapter(mPlaceListAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
