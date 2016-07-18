package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;


public class GetLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient googleApiClient;
    Location lastLocation;
    Toolbar toolbar;
    ImageView logoIcon;
    TextView title,placeText,noMarks;
    LinearLayout loadingLayout;
    ServerRequest serverRequest;
    ListView listView;
    Commons commons;
    boolean network;

    String [] items = {"Amos","Jkay","Allan doe","Qurous nanny"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_location);

        placeText = (TextView) findViewById(R.id.placeText);
        noMarks = (TextView) findViewById(R.id.noMarks);
        listView = (ListView) findViewById(R.id.listView);
        loadingLayout = (LinearLayout) findViewById(R.id.loadinglayout);

        placeText.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);

        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("My location");

        serverRequest = new ServerRequest(this);
        commons = new Commons();
        network = commons.isNetworkAvailable(this);

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        if(network == true){
            googleApiClient.connect();
        }else{
            noMarks.setVisibility(View.VISIBLE);
            noMarks.setText("Sorry, connection failed. Check your internet!");
        }
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        boolean network = commons.isNetworkAvailable(this);
        if(network == true){
            if (lastLocation != null) {
                //placeText.setText(lastLocation.getLatitude()+","+lastLocation.getLongitude());
                String placeLat = String.valueOf(lastLocation.getLatitude());
                String placeLong = String.valueOf(lastLocation.getLongitude());
                getUserLocationAddress(placeLat,placeLong);

                Log.d("GetLocation:",lastLocation.getLatitude()+","+lastLocation.getLongitude());
            }else{
                Log.d("GetLocation:","Last location is null");
            }
        }else{
            Toast.makeText(this, "There is a network problem!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void getUserLocationAddress(final String latitude, final String longitude){
        serverRequest.fetchUserCurrentLocation(latitude,longitude,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    loadingLayout.setVisibility(View.GONE);
                    placeText.setVisibility(View.VISIBLE);

                    placeText.setText("Place not acquired!");
                }else{
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap = arrayList.get(0);
                    if(hashMap.get("location").length()>0){
                        getPlacesVisitedByUsersAround(latitude,longitude);
                        loadingLayout.setVisibility(View.GONE);
                        placeText.setVisibility(View.VISIBLE);

                        placeText.setText(hashMap.get("location"));
                    }

                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }

    public void getPlacesVisitedByUsersAround(String latitude,String longitude){
        serverRequest.fetchLocationUserContents(latitude,longitude,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(final ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    //display no content retrieved
                    Log.d("Location places:","Null");
                }else{
                    Log.d("Total Arraylist:",Integer.toString(arrayList.size()));
                    if(arrayList.size()==0){
                        noMarks.setVisibility(View.VISIBLE);
                    }else{
                        CustomLocationListview customList = new CustomLocationListview(getApplicationContext(),arrayList);
                        listView.setAdapter(customList);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap = arrayList.get(position);

                                Intent intent = new Intent(getApplicationContext(), FeedItemActivity.class);
                                intent.putExtra("hashmap", hashMap);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }
}
