package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.*;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class Friends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ImageView logoIcon = (ImageView) findViewById(R.id.logoIcon);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        CustomSetTabLayout customSetTabLayout = new CustomSetTabLayout(tabLayout,viewPager,getSupportFragmentManager(),"Friends");
        customSetTabLayout.setTabStructure();
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
