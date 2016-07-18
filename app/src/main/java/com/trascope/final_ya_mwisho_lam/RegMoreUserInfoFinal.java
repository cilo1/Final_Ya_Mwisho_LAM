package com.trascope.final_ya_mwisho_lam;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class RegMoreUserInfoFinal extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    Button selectBtn,btnProceed;
    ProgressBar progressBar;
    ImageView profileImg;
    String email,userID;
    Bitmap currentImg;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_reg_more_user_info_final);

        Intent userIDIntent = getIntent();
        userID = userIDIntent.getStringExtra("user_id");
        email = userIDIntent.getStringExtra("email");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.title);

        selectBtn = (Button) findViewById(R.id.selectBtn);
        btnProceed = (Button) findViewById(R.id.btnProceed);

        profileImg = (ImageView) findViewById(R.id.profileImg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        selectBtn.setOnClickListener(this);
        btnProceed.setOnClickListener(this);

        toolbarTitle.setText("Final step and its done");
        setSupportActionBar(toolbar);

        serverRequest = new ServerRequest(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.selectBtn:
                selectImage();
                break;
            case R.id.btnProceed:
                progressBar.setVisibility(View.VISIBLE);
                Bitmap image = null;
                if(getCurrentImg() == null){
                    Drawable drawable = profileImg.getDrawable();
                    image = ((BitmapDrawable) drawable).getBitmap();
                }else {
                    image = getCurrentImg();
                }
                serverRequest.updateProfilePhoto(Integer.parseInt(userID),image,new UrlCallBack() {
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
                            Log.d("Photo Upload","No item returned");
                            Toast.makeText(getApplicationContext(),"No item returned",Toast.LENGTH_LONG).show();
                        }else{
                            if(ef.status == true){
                                Intent intent = new Intent(getApplicationContext(),HowItWorks.class);
                                intent.putExtra("register","register");
                                intent.putExtra("email",email);
                                startActivity(intent);
                            }else{
                                Log.d("Photo Upload","Failed to upload");
                                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean state = false;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //startActivity(new Intent(this, Create.class));
                profileImg.setImageBitmap(thumbnail);
                setCurrentImg(thumbnail);
                state = true;
            } else if (requestCode == SELECT_FILE) {
                String selectedImageUri = getAbsolutePath(data.getData());
                // startActivity(new Intent(this, Create.class));
                profileImg.setImageBitmap(decodeFile(selectedImageUri));
                setCurrentImg(decodeFile(selectedImageUri));
                state = true;
            }
        }

        if(state == true){
            Drawable drawable = profileImg.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            if(bitmap == null){
                selectBtn.setText("Select photo");
            }else{
                selectBtn.setText("Change Photo");
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public Bitmap getCurrentImg() {
        return currentImg;
    }

    public void setCurrentImg(Bitmap currentImg) {
        this.currentImg = currentImg;
    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
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
}
