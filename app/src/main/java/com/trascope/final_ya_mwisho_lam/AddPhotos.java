package com.trascope.final_ya_mwisho_lam;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;


public class AddPhotos extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    private static final int SELECT_VIDEO = 3;
    ImageView imageSelected;
    VideoView videoSelected;
    String videoPath;
    Button btnChange,btnProceed;
    ImageButton btnBack;
    Bitmap currentImg;
    String place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_add_photos);

        Intent intent = getIntent();
        place = intent.getStringExtra("place");

        imageSelected = (ImageView) findViewById(R.id.imageSelected);
        videoSelected = (VideoView) findViewById(R.id.videoSelected);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnChange.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        selectImage();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChange:
                selectImage();
                break;
            case R.id.btnProceed:
                Intent intent = new Intent(this, Create.class);
                Bitmap map = getCurrentImg();
                if(map != null){
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    map.compress(Bitmap.CompressFormat.PNG,0,bs);
                    intent.putExtra("bitmap",bs.toByteArray());
                    if(place != null){
                        intent.putExtra("place",place);
                    }
                    startActivity(intent);
                }else{
                    if(videoPath.length()>0){
                        Log.d("Video status", "ok");
                        intent.putExtra("video",videoPath);
                        if(place != null){
                            intent.putExtra("place",place);
                        }
                        startActivity(intent);
                    }
                }
                break;
            case R.id.btnBack:
                finish();
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
                imageSelected.setImageBitmap(thumbnail);
                setCurrentImg(thumbnail);
                state = true;
            }else if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                videoPath = getPath(data.getData());
                videoSelected.setVisibility(View.VISIBLE);
                videoSelected.setVideoPath(videoPath);
                videoSelected.requestFocus();
                videoSelected.start();
            } else if (requestCode == SELECT_FILE) {
                String selectedImageUri = getAbsolutePath(data.getData());
               // startActivity(new Intent(this, Create.class));
                imageSelected.setImageBitmap(decodeFile(selectedImageUri));
                setCurrentImg(decodeFile(selectedImageUri));
                state = true;
            }
        }

        if(state == true){
            Drawable drawable = imageSelected.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            if(bitmap == null){
                btnChange.setText("Add Photo");
            }else{
                btnChange.setText("Change Photo");
            }
        }else{
            if(videoPath == null){
                btnChange.setText("Add Photo");
            }else{
                btnChange.setText("Change");
            }
        }
    }

    public Bitmap getCurrentImg() {
        return currentImg;
    }

    public void setCurrentImg(Bitmap currentImg) {
        this.currentImg = currentImg;
    }


    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Choose Video","Cancel" };
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
                }else if (items[item].equals("Choose Video")) {
                    chooseVideo();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
