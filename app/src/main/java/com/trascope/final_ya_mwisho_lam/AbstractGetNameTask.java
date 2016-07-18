package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 07-Dec-15.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void> {

    protected MainActivity mActivity;
    public static String GOOGLE_USER_DATA = "No data";
    protected String mScope;
    protected String mEmail;
    protected int mRequest;
    User user;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;

    public AbstractGetNameTask(MainActivity mActivity, String mEmail, String mScope) {
        this.mActivity = mActivity;
        this.mEmail = mEmail;
        this.mScope = mScope;
        serverRequest = new ServerRequest(mActivity);
        localUserStorage = new LocalUserStorage(mActivity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            fetchNameFromProfileServer();
        } catch (IOException ex) {
            onError("Following Error occured, please try again. "
                    + ex.getMessage(), ex);
        } catch (JSONException e) {
            onError("Bad response: "
                    + e.getMessage(), e);
        }
        return null;
    }

    protected void onError(String msg, Exception e) {
        if(e != null) {
            Log.e("", "Exception: ", e);
        }
    }

    protected abstract String fetchToken() throws IOException;

    private void fetchNameFromProfileServer() throws IOException, JSONException {
        String token = fetchToken();

        URL url = new URL("https://www.googleapis.com/oauth2" +
                "/v1/userinfo?access_token=" + token);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        if(sc == 200) {
            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();

            /*
            Intent intent = new Intent(mActivity, Home.class);
            intent.putExtra("email_id", mEmail);
            mActivity.startActivity(intent);
            mActivity.finish();
             */
            setUserData();

            return;
        } else if(sc == 401) {
            GoogleAuthUtil.invalidateToken(mActivity, token);
            onError("Server auth error: ", null);
        } else {
            onError("Returned by server: "
                    + sc, null);
            return;
        }
    }

    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }

        return new String(bos.toByteArray(), "UTF-8");
    }

    public void setUserData(){
        JSONObject profileData = null;
        String name = null;
        String userImageUrl = null;
        try {
            profileData = new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);
            if(profileData.has("picture")) {
                userImageUrl = profileData.getString("picture");
            }
            if(profileData.has("name")) {
                name = profileData.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        user = new User(name,mEmail,userImageUrl,"Google");
        postUserDetails(user,"Google");
    }

    public void postUserDetails(User user,String appName){
        serverRequest.loginSocialMediaUser(user,appName,new UrlCallBack() {
            @Override
            public void done(User user) {
                if(user != null){
                    loggedInSocialUser(user);
                }else{
                    Log.d("Result","No user returned");
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

    public void loggedInSocialUser(User returnedUser){
        localUserStorage.storeSocialUser(returnedUser);
        localUserStorage.setUserLogged(true);
        mActivity.startActivity(new Intent(mActivity, Home.class));
        mActivity.finish();
    }
}

