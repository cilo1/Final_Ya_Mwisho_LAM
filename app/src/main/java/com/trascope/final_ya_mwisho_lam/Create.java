package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class Create extends AppCompatActivity {
    Bitmap bitmap;
    String video;
    PagerAdapter pagerAdapter;
    String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_create);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("");
       // setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Intent intent = getIntent();
        if(intent.getByteArrayExtra("bitmap") == null){
            video = intent.getStringExtra("video");
        }else{
            byte[] byteArr = intent.getByteArrayExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(byteArr,0,byteArr.length);
        }
        place = intent.getStringExtra("place");

        tabLayout.addTab(tabLayout.newTab().setText("New Place"));
        tabLayout.addTab(tabLayout.newTab().setText("My Tours"));
        tabLayout.addTab(tabLayout.newTab().setText("My Posts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        if(intent.getByteArrayExtra("bitmap") == null){
            if(place != null){
                pagerAdapter = new com.trascope.final_ya_mwisho_lam.PagerAdapter(getSupportFragmentManager(),
                        tabLayout.getTabCount(),"Create",video,place);
            }else{
                pagerAdapter = new com.trascope.final_ya_mwisho_lam.PagerAdapter(getSupportFragmentManager(),
                        tabLayout.getTabCount(),"Create",video);
            }
        }else{
            if(place != null){
                pagerAdapter = new com.trascope.final_ya_mwisho_lam.PagerAdapter(getSupportFragmentManager(),
                        tabLayout.getTabCount(),"Create",bitmap,place);
            }else{
                pagerAdapter = new com.trascope.final_ya_mwisho_lam.PagerAdapter(getSupportFragmentManager(),
                        tabLayout.getTabCount(),"Create",bitmap);
            }
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home){
            startActivity(new Intent(this, Home.class));
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

        if(id == R.id.action_favorites){
            //startActivity(new Intent(this, Favorite.class));
        }

        if(id == R.id.action_invite_friends){
            startActivity(new Intent(this, InviteFriends.class));
        }

        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_signout){
            LocalUserStorage localUserStorage = new LocalUserStorage(this);
            localUserStorage.clearUserData();
            if(localUserStorage.getUserLogged() == false){
                startActivity(new Intent(this,MainActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
