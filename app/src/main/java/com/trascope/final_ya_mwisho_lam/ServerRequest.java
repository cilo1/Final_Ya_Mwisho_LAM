package com.trascope.final_ya_mwisho_lam;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class ServerRequest {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    //public static final String SERVER_ADDRESS = "http://10.0.2.2/LAM_final";
    public static final String SERVER_ADDRESS = "http://www.goodsclearanceclub.com/LAM_final";

    public ServerRequest(Context context) {
    }

    public void registerUser(User user, UrlCallBack urlCallBack){
        new RegisterUserAsync(user, urlCallBack).execute();
    }

    public void loginUser(User user, UrlCallBack urlCallBack){
        new LoginUserAsync(user, urlCallBack).execute();
    }

    public void fetchAllUsersContent(User user,UrlCallBack urlCallBack){
        new FetchAllUsersContent(user,urlCallBack).execute();
    }

    public void fetchAllUsersData(UrlCallBack urlCallBack){
        new FetchAllUsersData(urlCallBack).execute();
    }

    public void fetchUserBacketlist(User user,UrlCallBack urlCallBack){
        new FetchUserBacketlist(user,urlCallBack).execute();
    }

    public void fetchPopularMarks(User user, UrlCallBack urlCallBack){
        new FetchPopularMarks(user,urlCallBack).execute();
    }

    public void postMark(User user,int tourID, Bitmap image, String placeName, String country,String about,
                         UrlCallBack urlCallBack){
        new PostMarkAsync(user,tourID, image, placeName, country,about, urlCallBack).execute();
    }

    public void fetchUserPosts(User user, UrlCallBack urlCallBack){
        new FetchAllUserPosts(user, urlCallBack).execute();
    }

    public void changeUserDetails(User user, UrlCallBack urlCallBack){
        new ChangeUserDetailsAsync(user,urlCallBack).execute();
    }

    public void fetchUserFollowingData(int userID, UrlCallBack urlCallBack){
        new FetchFollowingUserDataAsync(userID,urlCallBack).execute();
    }

    public void fetchUserFollowersData(int userID, UrlCallBack urlCallBack){
        new FetchFollowersUserDataAsync(userID,urlCallBack).execute();
    }

    public void likeUserPost(int userID, int contentID, UrlCallBack urlCallBack){
        new LikeUserPostAsync(userID,contentID,urlCallBack).execute();
    }

    public void followUser(int userID,int postedBy,int groupID,UrlCallBack urlCallBack){
        new FollowUserAsync(userID,postedBy,groupID,urlCallBack).execute();
    }

    public void backetlistUserPost(int userID, int contentID, UrlCallBack urlCallBack){
        new BucketlistUserPostAsync(userID,contentID,urlCallBack).execute();
    }

    public void deleteUserPost(int userID, int contentID, UrlCallBack urlCallBack){
        new DeleteUserPostAsync(userID,contentID,urlCallBack).execute();
    }

    public void commentOnPost(int userID, int contentID, String comment, UrlCallBack urlCallBack){
        new CommentOnPostAsync(userID,contentID,comment,urlCallBack).execute();
    }

    public void fetchCommentsOnPost(int contentID,UrlCallBack urlCallBack){
        new FetchCommentsOnPostAsync(contentID,urlCallBack).execute();
    }

    public void loginSocialMediaUser(User user,String appName, UrlCallBack urlCallBack){
        new LoginSocialMediaUsersAsync(user,appName, urlCallBack).execute();
    }

    public void registerUserInfo(int userID,String home,String work,String bio,String homeCountry,
                                 String workCountry,UrlCallBack urlCallBack){
        new RegisterUserInfoAsync(userID,home,work,bio,homeCountry,workCountry,urlCallBack).execute();
    }

    public void fetchUserGroups(int userID, UrlCallBack urlCallBack){
        new FetchUserGroupsAsync(userID, urlCallBack).execute();
    }

    public void fetchUserGroupsData(int userID, int groupID, UrlCallBack urlCallBack){
        new FetchUserGroupsDataAsync(userID, groupID, urlCallBack).execute();
    }

    public void createUserTour(int userID,String tourName,String placeName,String country,String date,
                               UrlCallBack urlCallBack){
        new CreateUserTourAsync(userID,tourName,placeName,country,date,urlCallBack).execute();
    }

    public void fetchUserTours(int userID, UrlCallBack urlCallBack){
        new FetchUserToursAsync(userID,urlCallBack).execute();
    }

    public void fetchSelectedUserToursContent(int userID, int tourID, UrlCallBack urlCallBack){
        new FetchSelectedUserToursContentAsync(userID,tourID,urlCallBack).execute();
    }

    public void updateProfilePhoto(int userID, Bitmap bitmap, UrlCallBack urlCallBack){
        new UploadProfilePhotoAsync(userID,bitmap,urlCallBack).execute();
    }

    public void fetchUserCurrentLocation(String latitude, String longitude,UrlCallBack urlCallBack){
        new FetchUserCurrentLocationAsync(latitude,longitude,urlCallBack).execute();
    }

    public void changeTourStatusClosed(int userID, int tourID, UrlCallBack urlCallBack){
        new ChangeTourStatusClosedAsync(userID,tourID,urlCallBack).execute();
    }

    public void reportIncident(int contentID, String item, int userID, String type, UrlCallBack urlCallBack){
        new ReportIncidentAsync(contentID,item,userID,type,urlCallBack).execute();
    }

    public void sendEmailForPassword(String email, UrlCallBack urlCallBack){
        new SendEmailForPasswordAsync(email,urlCallBack).execute();
    }

    public void fetchLocationUserContents(String latitude, String longitude,UrlCallBack urlCallBack){
        new FetchLocationUserContentsAsync(latitude,longitude,urlCallBack).execute();
    }

    /*This method is not in use*/
    public void searchPlacesAsync(String item, UrlCallBack urlCallBack){
        new SearchPlacesAsync(item,urlCallBack).execute();
    }

    public void fetchFavoritedContents(User user, UrlCallBack urlCallBack){
        new FetchFavoritedContentsAsync(user,urlCallBack).execute();
    }

    public void videoUpload(User user,int tourID, String video,String placeName, String country,String about,
                                 UrlCallBack urlCallBack){
        new VideoUploadAsync(user,tourID,video,placeName,country,about, urlCallBack).execute();
    }

    public void postMarkForNewLocation(int userID,String latitude,String longitude,UrlCallBack urlCallBack){
        new PostMarkForNewLocationIsAsync(userID,latitude,longitude,urlCallBack).execute();
    }

    public class LoginUserAsync extends AsyncTask<Void, Void, User>{
        User user;
        UrlCallBack urlCallBack;
        User returnedUser;

        public LoginUserAsync(User user, UrlCallBack urlCallBack) {
            this.urlCallBack = urlCallBack;
            this.user = user;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email",user.email));
            dataToSend.add(new BasicNameValuePair("password",user.password));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserData.php");
            HttpClient httpClient = new DefaultHttpClient(httpParams);

            returnedUser = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);
                Log.d("Result:",result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    returnedUser = null;
                }else{
                    int userID = jsonObject.getInt("user_id");
                    String username = jsonObject.getString("username");
                    String email = jsonObject.getString("email");
                    String password = jsonObject.getString("password");
                    String userImage = jsonObject.getString("userImage");
                    String bio = jsonObject.getString("bio");
                    String home = jsonObject.getString("home");
                    String homeCountry = jsonObject.getString("homecountry");
                    String work = jsonObject.getString("work");
                    String workCountry = jsonObject.getString("workcountry");

                    returnedUser = new User(userID,userImage,username,email,password,home,work,bio,
                            homeCountry,workCountry);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User user) {
            urlCallBack.done(user);
            super.onPostExecute(user);
        }
    }

    public class RegisterUserAsync extends AsyncTask<Void, Void, User>{
        User user, returnedUser;
        UrlCallBack urlCallBack;

        public RegisterUserAsync(User user, UrlCallBack urlCallBack){
            this.user = user;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username",user.username));
            dataToSend.add(new BasicNameValuePair("email",user.email));
            dataToSend.add(new BasicNameValuePair("password",user.password));

            HttpParams httpParams =new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/register.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    Log.d("Result","No data found");
                }else{
                    int userID = jsonObject.getInt("user_id");
                    returnedUser = new User(userID,null,null,null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User user) {
            urlCallBack.done(user);
            super.onPostExecute(user);
        }
    }

    public class FetchAllUsersContent extends AsyncTask<Void,Void,User>{
        UrlCallBack urlCallBack;
        String result;
        User user;
        JSONArray userContents;
        ArrayList<HashMap<String,String>> dbUserContents;

        public FetchAllUsersContent(User user,UrlCallBack urlCallBack) {
            this.urlCallBack = urlCallBack;
            this.result = null;
            dbUserContents = new ArrayList<HashMap<String, String>>();
            this.user = user;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserContent.php");
            user = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.d("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    user = null;
                }
                userContents = jsonObject.getJSONArray("contents");
                JSONArray usersArray = jsonObject.getJSONArray("users");

                Log.d("Array length:", Integer.toString(userContents.length()));

                for(int i = 0; i<userContents.length();i++){

                    JSONObject c = userContents.getJSONObject(i);
                    JSONObject b = usersArray.getJSONObject(i);

                    String contentID = c.getString("content_id");
                    String userID = c.getString("user_id");
                    String tourID = c.getString("tour_id");
                    String tourStatus = c.getString("tour_status");
                    String postedBy = c.getString("postedBy");
                    String userImg = c.getString("userImage");
                    String place = c.getString("place");
                    String imageUrl = c.getString("imageUrl");
                    String about = c.getString("about");
                    String relation = c.getString("relation");
                    String liked = c.getString("liked");
                    String totalLikes = c.getString("totalLikes");
                    String likeStatus = c.getString("likeStatus");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String bucketlistStatus = c.getString("bucketlistStatus");
                    String followingStatus = c.getString("followingStatus");
                    String timeAgo = c.getString("timeAgo");
                    String username = b.getString("username");
                    String bio = b.getString("bio");
                    String home = b.getString("home");
                    String work = b.getString("work");
                    String email = b.getString("email");
                    String userImage = b.getString("user_image");
                    String total_marks = b.getString("total_marks");
                    String total_likes = b.getString("total_likes");
                    String total_bucketlist = b.getString("total_bucketlist");
                    String total_following = b.getString("total_following");
                    String total_followers = b.getString("total_followers");

                    Log.i("postedBy:", postedBy);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("content_id",contentID);
                    hashMap.put("user_id",userID);
                    hashMap.put("tour_id",tourID);
                    hashMap.put("tourStatus",tourStatus);
                    hashMap.put("postedBy",postedBy);
                    hashMap.put("userImage",userImg);
                    hashMap.put("place",place);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("about",about);
                    hashMap.put("relation",relation);
                    hashMap.put("liked",liked);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("likeStatus",likeStatus);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("bucketlistStatus",bucketlistStatus);
                    hashMap.put("followingStatus",followingStatus);
                    hashMap.put("timeAgo",timeAgo);
                    hashMap.put("username", username);
                    hashMap.put("bio", bio);
                    hashMap.put("home", home);
                    hashMap.put("work", work);
                    hashMap.put("email", email);
                    hashMap.put("userImage", userImage);
                    hashMap.put("total_marks", total_marks);
                    hashMap.put("total_likes", total_likes);
                    hashMap.put("total_bucketlist", total_bucketlist);
                    hashMap.put("total_following", total_following);
                    hashMap.put("total_followers", total_followers);

                    dbUserContents.add(hashMap);
                }
                user = new User(dbUserContents);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            urlCallBack.done(user);
            super.onPostExecute(user);
        }
    }

    public class FetchAllUsersData extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>{
        UrlCallBack urlCallBack;
        String result;
        User user;
        JSONArray userData;
        ArrayList<HashMap<String,String>> dbUserData;

        public FetchAllUsersData(UrlCallBack urlCallBack) {
            this.urlCallBack = urlCallBack;
            this.result = null;
            dbUserData = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("All","all"));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchAllUsers.php");
            user = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    user = null;
                }
                userData = jsonObject.getJSONArray("usersData");

                Log.i("PeopleSearchArray length:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    String userID = c.getString("user_id");
                    String username = c.getString("username");
                    String userImg = c.getString("userImage");
                    String email = c.getString("email");

                    Log.i("user:", username);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("user_id",userID);
                    hashMap.put("userImage",userImg);
                    hashMap.put("username",username);
                    hashMap.put("email",email);

                    dbUserData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return dbUserData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class FetchUserBacketlist extends AsyncTask<Void,Void,ArrayList<HashMap<String,String>>>{
        //int userID;
        UrlCallBack urlCallBack;
        String result;
        JSONArray userData;
        ArrayList<HashMap<String,String>> dbUserData, dataReceived;
        User user;
        Commons ef;

        public FetchUserBacketlist(User user, UrlCallBack urlCallBack) {
            this.user = user;
            this.urlCallBack = urlCallBack;
            dbUserData = new ArrayList<HashMap<String, String>>();
            dataReceived = new ArrayList<HashMap<String, String>>();
            this.result = null;

        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));
            //Log.i("userID:", Integer.toString(user.userID));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchBucketlistContent.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    user = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Array length:", Integer.toString(userData.length()));

                ef = new Commons(userData.length());

                for(int i = 0; i<userData.length();i++) {

                    JSONObject c = userData.getJSONObject(i);
                    String username = c.getString("username");
                    String userImg = c.getString("userImage");
                    String place = c.getString("place");
                    String imageUrl = c.getString("imageUrl");
                    String totalLikes = c.getString("totalLikes");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String timeAgo = c.getString("timeAgo");

                    Log.i("username:", username);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("username",username);
                    hashMap.put("userImage",userImg);
                    hashMap.put("place",place);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("timeAgo",timeAgo);

                    dbUserData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return dbUserData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> arrayList) {
            urlCallBack.done(arrayList,ef);
            super.onPostExecute(arrayList);
        }
    }

    public class FetchPopularMarks extends AsyncTask<Void,Void,ArrayList<HashMap<String,String>>>{
        //int userID;
        UrlCallBack urlCallBack;
        String result;
        JSONArray userData;
        ArrayList<HashMap<String,String>> dbUserData, dataReceived;
        User user;
        Commons ef;

        public FetchPopularMarks(User user, UrlCallBack urlCallBack) {
            this.user = user;
            this.urlCallBack = urlCallBack;
            dbUserData = new ArrayList<HashMap<String, String>>();
            dataReceived = new ArrayList<HashMap<String, String>>();
            this.result = null;

        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchPopularMarks.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    user = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Array length:", Integer.toString(userData.length()));

                //ef = new Commons(userData.length());

                for(int i = 0; i<userData.length();i++) {

                    JSONObject c = userData.getJSONObject(i);
                    String content_id = c.getString("content_id");
                    String popular_id = c.getString("popular_id");
                    String username = c.getString("username");
                    String userImg = c.getString("userImage");
                    String place = c.getString("place");
                    String imageUrl = c.getString("imageUrl");
                    String about = c.getString("about");
                    String totalLikes = c.getString("totalLikes");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String timeAgo = c.getString("timeAgo");

                    Log.i("username:", username);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("content_id",content_id);
                    hashMap.put("popular_id",popular_id);
                    hashMap.put("username",username);
                    hashMap.put("userImage",userImg);
                    hashMap.put("place",place);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("about",about);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("timeAgo",timeAgo);

                    dbUserData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return dbUserData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class PostMarkAsync extends AsyncTask<Void,Void,Commons>{
        Bitmap image;
        String placeName;
        String country;
        String about;
        User user;
        UrlCallBack urlCallBack;
        String result;
        int tourID;
        Commons commons;
        JSONArray userData;
        boolean status;

        public PostMarkAsync(User user, int tourID,Bitmap image, String placeName, String country,String about,
                             UrlCallBack urlCallBack) {
            this.image = image;
            this.placeName = placeName;
            this.country = country;
            this.about = about;
            this.user = user;
            this.urlCallBack = urlCallBack;
            this.tourID = tourID;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
           // Log.i("String Image:",encodedImage);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));
            dataToSend.add(new BasicNameValuePair("tour_id",Integer.toString(tourID)));
            dataToSend.add(new BasicNameValuePair("place",placeName));
            dataToSend.add(new BasicNameValuePair("country",country));
            dataToSend.add(new BasicNameValuePair("about",about));
            dataToSend.add(new BasicNameValuePair("image",encodedImage));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/postMarks.php");
            Log.i("Server:",httpPost.toString());

            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("Result:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public HttpParams getHttpRequestParams(){
        HttpParams http = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(http,1000*30);
        HttpConnectionParams.setSoTimeout(http,1000*30);
        return http;
    }

    public class FetchAllUserPosts extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>{
        UrlCallBack urlCallBack;
        String result;
        User user;
        JSONArray userData;
        ArrayList<HashMap<String,String>> dbUserData;

        public FetchAllUserPosts(User user,UrlCallBack urlCallBack) {
            this.urlCallBack = urlCallBack;
            this.user = user;
            this.result = null;
            dbUserData = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserPosts.php");
            user = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    user = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("PeopleSearchArray length:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    String contentID = c.getString("content_id");
                    String place = c.getString("place");
                    String imageUrl = c.getString("imageUrl");
                    String totalLikes = c.getString("totalLikes");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String timeAgo = c.getString("timeAgo");

                    Log.i("user:", place);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("content_id",contentID);
                    hashMap.put("place",place);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("timeAgo",timeAgo);

                    dbUserData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return dbUserData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class ChangeUserDetailsAsync extends AsyncTask<Void, Void, User>{
        User user;
        UrlCallBack urlCallBack;
        String result;
        JSONArray userContents;
        ArrayList<HashMap<String,String>> dbUserContents;


        public ChangeUserDetailsAsync(User user, UrlCallBack urlCallBack){
            this.user = user;
            this.urlCallBack = urlCallBack;
            this.result = null;
            this.dbUserContents = new ArrayList<HashMap<String,String>>();
        }

        @Override
        protected User doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            user.userImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userImage", encodedImage));
            dataToSend.add(new BasicNameValuePair("user_id", Integer.toString(user.userID)));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("email", user.email));
            dataToSend.add(new BasicNameValuePair("home", user.home));
            dataToSend.add(new BasicNameValuePair("work", user.work));
            dataToSend.add(new BasicNameValuePair("bio", user.bio));
            dataToSend.add(new BasicNameValuePair("homeCountry", user.countryHome));
            dataToSend.add(new BasicNameValuePair("workCountry", user.countryWork));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + "/updateUserDetails.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() == 0) {
                    user = null;
                }
                userContents = jsonObject.getJSONArray("usersData");

                Log.i("Array length:", Integer.toString(userContents.length()));

                for (int i = 0; i < userContents.length(); i++) {

                    JSONObject c = userContents.getJSONObject(i);
                    String userID = c.getString("user_id");
                    String username = c.getString("username");
                    String bio = c.getString("bio");
                    String home = c.getString("home");
                    String work = c.getString("work");
                    String email = c.getString("email");
                    String userImage = c.getString("userImage");
                    String homeCountry = c.getString("homecountry");
                    String workCountry = c.getString("workcountry");

                    Log.i("postedBy:", username);

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("userID", userID);
                    hashMap.put("username", username);
                    hashMap.put("bio", bio);
                    hashMap.put("home", home);
                    hashMap.put("work", work);
                    hashMap.put("email", email);
                    hashMap.put("userImage", userImage);
                    hashMap.put("homecountry", homeCountry);
                    hashMap.put("workcountry", workCountry);

                    dbUserContents.add(hashMap);
                }

                user = new User(dbUserContents);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            urlCallBack.done(user);
            super.onPostExecute(user);
        }
    }

    public class FetchFollowingUserDataAsync extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>{
        int userID;
        UrlCallBack urlCallBack;
        String result;
        JSONArray jsonArray;
        JSONObject jsonObject;
        ArrayList<HashMap<String,String>> userData;
        public FetchFollowingUserDataAsync(int userID,UrlCallBack urlCallBack) {
            this.userID = userID;
            this.urlCallBack = urlCallBack;
            this.result = null;
            userData = new ArrayList<HashMap<String,String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"/following.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.i("Result:",result);

                jsonObject = new JSONObject(result);

                JSONArray following = jsonObject.getJSONArray("following");
                JSONArray userFollowing = jsonObject.getJSONArray("userFollowing");
                JSONArray latestPost = jsonObject.getJSONArray("latestPost");

                Log.i("Array Length:",Integer.toString(following.length()));
                Log.i("Array Length:",Integer.toString(userFollowing.length()));

                for(int i = 0; i<following.length();i++){
                    JSONObject c = following.getJSONObject(i);
                    JSONObject b = userFollowing.getJSONObject(i);
                    JSONObject d = latestPost.getJSONObject(i);

                    String user_following_id = c.getString("user_following_id");
                    String group = c.getString("group");
                    String username = b.getString("username");
                    String bio = b.getString("bio");
                    String home = b.getString("home");
                    String work = b.getString("work");
                    String email = b.getString("email");
                    String userImage = b.getString("user_image");
                    String lastPlace = d.getString("lastPlace");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("user_following_id", user_following_id);
                    hashMap.put("group", group);
                    hashMap.put("username", username);
                    hashMap.put("bio", bio);
                    hashMap.put("home", home);
                    hashMap.put("work", work);
                    hashMap.put("email", email);
                    hashMap.put("userImage", userImage);
                    hashMap.put("lastPlace", lastPlace);

                    userData.add(hashMap);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return userData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class FetchFollowersUserDataAsync extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>{
        int userID;
        UrlCallBack urlCallBack;
        String result;
        JSONArray jsonArray;
        JSONObject jsonObject;
        ArrayList<HashMap<String,String>> userData;
        public FetchFollowersUserDataAsync(int userID,UrlCallBack urlCallBack) {
            this.userID = userID;
            this.urlCallBack = urlCallBack;
            this.result = null;
            userData = new ArrayList<HashMap<String,String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"/followers.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.i("Result:",result);

                jsonObject = new JSONObject(result);

                JSONArray following = jsonObject.getJSONArray("followers");
                JSONArray userFollowing = jsonObject.getJSONArray("userFollowers");
                JSONArray latestPost = jsonObject.getJSONArray("latestPost");

                Log.i("Array Length:",Integer.toString(following.length()));
                Log.i("Array Length:",Integer.toString(userFollowing.length()));

                for(int i = 0; i<following.length();i++){
                    JSONObject c = following.getJSONObject(i);
                    JSONObject b = userFollowing.getJSONObject(i);
                    JSONObject d = latestPost.getJSONObject(i);

                    String user_following_id = c.getString("user_followers_id");
                    String group = c.getString("group");
                    String username = b.getString("username");
                    String bio = b.getString("bio");
                    String home = b.getString("home");
                    String work = b.getString("work");
                    String email = b.getString("email");
                    String userImage = b.getString("user_image");
                    String lastPlace = d.getString("lastPlace");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("user_followers_id", user_following_id);
                    hashMap.put("group", group);
                    hashMap.put("username", username);
                    hashMap.put("bio", bio);
                    hashMap.put("home", home);
                    hashMap.put("work", work);
                    hashMap.put("email", email);
                    hashMap.put("userImage", userImage);
                    hashMap.put("lastPlace", lastPlace);

                    userData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return userData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class FollowUserAsync extends AsyncTask<Void,Void,Commons>{
        int userID;
        UrlCallBack urlCallBack;
        int postedByID,groupID;
        String result;
        Commons commons;
        Boolean status;
        JSONArray jsonArray;
        JSONObject jsonObject;

        public FollowUserAsync(int userID, int postedByID,int groupID,UrlCallBack urlCallBack) {
            this.userID = userID;
            this.postedByID = postedByID;
            this.groupID = groupID;
            this.urlCallBack = urlCallBack;
            result = null;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("postedBy_id",Integer.toString(postedByID)));
            dataToSend.add(new BasicNameValuePair("group_id",Integer.toString(groupID)));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/followUser.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                jsonArray = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(jsonArray.length()));

                for(int i = 0; i<jsonArray.length();i++){

                    JSONObject c = jsonArray.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            //Log.i("Post Execute:",Boolean.toString(c.getStatus()));
            super.onPostExecute(c);
        }
    }

    public class LikeUserPostAsync extends AsyncTask<Void,Void,Commons>{
        UrlCallBack urlCallBack;
        int userID;
        String result;
        Boolean status;
        int totalLikes;
        JSONArray userData;
        int contentID;
        Commons commons;


        public LikeUserPostAsync(int userID,int contentID,UrlCallBack urlCallBack){
            this.userID = userID;
            this.contentID = contentID;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("content_id",Integer.toString(contentID)));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/likeUserPost.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.d("status result:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                    totalLikes = Integer.parseInt(c.getString("totalLikes"));
                    Log.i("totalLikes:", Integer.toString(totalLikes));
                }
                commons = new Commons(status,totalLikes);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class BucketlistUserPostAsync extends AsyncTask<Void,Void,Commons>{
        UrlCallBack urlCallBack;
        int userID;
        String result;
        int totalBucketlist;
        Boolean status;
        JSONArray userData;
        int contentID;
        Commons commons;


        public BucketlistUserPostAsync(int userID,int contentID,UrlCallBack urlCallBack){
            this.userID = userID;
            this.contentID = contentID;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("content_id",Integer.toString(contentID)));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/bucketlistUserPost.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                    totalBucketlist = Integer.parseInt(c.getString("totalBucketlist"));
                }
                commons = new Commons(status,totalBucketlist);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class DeleteUserPostAsync extends AsyncTask<Void,Void,Commons>{
        UrlCallBack urlCallBack;
        int userID;
        String result;
        Boolean status;
        JSONArray userData;
        int contentID;
        Commons commons;


        public DeleteUserPostAsync(int userID,int contentID,UrlCallBack urlCallBack){
            this.userID = userID;
            this.contentID = contentID;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("content_id",Integer.toString(contentID)));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/deleteUserPost.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class CommentOnPostAsync extends AsyncTask<Void,Void,Commons>{
        UrlCallBack urlCallBack;
        int userID;
        String result;
        Boolean status;
        JSONArray userData;
        int contentID;
        Commons commons;
        String comment;


        public CommentOnPostAsync(int userID,int contentID,String comment, UrlCallBack urlCallBack){
            this.userID = userID;
            this.contentID = contentID;
            this.urlCallBack = urlCallBack;
            this.comment = comment;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("content_id",Integer.toString(contentID)));
            dataToSend.add(new BasicNameValuePair("comment",comment));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/commentOnPost.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class FetchCommentsOnPostAsync extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>{
        int contentID;
        UrlCallBack urlCallBack;
        String result;
        JSONArray jsonArray;
        JSONObject jsonObject;
        ArrayList<HashMap<String,String>> userData;
        public FetchCommentsOnPostAsync(int contentID,UrlCallBack urlCallBack) {
            this.contentID = contentID;
            this.urlCallBack = urlCallBack;
            this.result = null;
            userData = new ArrayList<HashMap<String,String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("content_id",Integer.toString(contentID)));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"/fetchCommentsOnPost.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.i("Result:",result);

                jsonObject = new JSONObject(result);

                JSONArray contents = jsonObject.getJSONArray("contents");
                JSONArray users = jsonObject.getJSONArray("users");

                Log.i("Array Length:",Integer.toString(contents.length()));
                Log.i("Array Length:",Integer.toString(users.length()));

                for(int i = 0; i<contents.length();i++){
                    JSONObject c = contents.getJSONObject(i);
                    JSONObject b = users.getJSONObject(i);

                    String commentID = c.getString("comment_id");
                    String username = c.getString("username");
                    String userImage = c.getString("userImage");
                    String comment = c.getString("comment");
                    String timeAgo = c.getString("timeAgo");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("comment_id", commentID);
                    hashMap.put("username", username);
                    hashMap.put("userImage", userImage);
                    hashMap.put("comment", comment);
                    hashMap.put("timeAgo", timeAgo);

                    userData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return userData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class LoginSocialMediaUsersAsync extends AsyncTask<Void,Void,User>{
        UrlCallBack urlCallBack;
        User user,returnedUser;
        String appName;

        public LoginSocialMediaUsersAsync(User user,String appName, UrlCallBack urlCallBack) {
            this.urlCallBack = urlCallBack;
            this.user = user;
            this.appName = appName;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("social_user_id",Long.toString(user.socialUserID)));
            dataToSend.add(new BasicNameValuePair("username",user.username));
            dataToSend.add(new BasicNameValuePair("email",user.email));
            dataToSend.add(new BasicNameValuePair("app",appName));
            dataToSend.add(new BasicNameValuePair("userImage",user.userImageUrl));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserSocialData.php");
            HttpClient httpClient = new DefaultHttpClient(httpParams);

            returnedUser = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);
                Log.d("Result:",result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray content = jsonObject.getJSONArray("usersData");

                if(jsonObject.length() == 0){
                    returnedUser = null;
                }else{
                    JSONObject c = content.getJSONObject(0);
                    int userID = c.getInt("user_id");
                    String username = c.getString("username");
                    String email = c.getString("email");
                    String userImageUrl = c.getString("userImageUrl");
                    String appName = c.getString("app_name");
                    Long socialUserID = c.getLong("social_user_id");

                    returnedUser = new User(userID, username, email,userImageUrl,appName, socialUserID);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User user) {
            urlCallBack.done(user);
            super.onPostExecute(user);
        }
    }

    public class RegisterUserInfoAsync extends AsyncTask<Void,Void,Commons>{
        UrlCallBack urlCallBack;
        int userID;
        String result;
        Boolean status;
        JSONArray userData;
        Commons commons;
        String home,work,bio,homecountry,workcountry;


        public RegisterUserInfoAsync(int userID,String home,String work,String bio,String homecountry,
                                     String workcountry, UrlCallBack urlCallBack){
            this.userID = userID;
            this.home = home;
            this.work = work;
            this.bio = bio;
            this.homecountry = homecountry;
            this.workcountry = workcountry;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("home",home));
            dataToSend.add(new BasicNameValuePair("homecountry",homecountry));
            dataToSend.add(new BasicNameValuePair("workcountry",workcountry));
            dataToSend.add(new BasicNameValuePair("work",work));
            dataToSend.add(new BasicNameValuePair("bio",bio));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/registerMoreUserInfo.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class FetchUserGroupsDataAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String,String>>>{
        int userID,groupID;
        UrlCallBack urlCallBack;
        ArrayList<HashMap<String,String>> content;
        String result;
        JSONArray userData;

        public FetchUserGroupsDataAsync(int userID, int groupID, UrlCallBack urlCallBack) {
            this.userID = userID;
            this.urlCallBack = urlCallBack;
            this.groupID = groupID;
            this.result = null;
            this.content = new ArrayList<HashMap<String,String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("group_id",Integer.toString(groupID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserGroupData.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }

                JSONArray following = jsonObject.getJSONArray("following");
                JSONArray userFollowing = jsonObject.getJSONArray("userFollowing");
                JSONArray latestPost = jsonObject.getJSONArray("latestPost");

                Log.i("Status array size:", Integer.toString(following.length()));

                for(int i = 0; i<following.length();i++){

                    JSONObject c = following.getJSONObject(i);
                    JSONObject b = userFollowing.getJSONObject(i);
                    JSONObject d = latestPost.getJSONObject(i);

                    String user_following_id = c.getString("user_following_id");
                    String group = c.getString("group");
                    String username = b.getString("username");
                    String bio = b.getString("bio");
                    String home = b.getString("home");
                    String work = b.getString("work");
                    String email = b.getString("email");
                    String userImage = b.getString("user_image");
                    String lastPlace = d.getString("lastPlace");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("user_followers_id", user_following_id);
                    hashMap.put("group", group);
                    hashMap.put("username", username);
                    hashMap.put("bio", bio);
                    hashMap.put("home", home);
                    hashMap.put("work", work);
                    hashMap.put("email", email);
                    hashMap.put("userImage", userImage);
                    hashMap.put("lastPlace", lastPlace);

                    content.add(hashMap);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class FetchUserGroupsAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String,String>>>{
        int userID;
        UrlCallBack urlCallBack;
        ArrayList<HashMap<String,String>> content;
        String result;
        JSONArray userData;

        public FetchUserGroupsAsync(int userID, UrlCallBack urlCallBack) {
            this.userID = userID;
            this.urlCallBack = urlCallBack;
            this.result = null;
            this.content = new ArrayList<HashMap<String,String>>();
            this.userData = null;
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserGroups.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }

                JSONArray userData = jsonObject.getJSONArray("groups");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);

                    String groupID = c.getString("groupID");
                    String groupName = c.getString("groupName");
                    String totalGroupFollowing = c.getString("totalGroupFollowing");
                    String groupDescription = c.getString("groupDescription");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("groupID", groupID);
                    hashMap.put("groupName", groupName);
                    hashMap.put("totalGroupFollowing", totalGroupFollowing);
                    hashMap.put("groupDescription", groupDescription);

                    content.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class CreateUserTourAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String, String>>>{
        int userID;
        String placeName, tourName, country, date;
        UrlCallBack urlCallBack;
        String result;
        ArrayList<HashMap<String, String>> content;

        public CreateUserTourAsync(int userID, String tourName, String placeName, String country, String date, UrlCallBack urlCallBack) {
            this.userID = userID;
            this.placeName = placeName;
            this.tourName = tourName;
            this.country = country;
            this.date = date;
            this.urlCallBack = urlCallBack;
            content = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("tourName",tourName));
            dataToSend.add(new BasicNameValuePair("placeName",placeName));
            dataToSend.add(new BasicNameValuePair("country",country));
            dataToSend.add(new BasicNameValuePair("date",date));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/createTour.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }

                JSONArray userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);

                    String tourID = c.getString("tourID");
                    String tourName = c.getString("tourName");
                    String place = c.getString("place");
                    String city = c.getString("city");
                    String countryN = c.getString("country");
                    String tourDate = c.getString("tourDate");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("tourID", tourID);
                    hashMap.put("tourName", tourName);
                    hashMap.put("place", place);
                    hashMap.put("city", city);
                    hashMap.put("country", countryN);
                    hashMap.put("tourDate", tourDate);

                    content.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class FetchUserToursAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String, String>>>{
        int userID;
        UrlCallBack urlCallBack;
        String result;
        ArrayList<HashMap<String, String>> content;

        public FetchUserToursAsync(int userID, UrlCallBack urlCallBack) {
            this.userID = userID;
            this.urlCallBack = urlCallBack;
            content = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchUserTours.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }

                JSONArray userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);

                    String tourID = c.getString("tourID");
                    String tourImage = c.getString("tourImage");
                    String tourName = c.getString("tourName");
                    String place = c.getString("place");
                    String city = c.getString("city");
                    String countryN = c.getString("country");
                    String tourDate = c.getString("tourDate");

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("tourID", tourID);
                    hashMap.put("tourImage", tourImage);
                    hashMap.put("tourName", tourName);
                    hashMap.put("place", place);
                    hashMap.put("city", city);
                    hashMap.put("country", countryN);
                    hashMap.put("tourDate", tourDate);

                    content.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class FetchSelectedUserToursContentAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String, String>>>{
        int userID,tourID;
        UrlCallBack urlCallBack;
        String result;
        ArrayList<HashMap<String, String>> content;
        JSONArray userContents;

        public FetchSelectedUserToursContentAsync(int userID, int tourID, UrlCallBack urlCallBack) {
            this.userID = userID;
            this.tourID = tourID;
            this.urlCallBack = urlCallBack;
            content = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("tour_id",Integer.toString(tourID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchSelectedUserToursContent.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }
                userContents = jsonObject.getJSONArray("contents");
                JSONArray usersArray = jsonObject.getJSONArray("users");

                Log.i("Array length:", Integer.toString(userContents.length()));

                for(int i = 0; i<userContents.length();i++){

                    JSONObject c = userContents.getJSONObject(i);
                    JSONObject b = usersArray.getJSONObject(i);

                    String contentID = c.getString("content_id");
                    String userID = c.getString("user_id");
                    String tourID = c.getString("tour_id");
                    String postedBy = c.getString("postedBy");
                    String userImg = c.getString("userImage");
                    String place = c.getString("place");
                    String imageUrl = c.getString("imageUrl");
                    String about = c.getString("about");
                    String relation = c.getString("relation");
                    String totalLikes = c.getString("totalLikes");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String timeAgo = c.getString("timeAgo");
                    String username = b.getString("username");
                    String bio = b.getString("bio");
                    String home = b.getString("home");
                    String work = b.getString("work");
                    String email = b.getString("email");
                    String userImage = b.getString("user_image");

                    Log.i("postedBy:", postedBy);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("content_id",contentID);
                    hashMap.put("user_id",userID);
                    hashMap.put("tour_id",tourID);
                    hashMap.put("postedBy",postedBy);
                    hashMap.put("userImage",userImg);
                    hashMap.put("place",place);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("about",about);
                    hashMap.put("relation",relation);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("timeAgo",timeAgo);
                    hashMap.put("username", username);
                    hashMap.put("bio", bio);
                    hashMap.put("home", home);
                    hashMap.put("work", work);
                    hashMap.put("email", email);
                    hashMap.put("userImage", userImage);

                    content.add(hashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class UploadProfilePhotoAsync extends AsyncTask<Void, Void, Commons>{
        Bitmap image;
        int userID;
        UrlCallBack urlCallBack;
        String result;
        Commons commons;
        JSONArray userData;
        boolean status;

        public UploadProfilePhotoAsync(int userID, Bitmap image, UrlCallBack urlCallBack) {
            this.image = image;
            this.userID = userID;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            // Log.i("String Image:",encodedImage);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("image",encodedImage));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/uploadProfilePhoto.php");
            Log.i("Server:",httpPost.toString());

            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("Result:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class FetchUserCurrentLocationAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String, String>>>{
        String latitude,longitude;
        UrlCallBack urlCallBack;
        String result;
        ArrayList<HashMap<String, String>> content;
        JSONArray userContents;

        public FetchUserCurrentLocationAsync(String latitude,String longitude, UrlCallBack urlCallBack) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.urlCallBack = urlCallBack;
            content = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("latitude",latitude));
            dataToSend.add(new BasicNameValuePair("longitude",longitude));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/reverseGeoCoding.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }
                userContents = jsonObject.getJSONArray("contents");

                Log.i("Array length:", Integer.toString(userContents.length()));

                for(int i = 0; i<userContents.length();i++){

                    JSONObject c = userContents.getJSONObject(i);

                    String location = c.getString("location");

                    Log.i("location:", location);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("location",location);

                    content.add(hashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class ChangeTourStatusClosedAsync extends AsyncTask<Void,Void,Commons>{
        int userID, tourID;
        UrlCallBack urlCallBack;
        String result;
        Commons commons;
        Boolean status;
        JSONArray jsonArray;
        JSONObject jsonObject;

        public ChangeTourStatusClosedAsync(int userID, int tourID,UrlCallBack urlCallBack) {
            this.userID = userID;
            this.tourID = tourID;
            this.urlCallBack = urlCallBack;
            result = null;
        }

        @Override
        protected Commons doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));
            dataToSend.add(new BasicNameValuePair("tour_id",Integer.toString(tourID)));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/changeTourStatus.php");
            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                jsonArray = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(jsonArray.length()));

                for(int i = 0; i<jsonArray.length();i++){

                    JSONObject c = jsonArray.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            //Log.i("Post Execute:",Boolean.toString(c.getStatus()));
            super.onPostExecute(c);
        }
    }

    public class ReportIncidentAsync extends AsyncTask<Void, Void, Commons>{
        int contentID,userID;
        String item, type;
        UrlCallBack urlCallBack;
        String result;
        Commons commons;
        JSONArray userData;
        boolean status;

        public ReportIncidentAsync(int contentID, String item, int userID, String type, UrlCallBack urlCallBack) {
            this.contentID = contentID;
            this.item = item;
            this.type = type;
            this.userID = userID;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("content_id",Integer.toString(contentID)));
            dataToSend.add(new BasicNameValuePair("incident",item));
            dataToSend.add(new BasicNameValuePair("type",type));
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/reportIncident.php");
            Log.i("Server:",httpPost.toString());

            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("Result:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class SendEmailForPasswordAsync extends AsyncTask<Void, Void, Commons>{
        String email;
        UrlCallBack urlCallBack;
        String result;
        Commons commons;
        JSONArray userData;
        boolean status;

        public SendEmailForPasswordAsync(String email, UrlCallBack urlCallBack) {
            this.email = email;
            this.urlCallBack = urlCallBack;
        }

        @Override
        protected Commons doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email",email));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/forgotPassword.php");
            Log.i("Server:",httpPost.toString());

            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("Result:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                }
                commons = new Commons(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return commons;
        }

        @Override
        protected void onPostExecute(Commons c) {
            urlCallBack.done(null,c);
            super.onPostExecute(c);
        }
    }

    public class FetchLocationUserContentsAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String,String>>>{
        UrlCallBack urlCallBack;
        String latitude;
        String longitude;
        String result;
        JSONArray userData;
        ArrayList<HashMap<String,String>> content;
        JSONArray userContents;

        public FetchLocationUserContentsAsync(String latitude,String longitude, UrlCallBack urlCallBack) {
            this.urlCallBack = urlCallBack;
            this.latitude = latitude;
            this.longitude = longitude;
            content = new ArrayList<HashMap<String,String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("latitude",latitude));
            dataToSend.add(new BasicNameValuePair("longitude",longitude));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchLocationUserContents.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }
                userContents = jsonObject.getJSONArray("contents");

                Log.i("Array length:", Integer.toString(userContents.length()));

                for(int i = 0; i<userContents.length();i++){

                    JSONObject c = userContents.getJSONObject(i);

                    String place = c.getString("place");
                    String distance = c.getString("distance");
                    String postedBy = c.getString("postedBy");
                    String userImage = c.getString("userImage");
                    String imageUrl = c.getString("imageUrl");
                    String totalLikes = c.getString("totalLikes");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String timeAgo = c.getString("timeAgo");

                    Log.i("location:", place);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("place",place);
                    hashMap.put("distance",distance);
                    hashMap.put("postedBy",postedBy);
                    hashMap.put("userImage",userImage);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("timeAgo",timeAgo);

                    content.add(hashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    /* This class is not in use*/
    public class SearchPlacesAsync extends AsyncTask<Void,Void,User>{
            UrlCallBack urlCallBack;
            String result;
            User user;
            String searchItem;
            JSONArray userContents;
            ArrayList<HashMap<String,String>> dbUserContents;

            public SearchPlacesAsync(String searchItem,UrlCallBack urlCallBack) {
                this.urlCallBack = urlCallBack;
                this.searchItem = searchItem;
                this.result = null;
                dbUserContents = new ArrayList<HashMap<String, String>>();
            }

            @Override
            protected User doInBackground(Void... params) {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("item",searchItem));

                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

                HttpClient httpClient = new DefaultHttpClient(httpParams);
                HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/searchPlaces.php");
                user = null;
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    result = EntityUtils.toString(httpEntity);

                    Log.i("RESULT:", result);

                    JSONObject jsonObject = new JSONObject(result);

                    if(jsonObject.length() == 0){
                        user = null;
                    }
                    userContents = jsonObject.getJSONArray("contents");
                    JSONArray usersArray = jsonObject.getJSONArray("users");

                    Log.i("Array length:", Integer.toString(userContents.length()));

                    for(int i = 0; i<userContents.length();i++){

                        JSONObject c = userContents.getJSONObject(i);
                        JSONObject b = usersArray.getJSONObject(i);

                        String contentID = c.getString("content_id");
                        String userID = c.getString("user_id");
                        String tourID = c.getString("tour_id");
                        String tourStatus = c.getString("tour_status");
                        String postedBy = c.getString("postedBy");
                        String userImg = c.getString("userImage");
                        String place = c.getString("place");
                        String imageUrl = c.getString("imageUrl");
                        String about = c.getString("about");
                        String relation = c.getString("relation");
                        String totalLikes = c.getString("totalLikes");
                        String totalBucketlist = c.getString("totalBucketlist");
                        String timeAgo = c.getString("timeAgo");
                        String username = b.getString("username");
                        String bio = b.getString("bio");
                        String home = b.getString("home");
                        String work = b.getString("work");
                        String email = b.getString("email");
                        String userImage = b.getString("user_image");

                        Log.i("postedBy:", postedBy);

                        HashMap<String,String> hashMap = new HashMap<String,String>();
                        hashMap.put("content_id",contentID);
                        hashMap.put("user_id",userID);
                        hashMap.put("tour_id",tourID);
                        hashMap.put("tourStatus",tourStatus);
                        hashMap.put("postedBy",postedBy);
                        hashMap.put("userImage",userImg);
                        hashMap.put("place",place);
                        hashMap.put("imageUrl",imageUrl);
                        hashMap.put("about",about);
                        hashMap.put("relation",relation);
                        hashMap.put("totalLikes",totalLikes);
                        hashMap.put("totalBucketlist",totalBucketlist);
                        hashMap.put("timeAgo",timeAgo);
                        hashMap.put("username", username);
                        hashMap.put("bio", bio);
                        hashMap.put("home", home);
                        hashMap.put("work", work);
                        hashMap.put("email", email);
                        hashMap.put("userImage", userImage);

                        dbUserContents.add(hashMap);
                    }
                    user = new User(dbUserContents);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                urlCallBack.done(user);
                super.onPostExecute(user);
            }
        }

    public class FetchFavoritedContentsAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String,String>>>{
        //int userID;
        UrlCallBack urlCallBack;
        String result;
        JSONArray userData;
        ArrayList<HashMap<String,String>> dbUserData, dataReceived;
        User user;
        Commons ef;

        public FetchFavoritedContentsAsync(User user, UrlCallBack urlCallBack) {
            this.user = user;
            this.urlCallBack = urlCallBack;
            dbUserData = new ArrayList<HashMap<String, String>>();
            dataReceived = new ArrayList<HashMap<String, String>>();
            this.result = null;

        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));
            //Log.i("userID:", Integer.toString(user.userID));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/fetchFavoritedContents.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    user = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.i("Array length:", Integer.toString(userData.length()));

                ef = new Commons(userData.length());

                for(int i = 0; i<userData.length();i++) {

                    JSONObject c = userData.getJSONObject(i);
                    String username = c.getString("username");
                    String userImg = c.getString("userImage");
                    String place = c.getString("place");
                    String imageUrl = c.getString("imageUrl");
                    String totalLikes = c.getString("totalLikes");
                    String totalBucketlist = c.getString("totalBucketlist");
                    String timeAgo = c.getString("timeAgo");

                    Log.i("fav username:", username);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("username",username);
                    hashMap.put("userImage",userImg);
                    hashMap.put("place",place);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("totalLikes",totalLikes);
                    hashMap.put("totalBucketlist",totalBucketlist);
                    hashMap.put("timeAgo",timeAgo);

                    dbUserData.add(hashMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return dbUserData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

    public class VideoUploadAsync extends AsyncTask<Void,Void,Boolean>{
        String videoPath;
        UrlCallBack urlCallBack;
        Bitmap image;
        String placeName;
        String country;
        String about;
        User user;
        String result, resultStatus;
        int tourID;
        Commons commons;
        JSONArray userData;
        boolean status;

        public VideoUploadAsync(User user,int tourID,String videoPath,String placeName, String country,String about,
                           UrlCallBack urlCallBack) {
            this.videoPath = videoPath;
            this.urlCallBack = urlCallBack;
            this.image = image;
            this.placeName = placeName;
            this.country = country;
            this.about = about;
            this.user = user;
            this.tourID = tourID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(user.userID)));
            dataToSend.add(new BasicNameValuePair("tour_id",Integer.toString(tourID)));
            dataToSend.add(new BasicNameValuePair("image","null"));
            dataToSend.add(new BasicNameValuePair("place",placeName));
            dataToSend.add(new BasicNameValuePair("country",country));
            dataToSend.add(new BasicNameValuePair("about",about));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/postVideoMarks.php");
            Log.i("Server:",httpPost.toString());

            commons = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("Result:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    commons = null;
                }
                userData = jsonObject.getJSONArray("contents");

                Log.d("Status array size:", Integer.toString(userData.length()));

                for(int i = 0; i<userData.length();i++){

                    JSONObject c = userData.getJSONObject(i);
                    status = Boolean.parseBoolean(c.getString("status"));
                    Log.d("statusp",Boolean.toString(status));
                }
                if(status == true){
                    Upload upload = new Upload();
                    resultStatus = upload.uploadVideo(videoPath);
                    Log.d("resultStatus",resultStatus);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(resultStatus.equals("true")){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            urlCallBack.done(b);
            super.onPostExecute(b);
        }
    }

    public class Upload {

        public static final String UPLOAD_URL= SERVER_ADDRESS+"/uploadVideo.php";

        private int serverResponseCode;

        public String uploadVideo(String file) {

            String fileName = file;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            File sourceFile = new File(file);
            if (!sourceFile.isFile()) {
                Log.e("File", "Source File Does not exist");
                return null;
            }

            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(UPLOAD_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("myFile", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                Log.i("Huzza", "Initial .available : " + bytesAvailable);

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();

                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (serverResponseCode == 200) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                            .getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                } catch (IOException ioex) {
                }
                return sb.toString();
            }else {
                return "failed";
            }
        }
    }

    public class PostMarkForNewLocationIsAsync extends AsyncTask<Void,Void,ArrayList<HashMap<String, String>>>{
        String latitude,longitude;
        int userID;
        UrlCallBack urlCallBack;
        String result;
        ArrayList<HashMap<String, String>> content;
        JSONArray userContents;

        public PostMarkForNewLocationIsAsync(int userID,String latitude,String longitude, UrlCallBack urlCallBack) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.userID = userID;
            this.urlCallBack = urlCallBack;
            content = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("latitude",latitude));
            dataToSend.add(new BasicNameValuePair("longitude",longitude));
            dataToSend.add(new BasicNameValuePair("user_id",Integer.toString(userID)));

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS+"/postMarkForNewLocation.php");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.i("RESULT:", result);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    content = null;
                }
                userContents = jsonObject.getJSONArray("contents");

                Log.i("Array length:", Integer.toString(userContents.length()));

                for(int i = 0; i<userContents.length();i++){

                    JSONObject c = userContents.getJSONObject(i);

                    String status = c.getString("status");
                    String location = c.getString("location");

                    Log.i("location:", location);

                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("status",status);
                    hashMap.put("location",location);

                    content.add(hashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) {
            urlCallBack.done(arrayList);
            super.onPostExecute(arrayList);
        }
    }

}

 private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
