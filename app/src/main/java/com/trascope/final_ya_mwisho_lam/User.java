package com.trascope.final_ya_mwisho_lam;

import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class User {
    String username,email,password,work,home,bio,countryHome,countryWork;
    Bitmap userImage;
    String userImageUrl;
    String appName;
    int userID;
    long socialUserID;
    ArrayList<HashMap<String,String>> contentList;

    public User(){}

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String username, String socialUserID,Uri userImageUrl){
        this.username = username;
        this.socialUserID = Long.parseLong(socialUserID);
        this.userImageUrl = String.valueOf(userImageUrl);
    }

    public User(String username, String email, String userImageUrl,String appName){
        this.username = username;
        this.email = email;
        this.userImageUrl = userImageUrl;
        this.appName = appName;
    }

    public User(int userID, String username, String email,String userImageUrl,String appName, Long socialUserID){
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.userImageUrl = userImageUrl;
        this.appName = appName;
        this.socialUserID = socialUserID;
    }

    public User(ArrayList<HashMap<String,String>> contentList){
        this.contentList = contentList;
    }

    public User(int userID, String username, String email, String password){
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public ArrayList<HashMap<String,String>> getUserContentList(){
        return contentList;
    }

    public User(int userID,Bitmap userImage,String username, String email, String home, String work,
                String bio, String homeCountry, String workCountry){
        this.userID = userID;
        this.userImage = userImage;
        this.username = username;
        this.email = email;
        this.home = home;
        this.work = work;
        this.bio = bio;
        this.countryHome = homeCountry;
        this.countryWork = workCountry;
    }

    public User(int userID,String userImage,String username, String email, String password, String home, String work,
                String bio,String countryHome,String countryWork){
        this.userID = userID;
        this.userImageUrl = userImage;
        this.username = username;
        this.email = email;
        this.password = password;
        this.home = home;
        this.work = work;
        this.bio = bio;
        this.countryHome = countryHome;
        this.countryWork = countryWork;
    }

}
