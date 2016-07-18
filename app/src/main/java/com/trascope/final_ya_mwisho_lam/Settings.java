package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facebook.login.LoginManager;


public class Settings extends AppCompatActivity {
    Spinner spinner;
    String[] language = {"English"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                language);
        spinner.setAdapter(arrayAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
            startActivity(new Intent(this, Settings.class));
        }

        if(id == R.id.action_signout){
            LocalUserStorage localUserStorage = new LocalUserStorage(this);
            localUserStorage.clearUserData();
            if(localUserStorage.getUserLogged() == false){
                LoginManager.getInstance().logOut();
                startActivity(new Intent(this,MainActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
