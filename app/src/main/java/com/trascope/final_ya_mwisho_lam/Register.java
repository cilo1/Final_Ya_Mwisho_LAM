package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class Register extends AppCompatActivity {
    EditText username, email, password, confirm_pwd;
    TextView errorMsg;
    Commons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_register);

        commons = new Commons();

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pwd);
        confirm_pwd = (EditText) findViewById(R.id.confirmPwd);
        errorMsg = (TextView) findViewById(R.id.errorMsg);

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
        toolbarTitle.setText("Sign Up");
        setSupportActionBar(toolbar);
    }

    public void signUp(View v){
                // register user
                String usernameString = username.getText().toString();
                final String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

        if(usernameString.length()!=0 && emailString.length()!=0 && passwordString.length()!=0){
            if(commons.isValidEmail(emailString) == true) {
                    if(commons.isValidPassword(passwordString) == true){

                        if(passwordString.equals(confirm_pwd.getText().toString())){
                            User user = new User(usernameString, emailString, passwordString);
                            ServerRequest serverRequest = new ServerRequest(this);
                            serverRequest.registerUser(user, new UrlCallBack() {
                                @Override
                                public void done(User user) {
                                    if(user == null){
                                        Log.d("User returned","Null");
                                    }else{
                                        String userID = Integer.toString(user.userID);
                                        Log.d("User returned",userID);
                                        Intent intent = new Intent(Register.this, RegMoreUserInfo.class);
                                        intent.putExtra("user_id",userID);
                                        intent.putExtra("email",emailString);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void done(Boolean b) {

                                }

                                @Override
                                public void done(ArrayList<HashMap<String, String>> arrayList) {

                                }

                                @Override
                                public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

                                }
                            });

                        }else {
                            Toast.makeText(this,"Sorry, password string do not match!",Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(this,"Sorry, password should be more than 5 characters!",Toast.LENGTH_LONG).show();
                    }

            }else{
                Toast.makeText(this,"Sorry, your email format is invalid!",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"All fields are required!",Toast.LENGTH_LONG).show();
        }
     }

    public void login(View v){
        startActivity(new Intent(this, Login.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_howitworks) {
            startActivity(new Intent(this,HowItWorks.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
