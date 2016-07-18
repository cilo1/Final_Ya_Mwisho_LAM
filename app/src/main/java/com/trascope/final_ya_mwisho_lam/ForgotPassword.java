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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ForgotPassword extends AppCompatActivity {
    LinearLayout linearLayout;
    ProgressBar progressBar;
    TextView resultText;
    EditText email;
    ServerRequest serverRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_forgot_password);

        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resultText = (TextView) findViewById(R.id.resultText);
        email = (EditText) findViewById(R.id.email);

        serverRequest = new ServerRequest(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.title);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.logoIcon);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Forgot Password");
        setSupportActionBar(toolbar);
    }

    public void register(View v){
        startActivity(new Intent(this, Register.class));
    }

    public void login(){ startActivity(new Intent(this, Login.class)); }

    public void sendEmail(View v){
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String emailString = email.getText().toString();
        Toast.makeText(this,"Sent successfully",Toast.LENGTH_LONG).show();
        serverRequest.sendEmailForPassword(emailString, new UrlCallBack() {
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
                    Log.d("Status","null");
                }else{
                    progressBar.setVisibility(View.GONE);
                    resultText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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
