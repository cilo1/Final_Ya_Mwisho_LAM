package com.trascope.final_ya_mwisho_lam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by cmigayi on 07-Nov-15.
 */
public class FeedReaderDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "LAMFinal.db";

    public static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE_USER_CONTENTS = "CREATE TABLE "+
            FeedReaderContract.FeedUserContent.TABLE_NAME+"("+
            FeedReaderContract.FeedUserContent._ID + " INTEGER PRIMARY KEY,"+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_CONTENT_ID + TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_POSTED_BY + TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_PLACE+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_IMAGEURL+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_RELATION+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_TOTAL_LIKES+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_TOTAL_BUCKETLIST+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedUserContent.COLUMN_NAME_DATE_TIME+ TEXT_TYPE +")";

    public static final String SQL_CREATE_TABLE_POPULAR_MARKS = "CREATE TABLE "+
            FeedReaderContract.FeedPopularPlaces.TABLE_NAME+"("+
            FeedReaderContract.FeedPopularPlaces._ID + " INTEGER PRIMARY KEY,"+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_POPULAR_MARKS_ID + TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_USER_IMAGE + TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_PLACE+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_IMAGEURL+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_TOTAL_LIKES+ TEXT_TYPE + COMMA_SEP+
            FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_TOTAL_BUCKETLIST+ TEXT_TYPE +")";

    public static final String SQL_DELETE_USER_CONTENTS = "DROP TABLE IF EXISTS "+
            FeedReaderContract.FeedUserContent.TABLE_NAME;

    public static final String SQL_DELETE_POPULAR_MARKS = "DROP TABLE IF EXISTS "+
            FeedReaderContract.FeedPopularPlaces.TABLE_NAME;

    private static final String DATABASE_ALTER_USER_CONTENTS = "ALTER TABLE "+
            FeedReaderContract.FeedUserContent.TABLE_NAME + " ADD COLUMN " +
            FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_ID + TEXT_TYPE +";";


    public FeedReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("Database:","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER_CONTENTS);
        db.execSQL(SQL_CREATE_TABLE_POPULAR_MARKS);
        Log.i("Database:","Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_CONTENTS);
        db.execSQL(SQL_DELETE_POPULAR_MARKS);
        //db.execSQL(DATABASE_ALTER_USER_CONTENTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean checkIfTableHasData(SQLiteDatabase sdb,String TableName){
        String count = "SELECT count(*) FROM "+TableName;
        Cursor mcursor = sdb.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){
            return true;
        }else{
            return false;
        }
    }

    public void putInformation(FeedReaderDBHelper frDBHelper,String contentID,String userID,String postedBy,
                               String place, String imgUrl, String relations, String totalLikes,
                               String totalBucketlist, String dateTime){

        SQLiteDatabase sdb = frDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_CONTENT_ID, contentID);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_ID, userID);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_POSTED_BY,postedBy);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_PLACE, place);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_IMAGEURL, imgUrl);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_RELATION,relations);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_TOTAL_LIKES, totalLikes);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_TOTAL_BUCKETLIST, totalBucketlist);
        values.put(FeedReaderContract.FeedUserContent.COLUMN_NAME_DATE_TIME, dateTime);

        sdb.insert(FeedReaderContract.FeedUserContent.TABLE_NAME,null, values);
        sdb.close();
        Log.i("Database:","User Content row inserted");
    }

    public void putInfoPopularMarks(FeedReaderDBHelper frDBHelper,String popularID, String username,
                                    String userImage, String place, String imgUrl, String totalLikes,
                                    String totalBucketlist){

        SQLiteDatabase sdb = frDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_POPULAR_MARKS_ID, popularID);
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_USERNAME,username);
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_USER_IMAGE, userImage);
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_PLACE, place);
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_IMAGEURL, imgUrl);
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_TOTAL_LIKES, totalLikes);
        values.put(FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_TOTAL_BUCKETLIST, totalBucketlist);

        sdb.insert(FeedReaderContract.FeedPopularPlaces.TABLE_NAME,null, values);
        sdb.close();
        Log.i("Database:","Popular Marks row inserted");

    }

    public Cursor getInformation(FeedReaderDBHelper frDBHelper){
        SQLiteDatabase sdb = frDBHelper.getReadableDatabase();
        String [] columns = {FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_CONTENT_ID,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_USER_ID,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_POSTED_BY,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_PLACE,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_IMAGEURL,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_RELATION,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_TOTAL_LIKES,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_TOTAL_BUCKETLIST,
                FeedReaderContract.FeedUserContent.COLUMN_NAME_DATE_TIME};

        boolean state = checkIfTableHasData(sdb,FeedReaderContract.FeedUserContent.TABLE_NAME);
        if(state == true){
            Cursor cursor = sdb.query(FeedReaderContract.FeedUserContent.TABLE_NAME,
                    columns,null,null,null,null,null);
            // sdb.close();
            return cursor;
        }else{
            return null;
        }

    }

    public Cursor getInfoPopularPlaces(FeedReaderDBHelper frDBHelper){
        SQLiteDatabase sdb = frDBHelper.getReadableDatabase();
        String [] columns = {FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_POPULAR_MARKS_ID,
                FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_USERNAME,
                FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_USER_IMAGE,
                FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_PLACE,
                FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_IMAGEURL,
                FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_TOTAL_LIKES,
                FeedReaderContract.FeedPopularPlaces.COLUMN_NAME_TOTAL_BUCKETLIST };

        boolean state = checkIfTableHasData(sdb,FeedReaderContract.FeedUserContent.TABLE_NAME);
        if(state == true){
            Cursor cursor = sdb.query(FeedReaderContract.FeedPopularPlaces.TABLE_NAME,
                    columns,null,null,null,null,null);

            return cursor;
        }else{
            return null;
        }
    }
}
