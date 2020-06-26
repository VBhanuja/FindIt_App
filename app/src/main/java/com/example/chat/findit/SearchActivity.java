package com.example.chat.findit;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = SearchActivity.class.getSimpleName();
    private ArrayList<Position> mNearByPlaceArrayList = new ArrayList<>();
    private ProgressBar mProgressBar;
    String locationQueryStringUrl;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);
        linearLayoutManager=new LinearLayoutManager(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        if(isNetworkAvailable()){
        handleIntent(getIntent());
        if(savedInstanceState!=null){
            int pos=savedInstanceState.getInt("pos");
            mNearByPlaceArrayList= (ArrayList<Position>) savedInstanceState.getSerializable("list");
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new PositionAdapter(this,mNearByPlaceArrayList));
        }}
        else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(SearchActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(SearchActivity.this);
            }
            builder.setTitle("No connection")
                    .setMessage("Retry again after some time")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                            mProgressBar.setVisibility(View.INVISIBLE);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String locationName = intent.getStringExtra(SearchManager.QUERY);

            String currentLocation = getSharedPreferences(
                    Constants.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                    .getString(Constants.CURRENT_LOCATION_DATA_KEY, null);

            locationQueryStringUrl = Constants.base_url + Constants.nearby + "/" +
                    Constants.json + "?" + Constants.location + "=" +
                    currentLocation + "&" + Constants.rank + "=" + Constants.distance +
                    "&" + Constants.distance_TAG + "=" + locationName.replace(" ", "%20") + "&" +
                    Constants.key + "=" + Constants.API_KEY;
            Log.d(TAG, locationQueryStringUrl);
            LocationTask task=new LocationTask();
            task.execute(locationQueryStringUrl);
        }
    }
    public class LocationTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String url=strings[0];
                URL u= new URL(url);
                HttpURLConnection connection= (HttpURLConnection) u.openConnection();
                InputStream is=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                StringBuilder builder=new StringBuilder();
                String line=null;
                while ((line=reader.readLine())!=null){
                    builder.append(line+"\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                mProgressBar.setVisibility(View.GONE);
                JSONObject response=new JSONObject(s);
                JSONArray rootJsonArray = response.getJSONArray("results");
                for (int i = 0; i < rootJsonArray.length(); i++) {
                    JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(i);

                    String currentPlaceId = singlePlaceJsonObject.getString("place_id");
                    Double currentPlaceLatitude = singlePlaceJsonObject
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    Double currentPlaceLongitude = singlePlaceJsonObject
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");
                    String currentPlaceName = singlePlaceJsonObject.getString("name");
                    String currentPlaceOpeningHourStatus = singlePlaceJsonObject
                            .has("opening_hours") ? singlePlaceJsonObject
                            .getJSONObject("opening_hours")
                            .getString("open_now") : "Status Not Available";
                    Double currentPlaceRating = singlePlaceJsonObject.has("rating") ?
                            singlePlaceJsonObject.getDouble("rating") : 0;
                    String currentPlaceAddress = singlePlaceJsonObject.has("vicinity") ?
                            singlePlaceJsonObject.getString("vicinity") :
                            "Address Not Available";
                    Position singlePlaceDetail = new Position(
                            currentPlaceId,
                            currentPlaceLatitude,
                            currentPlaceLongitude,
                            currentPlaceName,
                            currentPlaceOpeningHourStatus,
                            currentPlaceRating,
                            currentPlaceAddress);
                    mNearByPlaceArrayList.add(singlePlaceDetail);

                }

                if (mNearByPlaceArrayList.size() == 0) {
                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    PositionAdapter placeListAdapter =
                            new PositionAdapter(SearchActivity.this,
                                    mNearByPlaceArrayList);
                    recyclerView.setLayoutManager(
                            new GridLayoutManager(SearchActivity.this, 1));
                    recyclerView.setAdapter(placeListAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mNearByPlaceArrayList!=null){
            outState.putSerializable("list",mNearByPlaceArrayList);
            int position=linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt("pos",position);
        }
        super.onSaveInstanceState(outState);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SearchActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
