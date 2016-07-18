package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class UserCommentsActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etComment;
    ImageButton postCommentBtn;
    ListView listView;
    HashMap<String, String> hashMap;
    String comment;
    int contentID;
    LocalUserStorage localUserStorage;
    User user;
    ServerRequest serverRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_user_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textAbout = (TextView) findViewById(R.id.textAbout);
        TextView textTime = (TextView) findViewById(R.id.textTime);
        TextView textPlace = (TextView) findViewById(R.id.textPlace);
        etComment = (EditText) findViewById(R.id.etComment);
        postCommentBtn = (ImageButton) findViewById(R.id.postCommentBtn);

        ImageView logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("Comments");

        localUserStorage = new LocalUserStorage(this);
        user = localUserStorage.getLoggedInUser();
        serverRequest = new ServerRequest(this);

        Intent intent2 = getIntent();
        hashMap = (HashMap<String, String>) intent2.getSerializableExtra("hashmap");
        ImageView userImage = (ImageView) findViewById(R.id.userImage);

        contentID = Integer.parseInt(hashMap.get("content_id"));
        setContentID(contentID);

        Picasso.with(this).load(hashMap.get("userImage")).transform(new CircleTransform()).into(userImage);
        textAbout.setText(Html.fromHtml("<font color=\"#4F9EF7\">"+hashMap.get("username")+"</font>"+" "+hashMap.get("about")));
        textTime.setText(hashMap.get("timeAgo"));
        textPlace.setText(hashMap.get("place"));

        listView = (ListView) findViewById(R.id.listView);

        fetchPostedComments(contentID);


        postCommentBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_comments, menu);
        return true;
    }

    public int getContentID(){
        return this.contentID;
    }
    public void setContentID(int contentID){
        this.contentID = contentID;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.postCommentBtn:
                comment = etComment.getText().toString();
                int content_id = getContentID();
                Log.i("contentID",Integer.toString(content_id));
                Log.i("comment",comment);
                sendComment(user.userID, content_id, comment);
                break;
        }
    }

    public void sendComment(int userID,int contentID, String comment){
        serverRequest.commentOnPost(userID,contentID,comment,new UrlCallBack() {
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
                if(ef.getStatus() == true){
                    Toast.makeText(getApplicationContext(),"Comment sent",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),UserCommentsActivity.class);
                    intent.putExtra("hashmap",hashMap);
                    startActivity(intent);
                }
            }
        });
    }

    public void fetchPostedComments(int contentID){
        serverRequest.fetchCommentsOnPost(contentID,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList != null){
                    CustomUserCommentsListview customUserCommentsListview =
                            new CustomUserCommentsListview(getApplicationContext(),arrayList);
                    listView.setAdapter(customUserCommentsListview);
                }

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });

    }
}
