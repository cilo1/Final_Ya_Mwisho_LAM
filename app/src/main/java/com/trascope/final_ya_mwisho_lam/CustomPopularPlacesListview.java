package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 21-Oct-15.
 */
public class CustomPopularPlacesListview extends ArrayAdapter implements View.OnClickListener{
    Context context;
    ArrayList<HashMap<String,String>> arrayList;
    LayoutInflater layoutInflater;
    int posted_user_id, contentID,groupID;
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;
    Commons commons;
    ViewHolder viewHolder;
    HashMap<String, String> hashMap;
    boolean favorite_status, backetlist_status,follow_status;

    public CustomPopularPlacesListview(Context context,ArrayList<HashMap<String,String>> arrayList) {
        super(context, R.layout.popular_places_frag_listview_item,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);

        commons = new Commons();
        backetlist_status = false;
        favorite_status = false;
        follow_status = false;
        localUserStorage = new LocalUserStorage(context);
        serverRequest = new ServerRequest(context);
        user = localUserStorage.getLoggedInUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder = null;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.popular_places_frag_listview_item,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.textPlace = (TextView) convertView.findViewById(R.id.textPlace);
            viewHolder.textPostedBy = (TextView) convertView.findViewById(R.id.textPostedBy);
            viewHolder.totalLikes = (TextView) convertView.findViewById(R.id.textLikes);
            viewHolder.totalBucketlist = (TextView) convertView.findViewById(R.id.textBucketlist);
            viewHolder.mainImage = (ImageView) convertView.findViewById(R.id.imagePlace);

            viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_on_item);
            viewHolder.shareItemBtn = (ImageButton) convertView.findViewById(R.id.share_item);
            viewHolder.favBtn = (ImageButton) convertView.findViewById(R.id.favoriteBtn);
            viewHolder.bucketlistBtn = (ImageButton) convertView.findViewById(R.id.bucketlistBtn);
            viewHolder.followBtn = (ImageButton) convertView.findViewById(R.id.followBtn);
            viewHolder.moreInfoBtn = (ImageButton) convertView.findViewById(R.id.moreInfoBtn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textPostedBy.setOnClickListener(this);
        viewHolder.commentBtn.setOnClickListener(this);
        viewHolder.shareItemBtn.setOnClickListener(this);
        viewHolder.favBtn.setOnClickListener(this);
        viewHolder.bucketlistBtn.setOnClickListener(this);
        viewHolder.followBtn.setOnClickListener(this);
        viewHolder.moreInfoBtn.setOnClickListener(this);


        hashMap = new HashMap<String, String>();
        hashMap = arrayList.get(position);
        viewHolder.textPlace.setText(hashMap.get("place"));
        viewHolder.totalLikes.setText("Likes "+hashMap.get("totalLikes"));
        viewHolder.totalBucketlist.setText("Bucketlist "+hashMap.get("totalBucketlist"));
        viewHolder.textPostedBy.setText("Posted by "+hashMap.get("username"));
        Picasso.with(context).load(hashMap.get("imageUrl")).into(viewHolder.mainImage);

        insertPopularMarksToSQLITE(hashMap.get("popular_id"),hashMap.get("username"),hashMap.get("userImg"),
                hashMap.get("place"),hashMap.get("imageUrl"),hashMap.get("totalLikes"),hashMap.get("totalBucketlist"));

        return convertView;
    }

