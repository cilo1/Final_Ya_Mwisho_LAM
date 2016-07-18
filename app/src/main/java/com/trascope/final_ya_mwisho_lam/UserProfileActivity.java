package com.trascope.final_ya_mwisho_lam;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.*;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;


public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title,usernameText,textBio,textWork,textHome,followText,totalPosts,totalFollowers,totalFollowing;
    ImageView profImage;
    ImageButton btnFollow;
    boolean follow_status;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;
    CheckBox chkFamily,chkFriend,chkschoolMate,chkworkMate;
    HashMap<String,String> hashMap;
    int posted_user_id, groupID;
    ProgressBar unfollowProgressBar;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashmap");
        serverRequest = new ServerRequest(this);
        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        profImage = (ImageView) findViewById(R.id.profImg);
        textBio = (TextView) findViewById(R.id.textBio);
        textWork = (TextView) findViewById(R.id.textWork);
        textHome = (TextView) findViewById(R.id.textHome);
        totalPosts = (TextView) findViewById(R.id.totalPosts);
        totalFollowers = (TextView) findViewById(R.id.totalFollowers);
        totalFollowing = (TextView) findViewById(R.id.totalFollowing);
        btnFollow = (ImageButton) findViewById(R.id.btnFollow);

        unfollowProgressBar = (ProgressBar) findViewById(R.id.unfollowProgressBar);

        btnFollow.setOnClickListener(this);

        posted_user_id = Integer.parseInt(hashMap.get("user_id"));

        Picasso.with(this).load(hashMap.get("userImage")).transform(new CircleTransform()).into(profImage);
        totalPosts.setText(hashMap.get("total_marks"));
        totalFollowers.setText(hashMap.get("total_followers"));
        totalFollowing.setText(hashMap.get("total_following"));
        textBio.setText(hashMap.get("bio"));
        textHome.setText(hashMap.get("home"));
        textWork.setText(hashMap.get("work"));

        if(user.userID == posted_user_id){
            btnFollow.setVisibility(View.GONE);
        }else{
            if(Boolean.parseBoolean(hashMap.get("followingStatus")) == true){
                btnFollow.setBackgroundResource(R.drawable.follow_selected);
            }else{
                btnFollow.setBackgroundResource(R.drawable.follow);
            }
        }

        title.setText(hashMap.get("username"));
        ImageView logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));
        tabLayout.addTab(tabLayout.newTab().setText("Backetlist"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new com.trascope.final_ya_mwisho_lam.PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(),"Marks");
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnFollow:
                if(Boolean.parseBoolean(hashMap.get("followingStatus")) == true){
                    followUser(user.userID, posted_user_id, -1,null);
                    unfollowProgressBar.setVisibility(View.VISIBLE);
                    btnFollow.setVisibility(View.GONE);
                }else{
                    displayFollowUserSelectGroup(this, hashMap);
                }
                break;
        }
    }
    public void setFollowStatus(boolean status){
        this.follow_status = status;
    }
    public boolean getFollowStatus(){
        return follow_status;
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
                        btnFollow.setBackgroundResource(R.drawable.follow);
                        unfollowProgressBar.setVisibility(View.GONE);
                        btnFollow.setVisibility(View.VISIBLE);
                    }else{
                        btnFollow.setBackgroundResource(R.drawable.follow_selected);
                        dialog.dismiss();
                    }
                }
            }
        });
    }
    public void displayFollowUserSelectGroup(Context context,HashMap<String, String> hashMap){
        final Dialog dialog = new Dialog(context);
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
                followUser(user.userID,posted_user_id,groupID,null);
                if(getFollowStatus() == true){
                    btnFollow.setBackgroundResource(R.drawable.follow_selected);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
