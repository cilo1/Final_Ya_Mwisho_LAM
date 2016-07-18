package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.OnClickListener;

/**
 * Created by cmigayi on 03-Nov-15.
 */
public class CustomFollowingListview extends ArrayAdapter implements OnClickListener{
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;
    ImageButton unfollowBtn;
    ProgressBar unfollowProgressBar;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;
    int  userFollowingID;

    public CustomFollowingListview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.following_frag_listview_item,arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        hashMap = new HashMap<String, String>();
        serverRequest = new ServerRequest(context);
        localUserStorage = new LocalUserStorage(context);
        user = localUserStorage.getLoggedInUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.following_frag_listview_item, parent, false);
        TextView textUsername = (TextView) convertView.findViewById(R.id.textUsername);
        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        TextView textPlace = (TextView) convertView.findViewById(R.id.textPlace);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        unfollowBtn = (ImageButton) convertView.findViewById(R.id.unfollowBtn);
        unfollowProgressBar = (ProgressBar) convertView.findViewById(R.id.unfollowProgressBar);

        hashMap = arrayList.get(position);

        textUsername.setText(hashMap.get("username"));
        textGroup.setText("_"+hashMap.get("group"));
        textPlace.setText(hashMap.get("lastPlace"));
        Picasso.with(context).load(hashMap.get("userImage")).into(imageView);

        textUsername.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserProfileActivity.class);
                intent.putExtra("hashmap",hashMap);
                context.startActivity(intent);
            }
        });
        textPlace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FeedItemActivity.class);
                intent.putExtra("hashmap",hashMap);
                context.startActivity(intent);
            }
        });
        unfollowBtn.setOnClickListener(this);
        unfollowBtn.setTag(position);
        unfollowProgressBar.setOnClickListener(this);
        unfollowProgressBar.setTag(position);
        return convertView;
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
                if(ef == null){
                    Log.i("follow:", "Failed");
                }else{
                    if(ef.status == false){
                        Intent intent = new Intent(getContext(),Friends.class);
                        getContext().startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position=(Integer)v.getTag();
        Log.d("Tag","Tag:"+position);
        HashMap<String,String> map = new HashMap<>();
        map = arrayList.get(position);
        userFollowingID = Integer.parseInt(map.get("user_following_id"));

        switch(v.getId()){
            case R.id.unfollowBtn:
                unfollowProgressBar.setVisibility(View.VISIBLE);
                unfollowBtn.setVisibility(View.GONE);
                Log.d("following", String.valueOf(userFollowingID));
                Log.d("following", String.valueOf(user.userID));
                followUser(user.userID, userFollowingID, -1);
                break;
        }
    }
}
