package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class UserGroupData extends AppCompatActivity {
    HashMap<String,String> hashMap;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_user_group_data);

        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashMap");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ImageView logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(hashMap.get("groupName")+" ("+hashMap.get("totalGroupFollowing")+")");

        listView = (ListView) findViewById(R.id.listView);

        serverRequest = new ServerRequest(this);
        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();

        serverRequest.fetchUserGroupsData(user.userID,Integer.parseInt(hashMap.get("groupID")),new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_LONG).show();
                    Log.d("UserGroupData","Null");
                }else{
                    listView.setAdapter(new CustomFollowingListview(getApplicationContext(),arrayList));
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }

}
