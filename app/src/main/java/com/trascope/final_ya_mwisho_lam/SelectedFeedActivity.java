package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectedFeedActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView mainImage,postedByImg;
    TextView postedBy, textPlace, textTime,textAbout,textPostedBy;
    GridView gridView;
    HashMap<String,String> hashMap;
    ImageButton favBtn, shareItemBtn,bucketlistBtn,followBtn,commentBtn,moreInfoBtn,opentourBtn;
    Commons commons;
    boolean favorite_status, backetlist_status,follow_status;
    CheckBox chkFamily,chkFriend,chkschoolMate,chkworkMate;
    int contentID,posted_user_id,groupID;
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_feed);

        hashMap = new HashMap<String,String>();
        Intent intent = getIntent();
        hashMap = (HashMap<String,String>) intent.getSerializableExtra("hashmap");

        commons = new Commons();
        backetlist_status = false;
        favorite_status = false;
        follow_status = false;
        localUserStorage = new LocalUserStorage(this);
        serverRequest = new ServerRequest(this);
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

        mainImage = (ImageView) findViewById(R.id.mainImage);
        postedByImg = (ImageView) findViewById(R.id.postedByImg);
        textPostedBy = (TextView) findViewById(R.id.textPostedBy);
        textPlace = (TextView) findViewById(R.id.textPlace);
        textTime = (TextView) findViewById(R.id.textTime);
        textAbout = (TextView) findViewById(R.id.textAbout);
        commentBtn = (ImageButton) findViewById(R.id.comment_on_item);
        shareItemBtn = (ImageButton) findViewById(R.id.share_item);
        favBtn = (ImageButton) findViewById(R.id.favoriteBtn);
        bucketlistBtn = (ImageButton) findViewById(R.id.bucketlistBtn);
        followBtn = (ImageButton) findViewById(R.id.followBtn);
        moreInfoBtn = (ImageButton) findViewById(R.id.moreInfoBtn);

        mainImage.setOnClickListener(this);
        followBtn.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        shareItemBtn.setOnClickListener(this);
        bucketlistBtn.setOnClickListener(this);
        moreInfoBtn.setOnClickListener(this);

        contentID = Integer.parseInt(hashMap.get("content_id"));
        posted_user_id = Integer.parseInt(hashMap.get("user_id"));

        Picasso.with(this).load(hashMap.get("userImage")).transform(new CircleTransform()).into(postedByImg);
        Picasso.with(this).load(hashMap.get("imageUrl")).into(mainImage);
        textAbout.setText(Html.fromHtml("<font color=\"#4F9EF7\">" + hashMap.get("username") + "</font>" + " " + hashMap.get("about")));
        textTime.setText(hashMap.get("timeAgo"));
        textPlace.setText(hashMap.get("place"));
        textPostedBy.setText(hashMap.get("postedBy"));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mainImage:
                displayFeedItem(hashMap);
                break;
            case R.id.favoriteBtn:
                Log.d("like:", "clicked fav");
                likeUserPost(user.userID, contentID);
                break;
            case R.id.followBtn:
                displayFollowUserSelectGroup(this,hashMap);
                followUser(user.userID,posted_user_id,groupID);
                if(getFollowStatus() == true){
                    followBtn.setBackgroundResource(R.drawable.follow_selected);
                }else{
                    followBtn.setBackgroundResource(R.drawable.follow);
                }
                break;
            case R.id.bucketlistBtn:
                backetlistUserPost(user.userID, contentID);
                if(getBacketlistStatus() == true){
                    bucketlistBtn.setBackgroundResource(R.drawable.bucketlist_selected);
                    Toast.makeText(this,"You bucketlisted post",Toast.LENGTH_LONG).show();
                }else{
                    bucketlistBtn.setBackgroundResource(R.drawable.bucketlist);
                    Toast.makeText(this,"You unbucketlisted post",Toast.LENGTH_LONG).show();
                }
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
    public void setFavorite(){
        favBtn.setBackgroundResource(R.drawable.starr_selected);
    }
    public void setUnFavorite(){
        favBtn.setBackgroundResource(R.drawable.starr);

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
                    if(ef.getStatus() == true){
                        setFavorite();
                    }else{
                        setUnFavorite();
                    }
                    Log.d("Favorite user:", String.valueOf(ef.status));
                }else{
                    Log.d("Favorite user:","Failed");
                }
            }
        });
    }
    public void followUser(int userID,int postedByID,int groupID){
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

    public void displayFollowUserSelectGroup(Context context,HashMap<String, String> hashMap){
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Follow " + hashMap.get("postedBy"));
        dialog.setContentView(R.layout.dialog_follow_user);
        TextView text = (TextView) dialog.findViewById(R.id.textView);
        ImageView userImg = (ImageView) dialog.findViewById(R.id.userImg);
        Button btnDialogFollow = (Button) dialog.findViewById(R.id.btnDialogFollow);

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
                //set
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
