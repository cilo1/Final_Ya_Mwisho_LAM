package com.trascope.final_ya_mwisho_lam;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.ByteArrayOutputStream;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    FragmentManager fm;
    int numOfTabs;
    String activityName;
    Bitmap bitmap;
    String video,place;
    double latitude,longitude;

    public PagerAdapter(FragmentManager fm, int numOfTabs, String actitivityName) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.activityName = actitivityName;
    }
    //We create another constractor for the sake of posting new photos
    public PagerAdapter(FragmentManager fm, int numOfTabs, String actitivityName,Bitmap bitmap) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.activityName = actitivityName;
        this.bitmap = bitmap;
    }

    public PagerAdapter(FragmentManager fm, int numOfTabs, String actitivityName,Bitmap bitmap,String place) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.activityName = actitivityName;
        this.bitmap = bitmap;
        this.place = place;
    }
    //We create another constractor for the sake of posting new videos
    public PagerAdapter(FragmentManager fm, int numOfTabs, String actitivityName,String video) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.activityName = actitivityName;
        this.video= video;
    }

    public PagerAdapter(FragmentManager fm, int numOfTabs, String actitivityName,String video,String place) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.activityName = actitivityName;
        this.video= video;
        this.place = place;
    }

    public PagerAdapter(FragmentManager fm, int numOfTabs, String actitivityName,double latitude,double longitude) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.activityName = actitivityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Fragment getItem(int position) {
        if(activityName == "Home"){
            switch(position){
                case 0:
                    Feeds feeds = new Feeds();
                    Bundle bundle = new Bundle();
                    double[] arrayCoordinates = new double[]{latitude,longitude};
                    bundle.putDoubleArray("arrayCoordinates",arrayCoordinates);
                    feeds.setArguments(bundle);
                    return feeds;
                case 1:
                    PopularPlaces popularPlaces = new PopularPlaces();
                    return popularPlaces;
                case 2:
                    Bucketlist bucketlist = new Bucketlist();
                    return bucketlist;
                case 3:
                    Posts posts = new Posts();
                    return posts;
                case 4:
                    Tours tours = new Tours();
                    return tours;
            }
        }
        if(activityName == "Search"){
            switch(position){
                case 0:
                    SearchPeople searchPeople = new SearchPeople();
                    return searchPeople;
                case 1:
                    SearchPlaces searchPlaces = new SearchPlaces();
                    return searchPlaces;
            }
        }
        if(activityName == "Create"){
            switch(position){
                case 0:
                    NewPlace newPlace = new NewPlace();
                    if(bitmap == null){
                        Bundle bundle = new Bundle();
                        bundle.putString("video",video);
                        bundle.putString("place",place);
                        newPlace.setArguments(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,0,bs);
                        bundle.putByteArray("byteArray",bs.toByteArray());
                        bundle.putString("place",place);
                        newPlace.setArguments(bundle);
                    }
                    return newPlace;
                case 1:
                    Tours tours = new Tours();
                    return tours;
                case 2:
                    Posts posts = new Posts();
                    return posts;
            }
        }
        if(activityName == "Friends"){
            switch (position){
                case 0:
                    Following following = new Following();
                    return following;
                case 1:
                    Followers followers = new Followers();
                    return followers;
                case 2:
                    GroupFriends groupFriends = new GroupFriends();
                    return groupFriends;
            }
        }

        if(activityName == "Marks") {
            switch (position) {
                case 0:
                    UserMarksFragment userMarks = new UserMarksFragment();
                    return userMarks;
                case 1:
                    UserLikesFragment userLikes = new UserLikesFragment();
                    return userLikes;
                case 2:
                    UserBucketlistFragment userBucketlist = new UserBucketlistFragment();
                    return userBucketlist;
            }
        }

        if(activityName == "InviteFriends") {
            switch (position) {
                case 0:
                    InviteContacts inviteContacts = new InviteContacts();
                    return inviteContacts;
                case 1:
                    InviteFacebook inviteFacebook = new InviteFacebook();
                    return inviteFacebook;
                case 2:
                    InviteTwitter inviteTwitter = new InviteTwitter();
                    return  inviteTwitter;
                case 3:
                    InviteViaMoreOptions inviteViaMoreOptions = new InviteViaMoreOptions();
                    return inviteViaMoreOptions;
            }
        }
        return null;
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }
}
