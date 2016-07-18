package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cmigayi on 28-Oct-15.
 */
public class Commons {
    int length;
    boolean favoriteStatus = false;
    Boolean status;
    int totalLikesOrBucketlist;
    LoginManager loginManager;

    public Commons(){}

    public Commons(int length) {
        this.length = length;
    }
    public Commons(Boolean status) {
        this.status = status;
    }
    public Commons(Boolean status, int totalLikesOrBucketlist) {
        this.status = status; this.totalLikesOrBucketlist = totalLikesOrBucketlist;
    }

    public int getListContentSize(){
        return this.length;
    }

    public boolean setFavoriteStatus(boolean favoriteStatus){
        this.favoriteStatus = favoriteStatus;
        return this.favoriteStatus;
    }

    public Boolean getStatus(){
        return this.status;
    }

    public boolean getFavoriteStatus(){
        return this.favoriteStatus;
    }

    public static class REQUEST {
        public static final int CONTACT_INVITED = 500;
    }
//Fields validations
    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isValidPassword(String pwd){
        if(pwd.length() >= 5){
            return true;
        }else {
            return false;
        }
    }


    /**
     * This class takes a rectangular bitmap and gives a circular bitmap back.
     * Can be used for profile pictures or other kind of images.
     *
     *
     * @return circular bitmap
     */
    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = null;
        if (bitmap != null) {

            if (bitmap.getWidth() > bitmap.getHeight()) {
                output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            } else {
                output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            float r = 0;

            if (bitmap.getWidth() > bitmap.getHeight()) {
                r = bitmap.getHeight() / 2;
            } else {
                r = bitmap.getWidth() / 2;
            }

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(r, r, r, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        }
        return output;
    }

    public boolean checkInternetConnetion(Context context){
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    public boolean isNetworkAvailable(Context context){
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    public String getTimeAgo(String dateTime) throws java.text.ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(dateTime);
        long epoch = date.getTime();

        String timePassedString = (String) DateUtils.getRelativeTimeSpanString(epoch,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        return timePassedString;
    }


}