    static class ViewHolder{
        TextView textPostedBy,textPlace,textRelation,textAbout,timeUploaded,totalLikes,totalBucketlist;
        ImageButton favBtn, shareItemBtn,bucketlistBtn,followBtn,commentBtn,moreInfoBtn;
        ImageView mainImage,postedBy_img;
        int holderPosition;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.comment_on_item:
                Intent intent = new Intent(getContext(),UserCommentsActivity.class);
                intent.putExtra("hashmap",hashMap);
                v.getContext().startActivity(intent);
                break;
            case R.id.share_item:
                Uri bmpUri = getLocalBitmapUri(viewHolder.mainImage);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    getContext().startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                    // ...sharing failed, handle error
                }
                break;
            case R.id.postedBy:
                Intent intentPostedBy = new Intent(getContext(),UserProfileActivity.class);
                intentPostedBy.putExtra("hashmap",hashMap);
                getContext().startActivity(intentPostedBy);
                break;
            case R.id.favoriteBtn:
                likeUserPost(user.userID, contentID);
                if(getFavoriteStatus() == true){
                    viewHolder.favBtn.setBackgroundResource(R.drawable.starr_selected);
                    Toast.makeText(getContext(), "You favorited", Toast.LENGTH_LONG).show();
                }else{
                    viewHolder.favBtn.setBackgroundResource(R.drawable.starr);
                    Toast.makeText(getContext(),"You unfavorited",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bucketlistBtn:
                backetlistUserPost(user.userID, contentID);
                if(getBacketlistStatus() == true){
                    viewHolder.bucketlistBtn.setBackgroundResource(R.drawable.bucketlist_selected);
                    Toast.makeText(getContext(),"You bucketlisted post",Toast.LENGTH_LONG).show();
                }else{
                    viewHolder.bucketlistBtn.setBackgroundResource(R.drawable.bucketlist);
                    Toast.makeText(getContext(),"You unbucketlisted post",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.followBtn:
                followUser(user.userID,posted_user_id,groupID);
                if(getFollowStatus() == true){
                    viewHolder.followBtn.setBackgroundResource(R.drawable.follow_selected);
                }else{
                    viewHolder.followBtn.setBackgroundResource(R.drawable.follow);
                }
                break;
            case R.id.moreInfoBtn:
                selectMoreInfo();
                break;
        }
    }
    public void setFavoriteStatus(boolean status){
        this.favorite_status = status;
    }
    public void setBacketlistStatus(boolean status){
        this.backetlist_status = status;
    }
    public void setFollowStatus(boolean status){
        this.follow_status = status;
    }
    public boolean getFavoriteStatus(){
        return favorite_status;
    }
    public boolean getBacketlistStatus(){
        return backetlist_status;
    }
    public boolean getFollowStatus(){
        return follow_status;
    }
    public void backetlistUserPost(int userID, int contentID){
        serverRequest.backetlistUserPost(userID,contentID,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {
                if(ef.getStatus() != null){
                    setBacketlistStatus(ef.status);
                }else{
                    Log.i("Bucketlist user:", "Failed");
                }
            }
        });
    }
    public void likeUserPost(int userID, int contentID){
        serverRequest.likeUserPost(userID,contentID, new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {
                if(ef.getStatus() != null){
                    setFavoriteStatus(ef.status);
                }else{
                    Log.i("Favorite user:","Failed");
                }
            }
        });
    }
    public void followUser(int userID,int postedByID, int groupID){
        serverRequest.followUser(userID,postedByID,groupID,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

                if(ef.getStatus() != null){
                    setFollowStatus(ef.status);
                }else{
                    Log.i("Follow user:","Failed");
                }
            }


        });
    }

    private void insertPopularMarksToSQLITE(String popularID, String username, String userImg,
                                            String place, String imageUrl,String totalLikes,
                                            String totalBucketlist) {

        FeedReaderDBHelper fdDBHelper = new FeedReaderDBHelper(getContext());
        fdDBHelper.putInfoPopularMarks(fdDBHelper,popularID,username,userImg,place,imageUrl,totalLikes,
                totalBucketlist);
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void selectMoreInfo(){
        final CharSequence[] items = {"Report","Exit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Report")){
                    Intent intent = new Intent(getContext(),Report.class);
                    intent.putExtra("hashmap",hashMap);
                    getContext().startActivity(intent);
                }else if(items[item].equals("Exit")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
