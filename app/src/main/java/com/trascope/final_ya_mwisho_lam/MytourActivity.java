package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MytourActivity extends AppCompatActivity {
    HashMap<String, String> hashMap;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;
    Toolbar toolbar;
    ImageView logoIcon;
    TextView title;
    ListView listView;
    //ScrollView scrollView;
    LinearLayout loadinglayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_mytour);

        listView = (ListView) findViewById(R.id.listView);
        //scrollView = (ScrollView) findViewById(R.id.scrollView);
        loadinglayout = (LinearLayout) findViewById(R.id.loadinglayout);
        listView.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);

        Intent intent = getIntent();
        hashMap = new HashMap<String, String>();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashmap");

        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(hashMap.get("place"));

        serverRequest = new ServerRequest(this);
        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();

        getTourContents();
    }

    public void getTourContents(){
        serverRequest.fetchSelectedUserToursContent(user.userID,Integer.parseInt(hashMap.get("tourID")),
                new UrlCallBack() {
                    @Override
                    public void done(User user) {

                    }

                    @Override
                    public void done(Boolean b) {

                    }

                    @Override
                    public void done(final ArrayList<HashMap<String, String>> arrayList) {
                        if(arrayList == null){
                            Toast.makeText(getApplicationContext(),"No data found.",Toast.LENGTH_LONG).show();
                            loadinglayout.setVisibility(View.GONE);
                        }else{
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(new CustomSelectedToursGridview(getBaseContext(), arrayList));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    hashMap = arrayList.get(position);

                                    Intent intent = new Intent(getApplicationContext(), SelectedFeedActivity.class);
                                    intent.putExtra("hashmap", hashMap);
                                    startActivity(intent);
                                }
                            });
                            loadinglayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

                    }
                } );
    }

}
