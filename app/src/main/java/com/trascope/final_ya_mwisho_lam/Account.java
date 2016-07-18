package com.trascope.final_ya_mwisho_lam;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;


public class Account extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etEmail,textHome,textWork,textBio;
    AutoCompleteTextView homeCountry,workCountry;
    Button changePwdBtn,saveBtn,changeProfPicBtn;
    ImageView profileImg;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Bitmap currentImg;
    Commons commons;
    LocalUserStorage localUserStorage;
    User localUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_account);
        //Accessing old data
        etName = (EditText) findViewById(R.id.name);
        etEmail = (EditText) findViewById(R.id.email);
        textHome = (EditText) findViewById(R.id.textHome);
        textWork = (EditText) findViewById(R.id.textWork);
        textBio = (EditText) findViewById(R.id.textBio);
        homeCountry = (AutoCompleteTextView) findViewById(R.id.homeCountry);
        workCountry = (AutoCompleteTextView) findViewById(R.id.workCountry);
        profileImg = (ImageView) findViewById(R.id.profileImg);
        changeProfPicBtn = (Button) findViewById(R.id.changeProfPicBtn);
        changePwdBtn = (Button) findViewById(R.id.changePwdBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        commons = new Commons();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ImageView logoIcon = (ImageView) findViewById(R.id.logoIcon);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });

        localUserStorage = new LocalUserStorage(this);
        localUser = localUserStorage.getLoggedInUser();
        etEmail.setText(localUser.email);
        etName.setText(localUser.username);
        textHome.setText(localUser.home);
        textWork.setText(localUser.work);
        textBio.setText(localUser.bio);

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
        homeCountry.setAdapter(arrayAdapter);
        workCountry.setAdapter(arrayAdapter);

        homeCountry.setText(localUser.countryHome);
        workCountry.setText(localUser.countryWork);
        if(localUser.userImageUrl != null){
            progressBar.setVisibility(View.GONE);
            Picasso.with(this).load(localUser.userImageUrl).transform(new CircleTransform()).into(profileImg);
            Log.d("ImageUrl:",localUser.userImageUrl);
        }

        //Saving new data
        changePwdBtn.setOnClickListener(this);
        changeProfPicBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.changeProfPicBtn:
                changeProfilePic();
                break;
            case R.id.changePwdBtn:
                changePasswordForm();
                break;
            case R.id.saveBtn:
                Bitmap userImage = getCurrentImg();
                if(userImage != null){
                    Log.i("Image status","Image changed");
                }else{
                    userImage = ((BitmapDrawable)profileImg.getDrawable()).getBitmap();
                    Log.i("Image status","Image not changed ");
                }
                saveAccountDetails(userImage);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                profileImg.setImageBitmap(thumbnail);
                setCurrentImg(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                String selectedImageUri = getAbsolutePath(data.getData());
                profileImg.setImageBitmap(decodeFile(selectedImageUri));
                setCurrentImg(decodeFile(selectedImageUri));
            }
        }
    }

    public Bitmap getCurrentImg() {
        return currentImg;
    }

    public void setCurrentImg(Bitmap currentImg) {
        this.currentImg = currentImg;
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 160;

            // Find the correct scale value. It should be the power of
            // 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getAbsolutePath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void saveAccountDetails(Bitmap bitmapImage) {
        final String name,email,home,work,bio,stCountryHome,stCountryWork;

        boolean network = commons.checkInternetConnetion(this);

        name = etName.getText().toString();
        email = etEmail.getText().toString();
        home = textHome.getText().toString();
        work = textWork.getText().toString();
        bio = textBio.getText().toString();
        stCountryHome = homeCountry.getText().toString();
        stCountryWork = workCountry.getText().toString();

       // if(network == true){
            User user = new User(localUser.userID,bitmapImage,name,email,home,work,bio,stCountryHome,stCountryWork);
            ServerRequest serverRequest = new ServerRequest(this);
            serverRequest.changeUserDetails(user,new UrlCallBack() {
                @Override
                public void done(User user) {
                    if(user == null){
                        Log.i("Mwisho:","0");
                    }else{
                        localUserStorage.storeUserDataDetails(user.contentList);
                        Log.i("Mwisho:","1");
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

       // }else{
           // Toast.makeText(this, "Internet is not connected",Toast.LENGTH_SHORT).show();
       // }

    }

    public void changeProfilePic(){
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void changePasswordForm(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_password);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button changePwdBtn = (Button) dialog.findViewById(R.id.changePwdBtn);
        dialog.setTitle("Change your passowrd");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home){
            startActivity(new Intent(this, Home.class));
        }

        if(id == R.id.action_create){
            // startActivity(new Intent(this, Create.class));
            startActivity(new Intent(this, AddPhotos.class));
        }

        if(id == R.id.action_search){
            startActivity(new Intent(this, Search.class));
        }

        if(id == R.id.action_friends){
            startActivity(new Intent(this, Friends.class));
        }

        if(id == R.id.action_account){
            startActivity(new Intent(this,Account.class));
        }

        if(id == R.id.action_favorites){
            //startActivity(new Intent(this, Favorite.class));
        }

        if(id == R.id.action_invite_friends){
            startActivity(new Intent(this, InviteFriends.class));
        }

        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_signout){
            LocalUserStorage localUserStorage = new LocalUserStorage(this);
            localUserStorage.clearUserData();
            if(localUserStorage.getUserLogged() == false){
                startActivity(new Intent(this,MainActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    
}
