package com.example.chat.findit;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class Provider extends ContentProvider{
    //TAG for the debug purpose
    public static final String TAG = Provider.class.getSimpleName();

    //Path for retrieve data from database
    private static final int PLACE = 100;
    private static final int PLACE_ID = 101;

    //Uri matcher for matching Uri
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //Create Content Uri
        sUriMatcher.addURI(DetailContract.CONTENT_AUTHORITY, DetailContract.PATH_PLACE, PLACE);
        sUriMatcher.addURI(DetailContract.CONTENT_AUTHORITY, DetailContract.PATH_PLACE + "/#", PLACE_ID);
    }

    //Database object
    private DbHelper mPlaceDetailDbHelper;

    @Override
    public boolean onCreate() {
        mPlaceDetailDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase placeDetailDatabase = mPlaceDetailDbHelper.getReadableDatabase();

        //To get the Cursor
        Cursor cursor;
        int matchPlaceUri = sUriMatcher.match(uri);
        switch (matchPlaceUri) {
            case PLACE:
                cursor = placeDetailDatabase.query(DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLACE_ID:
                selection = DetailContract.PlaceDetailEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = placeDetailDatabase.query(DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int matchPlaceUri = sUriMatcher.match(uri);
        switch (matchPlaceUri) {
            case PLACE:
                return insertPlaceDetails(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not support for " + uri);
        }
    }

    private Uri insertPlaceDetails(Uri uri, ContentValues values) {


        SQLiteDatabase placeDetailDatabase = mPlaceDetailDbHelper.getWritableDatabase();
        long id = placeDetailDatabase.insert(DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(TAG, "Failed to insert data into DB" + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase placeDetailDatabase = mPlaceDetailDbHelper.getWritableDatabase();

        int rowDeleted;
        int matchPlaceUri = sUriMatcher.match(uri);
        switch (matchPlaceUri) {
            case PLACE:

                rowDeleted = placeDetailDatabase.delete(DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME,
                        selection, selectionArgs);
                break;

            case PLACE_ID:

                selection = DetailContract.PlaceDetailEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = placeDetailDatabase.delete(DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int matchPlaceUri = sUriMatcher.match(uri);
        switch (matchPlaceUri) {
            case PLACE:
                return DetailContract.PlaceDetailEntry.CONTENT_LIST_TYPE;
            case PLACE_ID:
                return DetailContract.PlaceDetailEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " With match " + matchPlaceUri);
        }
    }
}
