package com.example.chat.findit;

import android.content.ContentResolver;
import android.net.Uri;



public class DetailContract {
    public static final String CONTENT_AUTHORITY = "com.example.chat.findit";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PLACE = "place";

    private DetailContract() {
    }

    public static final class PlaceDetailEntry {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLACE);
        public static final String PLACE_TABLE_NAME = "place_detail";
        public static final String _ID = "_id";
        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_PLACE_LATITUDE = "place_latitude";
        public static final String COLUMN_PLACE_LONGITUDE = "place_longitude";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_PLACE_OPENING_HOUR_STATUS = "place_opening_hour_status";
        public static final String COLUMN_PLACE_RATING = "place_rating";
        public static final String COLUMN_PLACE_ADDRESS = "place_address";
        public static final String COLUMN_PLACE_PHONE_NUMBER = "place_phone_number";
        public static final String COLUMN_PLACE_WEBSITE = "place_website";
        public static final String COLUMN_PLACE_SHARE_LINK = "place_share_link";
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
    }
}
