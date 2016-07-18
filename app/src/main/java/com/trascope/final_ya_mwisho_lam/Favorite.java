package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class Favorite extends AppCompatActivity {
    ListView listView;
    TextView title, totalFav;
    LinearLayout loadingLayout;
    CustomFavoritesListview customFavoritesListview;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;
    Commons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ImageView logoIcon = (ImageView) findViewById(R.id.logoIcon);
        logoIcon.setImageResource(R.drawable.back2);
        title.setText("Favorites");
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        loadingLayout = (LinearLayout) findViewById(R.id.loadinglayout);
        totalFav = (TextView) findViewById(R.id.totalFav);

        listView.setVisibility(View.GONE);

        localUserStorage = new LocalUserStorage(this);
        serverRequest = new ServerRequest(this);
        user = localUserStorage.getLoggedInUser();
        commons = new Commons();
        boolean network = commons.isNetworkAvailable(this);

        if(network == true){
            getFavoritedItems();
        }else{
            Toast.makeText(this, "There is a network problem!", Toast.LENGTH_LONG).show();
        }

    }

    public void getFavoritedItems(){
        serverRequest.fetchFavoritedContents(user,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                loadingLayout.setVisibility(View.GONE);
                if(arrayList == null){
                    Log.d("Arraylist favorited","null");
                }else{
                    listView.setVisibility(View.VISIBLE);
                    totalFav.setText("My Favourites "+Integer.toString(arrayList.size())+" Place(s) listed");
                    customFavoritesListview = new CustomFavoritesListview(getBaseContext(),arrayList);
                    listView.setAdapter(customFavoritesListview);
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
