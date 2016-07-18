package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by cmigayi on 07-Nov-15.
 */
public class FeedReaderContract {

    public FeedReaderContract() {
    }

    public static abstract class FeedUserContent implements BaseColumns{
        public static final String TABLE_NAME = "usercontents";
        public static final String COLUMN_NAME_USER_CONTENT_ID = "content_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_POSTED_BY = "postedBy";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_IMAGEURL = "imageUrl";
        public static final String COLUMN_NAME_RELATION = "relation";
        public static final String COLUMN_NAME_TOTAL_LIKES = "totalLikes";
        public static final String COLUMN_NAME_TOTAL_BUCKETLIST = "bucketlist";
        public static final String COLUMN_NAME_DATE_TIME = "dateTime";
    }

    public  static abstract class FeedPopularPlaces implements BaseColumns{
        public static final String TABLE_NAME = "popularmarks";
        public static final String COLUMN_NAME_POPULAR_MARKS_ID = "popular_id";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_USER_IMAGE = "userImage";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_IMAGEURL = "imageUrl";
        public static final String COLUMN_NAME_TOTAL_LIKES = "totalLikes";
        public static final String COLUMN_NAME_TOTAL_BUCKETLIST = "bucketlist";
    }

}
