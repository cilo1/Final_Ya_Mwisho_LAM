package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;


public class Home extends AppCompatActivity{
    GoogleApiClient googleApiClient;
    Location lastLocation;
    double latitude,longitude;
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;
    Commons commons;
    boolean network;
    String processStatus = null;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_home);

        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();
        serverRequest = new ServerRequest(this);

        commons = new Commons();
        network = commons.isNetworkAvailable(this);

        Intent userIDIntent = getIntent();
        processStatus = userIDIntent.getStringExtra("register");

        if(processStatus != null){
            learnAboutAppDialog();
        }

        ImageView logoIcon = (ImageView) findViewById(R.id.logoIcon);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Popular"));
        tabLayout.addTab(tabLayout.newTab().setText("Bucketlist"));
        tabLayout.addTab(tabLayout.newTab().setText("My Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("My Tours"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter pagerAdapter = new com.trascope.final_ya_mwisho_lam.PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(),"Home");
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

    public void learnAboutAppDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Explore app");
        dialog.setContentView(R.layout.dialog_learn_about_app);
        TextView welcomeText = (TextView) dialog.findViewById(R.id.welcomeText);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnExplore = (Button) dialog.findViewById(R.id.btnExplore);
        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        User user = localUserStorage.getLoggedInUser();
        welcomeText.setText("Hi "+user.username+", welcome to LeaveAMark");
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home){
            //startActivity(new Intent(this, Home.class));
        }

        if(id == R.id.action_create){
           // startActivity(new Intent(this, Create.class));
            startActivity(new Intent(this, AddPhotos.class));
        }

        if(id == R.id.action_search){
            startActivity(new Intent(this, Search.class));
        }

        if(id == R.id.action_friends){
            startActivity(new Intent(this, Friends.class));
        }

        if(id == R.id.action_account){
            startActivity(new Intent(this,Account.class));
        }

        if(id == R.id.action_open_tour){

            if(localUserStorage.getOpenedTour() == true){
                Toast.makeText(this,"Tour is already Open!",Toast.LENGTH_LONG).show();
                closeTourDialog();
            }else {
                startActivity(new Intent(this, OpenTourIntro.class));
            }
        }

        if(id == R.id.action_favorites){
            startActivity(new Intent(this, Favorite.class));
        }

        if(id == R.id.action_invite_friends){
            startActivity(new Intent(this, InviteFriends.class));
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
        }

        if (id == R.id.action_location) {
            startActivity(new Intent(this, GetLocation.class));
        }

        if(id == R.id.action_signout){

            if(user.appName == "Facebook"){
                LoginManager.getInstance().logOut();
            }
            localUserStorage.clearUserData();

            if(localUserStorage.getUserLogged() == false){

                startActivity(new Intent(this, MainActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeTourDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_close_tour);
        dialog.setTitle("Close current tour");
        dialog.setCanceledOnTouchOutside(true);
        Button close_tour = (Button) dialog.findViewById(R.id.close_tour);
        close_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 UserTours userTours = localUserStorage.getOpenedTourData();
                if(localUserStorage.getOpenedTour() == true){
                    serverRequest.changeTourStatusClosed(user.userID,userTours.tourID, new UrlCallBack() {
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
                                Log.d("Change tour status","Null data");
                            }else{
                                localUserStorage.clearOpenedTour();
                                Toast.makeText(getApplicationContext(),"Tour has been closed!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void changeTourStatus(){

    }
}
