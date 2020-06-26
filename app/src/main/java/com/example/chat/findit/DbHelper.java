package com.example.chat.findit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "place_detail.db";
    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PLACE_DETAIL_QUERY =
                "CREATE TABLE " + DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME + "(" +
                        DetailContract.PlaceDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_ID + " TEXT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_LATITUDE + " REAL, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_LONGITUDE + " REAL, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_NAME + " TEXT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS + " TEXT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_RATING + " REAL ," +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_ADDRESS + " TEXT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER + " TEXT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_WEBSITE + " TEXT, " +
                        DetailContract.PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK + " TEXT" + ")";
        db.execSQL(SQL_CREATE_PLACE_DETAIL_QUERY);    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_PLACE_DETAIL_QUERY =
                "DELETE FROM " + DetailContract.PlaceDetailEntry.PLACE_TABLE_NAME;
        db.execSQL(SQL_DELETE_PLACE_DETAIL_QUERY);
    }
}
