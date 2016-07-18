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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;


public class RegMoreUserInfo extends AppCompatActivity implements View.OnClickListener{
    Button btnDone;
    String userID;
    ServerRequest serverRequest;
    String home,work,bio,homeCountry,workCountry;
    EditText etHome,etWork,etBio;
    AutoCompleteTextView etHomeCountry,etWorkCountry;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_reg_more_user_info);

        Intent userIDIntent = getIntent();
        userID = userIDIntent.getStringExtra("user_id");
        email = userIDIntent.getStringExtra("email");

        serverRequest = new ServerRequest(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.title);

        toolbarTitle.setText("You are almost there");
        setSupportActionBar(toolbar);

        etHome = (EditText) findViewById(R.id.etHome);
        etWork = (EditText) findViewById(R.id.etWork);
        etBio = (EditText) findViewById(R.id.etBio);
        etHomeCountry = (AutoCompleteTextView) findViewById(R.id.etHomeCountry);
        etWorkCountry = (AutoCompleteTextView) findViewById(R.id.etWorkCountry);

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries =new  ArrayList<String>();

        for(Locale locale : locales){
            String country = locale.getDisplayCountry();
            if(country.trim().length()>0 && !countries.contains(country)){
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                countries);
        etHomeCountry.setAdapter(arrayAdapter);
        etWorkCountry.setAdapter(arrayAdapter);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnDone:
                home = etHome.getText().toString();
                work = etWork.getText().toString();
                bio = etBio.getText().toString();
                homeCountry = etHomeCountry.getText().toString();
                workCountry = etWorkCountry.getText().toString();

                serverRequest.registerUserInfo(Integer.parseInt(userID),home,work,bio,homeCountry,workCountry,
                        new UrlCallBack() {
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
                            Log.d("User Info:","Status null result");
                        }else{
                            Log.d("User Info:","Status is "+ef.status);
                            Intent intent = new Intent(getApplicationContext(),RegMoreUserInfoFinal.class);
                            intent.putExtra("user_id",userID);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }
                    }
                });
                break;
        }

    }

}
