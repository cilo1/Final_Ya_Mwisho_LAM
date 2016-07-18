package com.trascope.final_ya_mwisho_lam;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnGoogle,btnFacebook;
    LoginButton facebookLoginBtn;
    CallbackManager callbackManager;
    private LoginManager loginManager;
    private AccessToken accessToken;
    User user;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    AccountManager mAccountManager;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //callbackManager = CallbackManager.Factory.create();
        setUpFacebook();
        serverRequest = new ServerRequest(this);
        localUserStorage = new LocalUserStorage(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.title);
        toolbarTitle.setVisibility(View.INVISIBLE);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.logoIcon);
        setSupportActionBar(toolbar);

        btnGoogle = (Button) findViewById(R.id.btnGoogle);
        btnFacebook= (Button) findViewById(R.id.btnFacebook);

        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnGoogle:
                syncGoogleAccount();
                break;
            case R.id.btnFacebook:
                if(isNetworkAvailable() == true) {
                    loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
                }else{
                    Toast.makeText(MainActivity.this, "No Network Service!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for(int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    private AbstractGetNameTask getTask(MainActivity activity, String email, String scope) {
        return new GetNameInForeground(activity, email, scope);
    }

    public void syncGoogleAccount() {
        if(isNetworkAvailable() == true) {
            String[] accountArr = getAccountNames();
            if(accountArr.length > 0) {
                getTask(MainActivity.this, accountArr[0], SCOPE).execute();
                    //String token = GoogleAuthUtil.getToken(this,accountArr[0],SCOPE);
                    Log.d("Account",accountArr[0]);

                Toast.makeText(MainActivity.this, "Google Account Sync!",
                        Toast.LENGTH_SHORT).show();
                Log.d("Accounts","Yes");
            } else {
                Toast.makeText(MainActivity.this, "No Google Account Sync!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "No Network Service!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpFacebook()
    {
        // First initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        // create the callback manager
        callbackManager = CallbackManager.Factory.create();

        // create the access token
        accessToken = AccessToken.getCurrentAccessToken();

        // create the login manager
        loginManager = LoginManager.getInstance();

        // create the callback for the login manager
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                // Your app code goes here for when a login is successful.
                // here I update the access token with the login result.
                accessToken.setCurrentAccessToken(loginResult.getAccessToken());
                Profile profile = Profile.getCurrentProfile();
                final Uri imageUrl = profile.getProfilePictureUri(40,40);

                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject json,
                                    GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Log.i("Status:","ERROR");
                                } else {
                                    Log.i("Status:","Success");
                                    try {
                                        Log.e("GraphResponse", "-------------" + response.toString());
                                        String name = json.getString("name");
                                        String id = json.getString("id");
                                        Log.d("name",name);
                                        Log.d("name", String.valueOf(imageUrl));
                                        Log.d("name", id);

                                        user = new User(name,id,imageUrl);
                                        postUserDetails(user,"Facebook");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).executeAsync();

                //user = new User(profile);
                //postUserDetails(user,"Facebook");
            }

            @Override
            public void onCancel()
            {
                // your app code goes here for when the login is cancelled
                // either by the user or by the Facebook SDK
            }

            @Override
            public void onError(FacebookException exception)
            {
                // your app code goes here for a login exception error
                // such as permissions were denied or another error.
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void postUserDetails(User user,String appName){
        serverRequest.loginSocialMediaUser(user,appName,new UrlCallBack() {
            @Override
            public void done(User user) {
                if(user != null){
                    loggedInSocialUser(user);
                }else{
                    Log.d("Result","No user returned");
                    errorMsgDialog();
                    Toast.makeText(getApplicationContext(),
                            "There is an issue with your network, try again later!",Toast.LENGTH_LONG).show();
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
    }

    public void errorMsgDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_social_login_failed);
        dialog.setTitle("Login Failure!");
        Button closeBtn = (Button) dialog.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void loggedInSocialUser(User returnedUser){
        localUserStorage.storeSocialUser(returnedUser);
        localUserStorage.setUserLogged(true);
        startActivity(new Intent(this, Home.class));
        this.finish();
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            Log.e("Network testing", "Available");
            return true;
        }
        Log.e("Network testing","Not Available");
        return false;
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
    public void register(View v){
        startActivity(new Intent(this, Register.class));
    }
    public void login(View v){
        startActivity(new Intent(this, Login.class));
    }
}
