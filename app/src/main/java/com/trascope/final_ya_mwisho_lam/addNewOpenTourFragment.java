package com.trascope.final_ya_mwisho_lam;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by cmigayi on 16-Dec-15.
 */
public class addNewOpenTourFragment extends Fragment {
    AutoCompleteTextView autoCountry;
    static TextView datePicked;
    Button btnSetDate,add_tour_btn;
    EditText place_name, tour_name, city_name;
    String str_tour_name,str_place_name,str_city_name,str_country,str_date;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_open_tour,container,false);
        autoCountry = (AutoCompleteTextView) view.findViewById(R.id.autoCountry);
        datePicked = (TextView) view.findViewById(R.id.datePicked);
        btnSetDate = (Button) view.findViewById(R.id.btnSetDate);
        add_tour_btn = (Button) view.findViewById(R.id.add_tour_btn);
        place_name = (EditText) view.findViewById(R.id.place_name);
        city_name = (EditText) view.findViewById(R.id.city_name);
        tour_name = (EditText) view.findViewById(R.id.tour_name);

        bundle = this.getArguments();
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

            place_name.setText(finalPlaceName);
            place_name.setKeyListener(null);
            city_name.setText(finalCityName);
            city_name.setKeyListener(null);
            autoCountry.setText(finalCountryName);
            autoCountry.setKeyListener(null);
        }

        serverRequest = new ServerRequest(getContext());
        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();

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

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        add_tour_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_tour_name = tour_name.getText().toString();
                str_place_name = place_name.getText().toString();
                str_city_name = city_name.getText().toString();
                str_country = autoCountry.getText().toString();
                str_date = datePicked.getText().toString();

                if(str_tour_name.length()==0 && str_place_name.length()==0 &&
                        str_city_name.length()==0 && str_country.length()==0
                        && str_date.length()==0){
                    Toast.makeText(getContext(),"All fields must be filled!",Toast.LENGTH_LONG).show();
                }else{
                    str_place_name = str_place_name+", "+str_city_name;
                    startNewOpenTour(user.userID,str_tour_name,str_place_name,str_country,str_date);
                }
            }
        });

        return view;
    }

    public String getSentCurrentPlace(){
        String sentPlace = null;
        if(bundle == null){
            Log.d("Place", "no place");
        }else{
            sentPlace = bundle.getString("place");
        }
        return sentPlace;
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

    public void startNewOpenTour(int userID,String tour_name,String place_name,String country,String date){

        serverRequest.createUserTour(userID,tour_name,place_name,country,date,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();
                }else{
                    localUserStorage.storeUserTours(arrayList);
                    localUserStorage.setOpenedTour(true);
                    chooseNextAction();
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }

    public void chooseNextAction(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_new_open_tour_choose_next_action);
        dialog.setTitle("Choose next Action");
        dialog.setCanceledOnTouchOutside(true);
        Button post = (Button) dialog.findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPhotos.class));
            }
        });
        Button back = (Button) dialog.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Home.class));
            }
        });
        dialog.show();
    }
}
