package com.trascope.final_ya_mwisho_lam;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class Login extends ActionBarActivity {
    EditText email, password;
    LocalUserStorage localUserStorage;
    Commons commons;
    String userEmail = null;
    String processStatus = null;
    boolean network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_login);

        Intent userIDIntent = getIntent();
        processStatus = userIDIntent.getStringExtra("register");
        userEmail = userIDIntent.getStringExtra("email");

        commons = new Commons();

        email = (EditText) findViewById(R.id.email);
        if(userEmail != null){
            email.setText(userEmail);
        }
        password = (EditText) findViewById(R.id.password);
        localUserStorage = new LocalUserStorage(this);

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
        toolbarTitle.setText("Sign In");
        setSupportActionBar(toolbar);
    }

    public void signIn(View v){
        String emailString;
        network = commons.isNetworkAvailable(this);

        if(network == true){
            if(userEmail != null){
                emailString = userEmail;
            }else{
                emailString = email.getText().toString();
            }
            String passwordString = password.getText().toString();

            if(emailString.length()!=0 && passwordString.length()!=0){
                if(commons.isValidEmail(emailString) == true) {
                    if(commons.isValidPassword(passwordString) == true){

                        User user = new User(emailString, passwordString);

                        ServerRequest serverRequest = new ServerRequest(this);
                        serverRequest.loginUser(user,new UrlCallBack() {
                            @Override
                            public void done(User user) {
                                if(user != null){
                                    loggedInUser(user);
                                }else{
                                    showErrorMessage();
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

                    }else{
                        Toast.makeText(this,"Sorry, password should be more than 5 characters!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"Sorry, your email format is invalid!",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show();
            }

        }else{

            Toast.makeText(this, "Sorry, connection failed. Check your internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    public void loggedInUser(User returnedUser){
        localUserStorage.storeUserData(returnedUser);
        Log.d("ImageUrl:", returnedUser.userImageUrl);
        localUserStorage.setUserLogged(true);

        if(processStatus != null){
            Log.i("Login status:","new user");
            Intent intent = new Intent(Login.this, Home.class);
            intent.putExtra("register","register");
            startActivity(intent);
        }else{
            Log.i("Login status:","returning user");
            startActivity(new Intent(Login.this, Home.class));
        }
    }

    private void showErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Incorrect user details!");
        builder.setPositiveButton("Ok",null);
        builder.show();
    }

    public void register(View v){
        startActivity(new Intent(this, Register.class));
    }

    public void forgotPassword(View v){
        startActivity(new Intent(this, ForgotPassword.class));
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
