package com.example.chat.findit;

import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

public class FavActivity  extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    public static final int FAVOURITE_PLACE_DETAIL_LOADER = 0;
    private ArrayList<Position> mFavouritePlaceArrayList = new ArrayList<>();
    private PositionAdapter mPlaceListAdapter;
    private RecyclerView mRecyclerView;
    private FavPlaceAdapter mFavouritePlaceCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setTitle(getString(R.string.favourite_place_list_string));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);
        mFavouritePlaceCursorAdapter = new FavPlaceAdapter(this, null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(mFavouritePlaceCursorAdapter);
        getLoaderManager().initLoader(FAVOURITE_PLACE_DETAIL_LOADER, null, this);
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

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DetailContract.PlaceDetailEntry._ID,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_ID,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_LATITUDE,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_LONGITUDE,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_NAME,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_RATING,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_ADDRESS,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_WEBSITE,
                DetailContract.PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK
        };

        return new CursorLoader(this,
                DetailContract.PlaceDetailEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        ((FavPlaceAdapter) mRecyclerView.getAdapter()).swapCursor(data);

        while (data.moveToNext()){
            String name=data.getString(4);
            String address=data.getString(7);
            Position p=new Position();
            p.setPlaceName(name);
            p.setPlaceAddress(address);
            mFavouritePlaceArrayList.add(p);
        }
        SharedPreferences preferences=getSharedPreferences("prefrencefile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        StringBuilder builder1=new StringBuilder();
        int count=0;
        for(int i=0;i<mFavouritePlaceArrayList.size();i++){
            builder1.append(count+" "+mFavouritePlaceArrayList.get(i).getPlaceName()+mFavouritePlaceArrayList.get(i).getPlaceAddress()+"\n");
            count++;
        }
        String l=builder1.toString();
        editor.putString("data",l);
        editor.commit();
        Intent intent=new Intent(this,FinItWidget.class);
        int[] myIds= AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(),FinItWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,myIds);
        sendBroadcast(intent);

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        ((FavPlaceAdapter) mRecyclerView.getAdapter()).swapCursor(null);
    }
}
