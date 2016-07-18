package com.trascope.final_ya_mwisho_lam;

/**
 * Created by cmigayi on 18-Dec-15.
 */
public class UserTours {
    int userID,tourID;
    String tourName,placeName,city,date,country;

    public UserTours(int tourID, String tourName, String placeName, String city, String country, String date){
        this.tourID = tourID;
        this.tourName = tourName;
        this.placeName = placeName;
        this.city = city;
        this.country = country;
        this.date = date;
    }
}
