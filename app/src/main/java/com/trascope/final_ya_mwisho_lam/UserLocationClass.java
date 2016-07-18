package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by cmigayi on 20-Feb-16.
 */
public class UserLocationClass implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    GoogleApiClient googleApiClient;
    Location lastLocation;
    Context context;
    String location;

    public UserLocationClass(Context context) {
        this.context = context;
        location = null;
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void startLocationConnect(){
        googleApiClient.connect();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            //placeText.setText(lastLocation.getLatitude()+","+lastLocation.getLongitude());
            String placeLat = String.valueOf(lastLocation.getLatitude());
            String placeLong = String.valueOf(lastLocation.getLongitude());

            Log.d("GetLocation:", lastLocation.getLatitude() + "," + lastLocation.getLongitude());
            setLocation(lastLocation.getLatitude() + "," + lastLocation.getLongitude());
        }else{
            Log.d("GetLocation:","Last location is null");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
