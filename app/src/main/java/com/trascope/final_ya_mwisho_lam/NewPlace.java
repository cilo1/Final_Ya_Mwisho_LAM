package com.trascope.final_ya_mwisho_lam;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by cmigayi on 26-Oct-15.
 */
public class NewPlace extends Fragment implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    Button btnSetDate, postBtn,btnChangePhoto;
    AutoCompleteTextView autoCountry;
    static TextView datePicked;
    EditText etPlace, etAboutPhoto,etCity;
    ImageView imagePlace;
    VideoView videoPlace;
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    String countryName, place, aboutPhoto;
    Uri selectedImage;
    Bitmap currentImg;
    Spinner visitSpinner;
    String[] visits = {"1st Visit","2rd Visit","3rd Visit","4th Visit","5th Visit","Frequent","Home","Work"};
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_place, container, false);

        bundle = this.getArguments();

        etPlace = (EditText) view.findViewById(R.id.etPlace);
        etCity = (EditText) view.findViewById(R.id.etCity);
        etAboutPhoto = (EditText) view.findViewById(R.id.aboutPhoto);
        btnSetDate = (Button) view.findViewById(R.id.btnSetDate);
        postBtn = (Button) view.findViewById(R.id.postBtn);
        btnChangePhoto = (Button) view.findViewById(R.id.btnChangePhoto);
        imagePlace = (ImageView) view.findViewById(R.id.imagePlace);
        videoPlace = (VideoView) view.findViewById(R.id.videoPlace);
        datePicked = (TextView) view.findViewById(R.id.datePicked);
        visitSpinner = (Spinner) view.findViewById(R.id.visitSpinner);

        ArrayAdapter visitAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,
                visits);
        visitSpinner.setAdapter(visitAdapter);

        if(getCurrentImg() == null){
            imagePlace.setVisibility(View.GONE);
            videoPlace.setVisibility(View.VISIBLE);
            videoPlace.setVideoPath(getCurrentVideo());
            videoPlace.requestFocus();
            videoPlace.start();
        }else{
            imagePlace.setImageBitmap(getCurrentImg());
        }

        if(getSentCurrentPlace() != null){
            String placeSent = getSentCurrentPlace();
            String[] placeArray = placeSent.split(",");
            String finalPlaceName,finalCityName,finalCountryName="";
            Log.d("finalPlace",Integer.toString(placeArray.length));
            if(placeArray.length == 5){
                finalPlaceName = placeArray[0]+","+placeArray[1];
                finalCityName = placeArray[3];
                finalCountryName = placeArray[4];
                Log.d("finalPlace1",finalPlaceName+","+finalCityName+","+finalCountryName);
            }else if(placeArray.length == 4){
                finalPlaceName = placeArray[0]+","+placeArray[1];
                finalCityName = placeArray[2];
                finalCountryName = placeArray[3];
                Log.d("finalPlace2",finalPlaceName+","+finalCityName+","+finalCountryName);
            }else if(placeArray.length == 3){
                finalPlaceName = placeArray[0];
                finalCityName = placeArray[1];
                finalCountryName = placeArray[2];
                Log.d("finalPlace3",finalPlaceName+","+finalCityName+","+finalCountryName);
            }else if(placeArray.length == 2){
                finalPlaceName = placeArray[0];
                finalCityName = "";
                finalCountryName = placeArray[1];
                Log.d("finalPlace4",finalPlaceName+","+finalCityName+","+finalCountryName);
            }else{
                finalPlaceName = placeArray[0];
                finalCityName = "";
                finalCountryName = "";
                Log.d("finalPlace5",finalPlaceName+","+finalCityName+","+finalCountryName);
            }

            etPlace.setText(finalPlaceName);
            etPlace.setKeyListener(null);
            etCity.setText(finalCityName);
            etCity.setKeyListener(null);
            autoCountry.setText(finalCountryName);
            autoCountry.setKeyListener(null);
        }

        autoCountry = (AutoCompleteTextView) view.findViewById(R.id.autoCountry);

        localUserStorage = new LocalUserStorage(getContext());

        if(localUserStorage.getOpenedTour() == true){
            UserTours userTours = localUserStorage.getOpenedTourData();
            etPlace.setText(userTours.placeName);
            etPlace.setKeyListener(null);
            etCity.setText(userTours.city);
            etCity.setKeyListener(null);
            autoCountry.setText(userTours.country);
            autoCountry.setKeyListener(null);
        }

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries =new  ArrayList<String>();

        for(Locale locale : locales){
            String country = locale.getDisplayCountry();
            if(country.trim().length()>0 && !countries.contains(country)){
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,
                countries);
        autoCountry.setAdapter(arrayAdapter);

        btnSetDate.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        btnChangePhoto.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSetDate:
                showDatePicker();
                break;
            case R.id.btnChangePhoto:
                startActivity(new Intent(getActivity(),AddPhotos.class));
                break;
            case R.id.postBtn:
                if(getCurrentImg() == null){
                    if(etPlace.length() > 0 && autoCountry.length() > 0) {
                        uploadVideoToServer();
                        startActivity(new Intent(getActivity(),Home.class));
                    }else{
                        Toast.makeText(getContext(),"You must indicate the place and country of the photo!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(etPlace.length() > 0 && autoCountry.length() > 0) {
                        uploadImageToServer();
                        startActivity(new Intent(getActivity(),Home.class));
                    }else{
                        Toast.makeText(getContext(),"You must indicate the place and country of the photo!",
                                Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    public void showDatePicker(){
        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),this,year,month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            c.set(year,monthOfYear,dayOfMonth);
            datePicked.setText(dateFormatter.format(c.getTime()));
        }
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imagePlace.setImageBitmap(thumbnail);
                setCurrentImg(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                String selectedImageUri = getAbsolutePath(data.getData());
                imagePlace.setImageBitmap(decodeFile(selectedImageUri));
                setCurrentImg(decodeFile(selectedImageUri));
            }
        }
    }*/

    public Bitmap getCurrentImg() {
        Bitmap bitmap = null;
        byte[] byteData = bundle.getByteArray("byteArray");
        if(byteData == null){
           Log.d("Bitmap","No bitmap");
        }else{
            bitmap = BitmapFactory.decodeByteArray(byteData,0,byteData.length);
        }
        return bitmap;
    }

    public String getCurrentVideo(){
        String video = null;
        if(bundle == null){
            Log.d("Video","No video");
        }else{
            video = bundle.getString("video");
        }
        return video;
    }

    public String getSentCurrentPlace(){
        String sentPlace = null;
        if(bundle == null){
            Log.d("Place","no place");
        }else{
            sentPlace = bundle.getString("place");
        }
        return sentPlace;
    }

   /* public void setCurrentImg(Bitmap currentImg) {
        this.currentImg = currentImg;
    }*/

   /* private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    }*/

    public void uploadVideoToServer(){
        String vid = getCurrentVideo();
        User user = localUserStorage.getLoggedInUser();
        int tourID;
        if(localUserStorage.getOpenedTour() == true){
            UserTours userTours = localUserStorage.getOpenedTourData();
            tourID = userTours.tourID;
        }else{
            tourID = -1;
        }

        place = etPlace.getText().toString()+", "+etCity.getText().toString();
        countryName = autoCountry.getText().toString();
        aboutPhoto = etAboutPhoto.getText().toString();
        int status = 0;
        serverRequest = new ServerRequest(getContext());
        serverRequest.videoUpload(user, tourID, vid, place, countryName, aboutPhoto, new UrlCallBack() {
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
                    Log.i("Upload Status","Null");
                }else{
                    Log.i("Upload Status",Boolean.toString(ef.status));
                    Toast.makeText(getContext(),"Post successful!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void uploadImageToServer(){
        Bitmap bitmapImage = getCurrentImg();
        User user = localUserStorage.getLoggedInUser();
        int tourID;
        if(localUserStorage.getOpenedTour() == true){
            UserTours userTours = localUserStorage.getOpenedTourData();
            tourID = userTours.tourID;
        }else{
            tourID = -1;
        }

        place = etPlace.getText().toString()+", "+etCity.getText().toString();
        countryName = autoCountry.getText().toString();
        aboutPhoto = etAboutPhoto.getText().toString();
        int status = 0;

        if(bitmapImage != null){
            Log.i("Image status","Image exists");
            serverRequest = new ServerRequest(getContext());
            serverRequest.postMark(user,tourID,bitmapImage,place,countryName,aboutPhoto,new UrlCallBack() {
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
                        Log.i("Upload Status","Null");
                    }else{
                        Log.i("Upload Status",Boolean.toString(ef.status));
                        Toast.makeText(getContext(),"Post successful!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Log.i("Image status","Image doesnt exist");
        }
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
        String[] projection = { MediaStore.MediaColumns.DATA };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
