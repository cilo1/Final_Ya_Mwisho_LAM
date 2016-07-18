package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class LocalUserStorage {
    SharedPreferences localStorage,toursLocalStorage;
    public static String SP_NAME = "User_Details";
    public static String SP_NAME2 = "User_Tours";

    public LocalUserStorage(Context context) {
        localStorage = context.getSharedPreferences(SP_NAME,0);
        toursLocalStorage = context.getSharedPreferences(SP_NAME2,0);
    }

    public void storeUserTours(ArrayList<HashMap<String,String>> arrayList){
        SharedPreferences.Editor editor = toursLocalStorage.edit();
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap = arrayList.get(0);
        editor.putInt("tourID", Integer.parseInt(hashMap.get("tourID")));
        editor.putString("tourName", hashMap.get("tourName"));
        editor.putString("placeName", hashMap.get("place"));
        editor.putString("city", hashMap.get("city"));
        editor.putString("country", hashMap.get("country"));
        editor.putString("tourDate",hashMap.get("tourDate"));
        editor.commit();
    }

    public void storeUserData(User user){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putInt("user_id", user.userID);
        editor.putString("username",user.username);
        editor.putString("email",user.email);
        editor.putString("password",user.password);
        editor.putString("bio",user.bio);
        editor.putString("userImageUrl", user.userImageUrl);
        //Log.d("ImageUrl:", user.userImageUrl);
        editor.putString("home", user.home);
        editor.putString("work",user.work);
        editor.putString("countryHome",user.countryHome);
        editor.putString("countryWork",user.countryWork);

        editor.commit();
    }

    public void storeUserDataDetails(ArrayList<HashMap<String,String>> arrayList){
        SharedPreferences.Editor editor = localStorage.edit();
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap = arrayList.get(0);
        editor.putInt("user_id", Integer.parseInt(hashMap.get("userID")));
        editor.putString("username", hashMap.get("username"));
        editor.putString("bio",hashMap.get("bio"));
        editor.putString("email",hashMap.get("email"));
        editor.putString("userImageUrl", hashMap.get("userImage"));
        editor.putString("home", hashMap.get("home"));
        editor.putString("work",hashMap.get("work"));
        editor.putString("countryHome",hashMap.get("homecountry"));
        editor.putString("countryWork",hashMap.get("workcountry"));

        editor.commit();
    }

    public void storeSocialUser(User user){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putInt("user_id", user.userID);
        editor.putString("username", user.username);
        editor.putString("userImageUrl", user.userImageUrl);
        editor.putString("appName", user.appName);
        editor.putLong("social_user_id",user.socialUserID);

        editor.commit();
    }

    public User getLoggedInUser(){
        int userID = localStorage.getInt("user_id",-1);
        String username = localStorage.getString("username","");
        String email = localStorage.getString("email","");
        String bio = localStorage.getString("bio","");
        String userImage = localStorage.getString("userImageUrl","");
        String home = localStorage.getString("home","");
        String work = localStorage.getString("work","");
        String countryHome = localStorage.getString("countryHome","");
        String countryWork = localStorage.getString("countryWork","");

        User user = new User(userID,userImage,username,email,null,home,work,bio,countryHome,countryWork);
        return user;
    }

    public UserTours getOpenedTourData(){
        int tourID = toursLocalStorage.getInt("tourID",-1);
        String tourName = toursLocalStorage.getString("tourName","");
        String placeName = toursLocalStorage.getString("placeName","");
        String city = toursLocalStorage.getString("city","");
        String country = toursLocalStorage.getString("country","");
        String tourDate = toursLocalStorage.getString("tourDate","");

        UserTours userTours = new UserTours(tourID,tourName,placeName,city,country,tourDate);
        return userTours;
    }

    public String getUserImage(){
        String userImage = localStorage.getString("userImageUrl","");
        return userImage;
    }

    public void setUserLogged(boolean logged){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putBoolean("userLoggedIn",logged);
        editor.commit();
    }

    public boolean getUserLogged(){
        if(localStorage.getBoolean("userLoggedIn",false) == true){
            return true;
        }else{
            return false;
        }
    }

    public void setOpenedTour(boolean openedTour){
        SharedPreferences.Editor editor = toursLocalStorage.edit();
        editor.putBoolean("tourOpened",openedTour);
        editor.commit();
    }

    public boolean getOpenedTour(){
        if(toursLocalStorage.getBoolean("tourOpened",false) == true){
            return true;
        }else{
            return false;
        }
    }

    public void clearOpenedTour(){
        SharedPreferences.Editor editor = toursLocalStorage.edit();
        editor.clear();
        editor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.clear();
        editor.commit();
    }
}
