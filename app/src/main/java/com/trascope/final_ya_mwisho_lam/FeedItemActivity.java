package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class FeedItemActivity extends AppCompatActivity implements View.OnClickListener {
    TextView postedBy, textTime,textPostedBy,textPlace,textRelation,textAbout,timeUploaded,
            totalLikes,totalBucketlist,opentourText,loadingTotalLikes,loadingTotalBucketlist;
    ImageButton favBtn, shareItemBtn,bucketlistBtn,followBtn,commentBtn,moreInfoBtn,opentourBtn;
    ImageView mainImage,postedBy_img,postedByImg;
    int holderPosition, posted_user_id, contentID,groupID;
    GridView gridView;
    HashMap<String,String> hashMap;
    User user;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    Commons commons;
    boolean favorite_status, backetlist_status,follow_status;
    CheckBox chkFamily,chkFriend,chkschoolMate,chkworkMate;
    LinearLayout loadinglayout;
    ProgressBar favoriteProgressBar,bucketlistProgressBar,unfollowProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_feed_item);

        hashMap = new HashMap<String,String>();
        Intent intent = getIntent();
        hashMap = (HashMap<String,String>) intent.getSerializableExtra("hashmap");

        serverRequest = new ServerRequest(this);
        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ImageView logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(hashMap.get("place"));

        loadinglayout = (LinearLayout) findViewById(R.id.loadinglayout);
        favoriteProgressBar = (ProgressBar) findViewById(R.id.favoriteProgressBar);
        bucketlistProgressBar = (ProgressBar) findViewById(R.id.bucketlistProgressBar);
        unfollowProgressBar = (ProgressBar) findViewById(R.id.unfollowProgressBar);
        loadingTotalLikes = (TextView) findViewById(R.id.loadingTotalLikes);
        loadingTotalBucketlist = (TextView) findViewById(R.id.loadingTotalBucketlist);

        mainImage = (ImageView) findViewById(R.id.mainImage);
        postedByImg = (ImageView) findViewById(R.id.postedByImg);
        textPostedBy = (TextView) findViewById(R.id.textPostedBy);
        textPlace = (TextView) findViewById(R.id.textPlace);
        textTime = (TextView) findViewById(R.id.textTime);
        textAbout = (TextView) findViewById(R.id.textAbout);
        textRelation = (TextView) findViewById(R.id.textRelation);
        commentBtn = (ImageButton) findViewById(R.id.comment_on_item);
        shareItemBtn = (ImageButton) findViewById(R.id.share_item);
        favBtn = (ImageButton) findViewById(R.id.favoriteBtn);
        bucketlistBtn = (ImageButton) findViewById(R.id.bucketlistBtn);
        followBtn = (ImageButton) findViewById(R.id.followBtn);
        moreInfoBtn = (ImageButton) findViewById(R.id.moreInfoBtn);
        opentourBtn = (ImageButton) findViewById(R.id.opentourBtn);
        opentourText = (TextView) findViewById(R.id.opentourText);
        totalLikes = (TextView) findViewById(R.id.totalLikes);
        totalBucketlist = (TextView) findViewById(R.id.totalBucketlist);

        gridView = (GridView) findViewById(R.id.gridView);

        textPostedBy.setOnClickListener(this);
       // postedBy_img.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
        shareItemBtn.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        bucketlistBtn.setOnClickListener(this);
        followBtn.setOnClickListener(this);
        moreInfoBtn.setOnClickListener(this);
        mainImage.setOnClickListener(this);
       // opentourBtn.setOnClickListener(this);

        posted_user_id = Integer.parseInt(hashMap.get("user_id"));
        contentID = Integer.parseInt(hashMap.get("content_id"));

        Picasso.with(this).load(hashMap.get("userImage")).transform(new CircleTransform()).into(postedByImg);
        Picasso.with(this).load(hashMap.get("imageUrl")).into(mainImage);
        textAbout.setText(Html.fromHtml("<font color=\"#4F9EF7\">" + hashMap.get("username") + "</font>" + " " + hashMap.get("about")));
        textTime.setText(hashMap.get("timeAgo"));
        textPlace.setText(hashMap.get("place"));
        textPostedBy.setText(hashMap.get("postedBy"));
        totalLikes.setText("Likes "+hashMap.get("totalLikes"));
        if(Boolean.parseBoolean(hashMap.get("likeStatus")) == true){
            favBtn.setBackgroundResource(R.drawable.starr_selected);
            totalLikes.setTextColor(Color.parseColor("#F73528"));
        }else{
            favBtn.setBackgroundResource(R.drawable.starr);
            totalLikes.setTextColor(Color.parseColor("#878787"));
        }
        totalBucketlist.setText("Bucketlist "+hashMap.get("totalBucketlist"));
        if(Boolean.parseBoolean(hashMap.get("bucketlistStatus")) == true){
            bucketlistBtn.setBackgroundResource(R.drawable.bucketlist_selected);
            totalBucketlist.setTextColor(Color.parseColor("#00af4d"));
        }else{
            bucketlistBtn.setBackgroundResource(R.drawable.bucketlist);
            totalBucketlist.setTextColor(Color.parseColor("#878787"));
        }
        if(user.userID == posted_user_id){
            followBtn.setVisibility(View.GONE);
        }else{
            if(Boolean.parseBoolean(hashMap.get("followingStatus")) == true){
                followBtn.setBackgroundResource(R.drawable.follow_selected);
            }else{
                followBtn.setBackgroundResource(R.drawable.follow);
            }
        }

        ServerRequest serverRequest = new ServerRequest(this);
        loadAllUsersContent();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.comment_on_item:
                Intent intent = new Intent(this,UserCommentsActivity.class);
                intent.putExtra("hashmap",hashMap);
                v.getContext().startActivity(intent);
                break;
            case R.id.share_item:
                Uri bmpUri = getLocalBitmapUri(mainImage);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                    // ...sharing failed, handle error
                }
                break;
            case R.id.postedBy:
                Intent intentPostedBy = new Intent(this,UserProfileActivity.class);
                intentPostedBy.putExtra("hashmap",hashMap);
                startActivity(intentPostedBy);
                break;
            case R.id.postedBy_img:
                Intent intentPosted_img = new Intent(this,UserProfileActivity.class);
                intentPosted_img.putExtra("hashmap",hashMap);
                startActivity(intentPosted_img);
                break;
            case R.id.favoriteBtn:
                favoriteProgressBar.setVisibility(View.VISIBLE);
                loadingTotalLikes.setVisibility(View.VISIBLE);
                favBtn.setVisibility(View.GONE);
                totalLikes.setVisibility(View.GONE);
                likeUserPost(user.userID, contentID);
                break;
            case R.id.bucketlistBtn:
                bucketlistProgressBar.setVisibility(View.VISIBLE);
                loadingTotalBucketlist.setVisibility(View.VISIBLE);
                bucketlistBtn.setVisibility(View.GONE);
                totalBucketlist.setVisibility(View.GONE);
                backetlistUserPost(user.userID, contentID);
                break;
            case R.id.followBtn:
                if(Boolean.parseBoolean(hashMap.get("followingStatus")) == true){
                    followUser(user.userID, posted_user_id, -1,null);
                    unfollowProgressBar.setVisibility(View.VISIBLE);
                    followBtn.setVisibility(View.GONE);
                }else{
                    displayFollowUserSelectGroup(this,hashMap);
                }
                break;
            case R.id.moreInfoBtn:
                selectMoreInfo();
                break;
        }
    }

    public void loadAllUsersContent(){
        serverRequest.fetchAllUsersContent(user,new UrlCallBack() {
            @Override
            public void done(User user) {
                if(user == null){
                    Toast.makeText(getApplicationContext(),"Check internet connection!",Toast.LENGTH_LONG).show();
                }else{
                    final ArrayList<HashMap<String, String>> arrayList = user.contentList;
                    CustomSearchPlacesGridview customSearchPlacesGridview =
                            new CustomSearchPlacesGridview(getApplicationContext(),arrayList);
                    gridView.setAdapter(customSearchPlacesGridview);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap = arrayList.get(position);
                            Intent intent = new Intent(getApplicationContext(),FeedItemActivity.class);
                            intent.putExtra("hashmap",hashMap);
                            startActivity(intent);
                            finish();
                        }
                    });
                    loadinglayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
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
                if(ef == null){
                    Log.i("bucketlisted:", "Failed");
                }else{
                    Log.i("bucketlisted:",Boolean.toString(ef.status));
                    Log.i("bucketlisted:",Integer.toString(ef.totalLikesOrBucketlist));
                    if(ef.status==true){
                        bucketlistBtn.setBackgroundResource(R.drawable.bucketlist_selected);
                        totalBucketlist.setText("Bucketlist "+ef.totalLikesOrBucketlist);
                        totalBucketlist.setTextColor(Color.parseColor("#00af4d"));
                        Toast.makeText(getApplicationContext(),"You bucketlisted",Toast.LENGTH_LONG).show();
                    }else{
                        bucketlistBtn.setBackgroundResource(R.drawable.bucketlist);
                        totalBucketlist.setText("Bucketlist "+ef.totalLikesOrBucketlist);
                        totalBucketlist.setTextColor(Color.parseColor("#878787"));
                        Toast.makeText(getApplicationContext(),"You Unbucketlisted",Toast.LENGTH_LONG).show();
                    }
                    bucketlistProgressBar.setVisibility(View.GONE);
                    loadingTotalBucketlist.setVisibility(View.GONE);
                    bucketlistBtn.setVisibility(View.VISIBLE);
                    totalBucketlist.setVisibility(View.VISIBLE);
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
                if(ef == null){
                    Log.i("favorited:","Failed");
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                }else{
                    Log.i("favorited:",Boolean.toString(ef.status));
                    if(ef.status==true){
                        favBtn.setBackgroundResource(R.drawable.starr_selected);
                        totalLikes.setText("Likes "+ef.totalLikesOrBucketlist);
                        totalLikes.setTextColor(Color.parseColor("#F73528"));
                        Toast.makeText(getApplicationContext(),"You liked",Toast.LENGTH_LONG).show();
                    }else{
                        favBtn.setBackgroundResource(R.drawable.starr);
                        totalLikes.setText("Likes "+ef.totalLikesOrBucketlist);
                        totalLikes.setTextColor(Color.parseColor("#878787"));
                        Toast.makeText(getApplicationContext(),"You Unliked",Toast.LENGTH_LONG).show();
                    }
                    favoriteProgressBar.setVisibility(View.GONE);
                    loadingTotalLikes.setVisibility(View.GONE);
                    favBtn.setVisibility(View.VISIBLE);
                    totalLikes.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void followUser(int userID,int postedByID,int groupID, final Dialog dialog){
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
                if(ef == null){
                    Log.i("follow:","Failed");
                }else{
                    if(ef.status == false){
                        followBtn.setBackgroundResource(R.drawable.follow);
                        unfollowProgressBar.setVisibility(View.GONE);
                        followBtn.setVisibility(View.VISIBLE);
                    }else{
                        followBtn.setBackgroundResource(R.drawable.follow_selected);
                        dialog.dismiss();
                    }
                    loadAllUsersContent();
                }
            }
        });
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
        final CharSequence[] items = {"Tag a friend","Report this post","Exit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Report")) {
                    Intent intent = new Intent(FeedItemActivity.this, Report.class);
                    intent.putExtra("hashmap", hashMap);
                    startActivity(intent);
                } else if (items[item].equals("Exit")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void displayFollowUserSelectGroup(Context context,HashMap<String, String> hashMap){
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Follow " + hashMap.get("postedBy"));
        dialog.setContentView(R.layout.dialog_follow_user);
        TextView text = (TextView) dialog.findViewById(R.id.textView);
        ImageView userImg = (ImageView) dialog.findViewById(R.id.userImg);
        final LinearLayout loadingFollow = (LinearLayout) dialog.findViewById(R.id.loadingFollow);
        final Button btnDialogFollow = (Button) dialog.findViewById(R.id.btnDialogFollow);

        chkFamily = (CheckBox) dialog.findViewById(R.id.chkFamily);
        chkFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 1;
                chkFriend.setChecked(false);
                chkschoolMate.setChecked(false);
                chkworkMate.setChecked(false);
            }
        });
        chkFriend = (CheckBox) dialog.findViewById(R.id.chkFriend);
        chkFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 2;
                chkFamily.setChecked(false);
                chkschoolMate.setChecked(false);
                chkworkMate.setChecked(false);
            }
        });
        chkschoolMate = (CheckBox) dialog.findViewById(R.id.chkschoolMate);
        chkschoolMate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 4;
                chkFriend.setChecked(false);
                chkFamily.setChecked(false);
                chkworkMate.setChecked(false);
            }
        });
        chkworkMate = (CheckBox) dialog.findViewById(R.id.chkworkMate);
        chkworkMate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 3;
                chkFriend.setChecked(false);
                chkschoolMate.setChecked(false);
                chkFamily.setChecked(false);
            }
        });

        text.setText("Who is "+hashMap.get("postedBy")+" to you?");
        Picasso.with(context).load(hashMap.get("userImage")).transform(new CircleTransform()).into(userImg);

        btnDialogFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingFollow.setVisibility(View.VISIBLE);
                btnDialogFollow.setVisibility(View.GONE);
                followUser(user.userID, posted_user_id, groupID, dialog);
            }
        });

        dialog.show();
    }

    public void displayFeedItem(HashMap<String, String> hashMap){
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(hashMap.get("place"));
        dialog.setContentView(R.layout.dialog_feed_item);
        ImageView imageMain = (ImageView) dialog.findViewById(R.id.imageMain);
        TextView textPlace = (TextView) dialog.findViewById(R.id.textPlace);

        Picasso.with(this).load(hashMap.get("imageUrl")).into(imageMain);
        textPlace.setText("Posted by " + hashMap.get("postedBy"));
        dialog.show();
    }

}
