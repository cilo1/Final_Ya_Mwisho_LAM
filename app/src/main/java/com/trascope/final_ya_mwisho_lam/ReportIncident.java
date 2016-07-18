package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ReportIncident extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView logoIcon;
    TextView title,reportHeader,reportContent;
    ProgressBar progressBar;
    LinearLayout btnLayout;
    HashMap<String, String> hashMap;
    String itemSelected;
    int contentID;
    Button blockBtn, reportBtn;
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_report_incident);

        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();
        serverRequest = new ServerRequest(this);

        reportHeader = (TextView) findViewById(R.id.reportHeader);
        reportContent = (TextView) findViewById(R.id.reportContent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLayout = (LinearLayout) findViewById(R.id.btn_linearlayout);

        reportBtn = (Button) findViewById(R.id.reportBtn);
        blockBtn = (Button) findViewById(R.id.blockBtn);

        blockBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);

        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashmap");
        itemSelected = intent.getStringExtra("item");
        contentID = Integer.parseInt(hashMap.get("content_id"));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        title = (TextView) toolbar.findViewById(R.id.title);

        logoIcon.setImageResource(R.drawable.back2);
        title.setText("Report");

        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        displayReportContent();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.reportBtn:
                btnLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                reportPost(contentID,itemSelected,user.userID,"Report");
                break;
            case R.id.blockBtn:
                btnLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                blockPost(contentID,itemSelected,user.userID,"Block");
                break;
        }
    }

    public void reportPost(int itemID, String item,int userID,String type){
        Toast.makeText(this, "The post has been reported under the specified cartegory",Toast.LENGTH_LONG).show();
        serverRequest.reportIncident(itemID,item,userID,type,new UrlCallBack() {
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
                    Log.d("Status","Null");
                }else{
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(ReportIncident.this,Home.class));
                }
            }
        });

    }
    public void blockPost(int itemID, String item,int userID,String type){
        Toast.makeText(this, "The post has been removed from your feeds",Toast.LENGTH_LONG).show();
        serverRequest.reportIncident(itemID,item,userID,type,new UrlCallBack() {
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
                    Log.d("Status","Null");
                }else{
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(ReportIncident.this,Home.class));
                }
            }
        });
    }

    public void displayReportContent(){
        String content;
        switch(itemSelected){
            case "Self Injury":
                content = "We remove posts that promote self injury, such as suicide. We may also " +
                        "remove post that attack or make fun of others, identified as victims";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "Harassment or Bullying":
                content = "We remove posts contain credible threats, contents that target certain " +
                        "individuals or group of people to shame, degrade or shame them, any form of " +
                        "blackmail or harassment. If you report or block someone, they won't be told " +
                        "who reported them.";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "Sale or promotion of illegal drugs":
                content = "We remove posts encouraging use of illegal drugs, or those that intent to " +
                        "sell or distribute drugs. If you report or block someone, they won't be told " +
                        "who reported them.";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "Nudity or Pornography":
                content = "We remove any contents of sexual intercourse, posts of genitals, nudity or" +
                        "partially nude children. If you report or block someone, they won't be told " +
                        "who reported them.";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "Violence or Harm":
                content = "We remove contents that are extremely graphic. Contents that encourage violence" +
                        "or that attacks anyone's based on religion, gender, ethnicity or sexual background." +
                        " In addition to, any threats of physical harm, theft, vandalism or financial harm." +
                        " If you report or block someone, they won't be told who reported them.";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "Hate speech or Symbol":
                content = "We remove photos of hate speech or symbols such as swastika. Contents that " +
                        "contain attacks to anyone or individuals. If you report or block someone, they won't be told " +
                        "who reported them. ";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "Intellectual property violation":
                content = "We remove contents or posts that contain copy right or trademark infringement." +
                        "If someone uses your photos or impersonates you without your consent/permission. " +
                        "If you report or block someone, they won't be told who reported them.";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
            case "I just don't like it":
                content = "Block or Unfollow if you don't want to see their posted contents. " +
                        "If you report or block someone, they won't be told who reported them.";
                reportHeader.setText("Report "+itemSelected);
                reportContent.setText(content);
                break;
        }
        reportBtn.setText("Report "+hashMap.get("postedBy"));
        blockBtn.setText("Block "+hashMap.get("postedBy"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_incident, menu);
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
