package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 15-Dec-15.
 */
public class CustomUserToursListview extends ArrayAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    String [] places;
    TextView tourName,placeName;
    ImageView tourImage;

    public CustomUserToursListview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.user_tour_listview_item, arrayList);
        this.arrayList = arrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.user_tour_listview_item,parent,false);
        tourName = (TextView) convertView.findViewById(R.id.tourName);
        placeName = (TextView) convertView.findViewById(R.id.placeName);
        tourImage = (ImageView) convertView.findViewById(R.id.tourImage);

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = arrayList.get(position);

        tourName.setText(hashMap.get("tourName"));
        placeName.setText(hashMap.get("place")+" "+hashMap.get("country"));
        if(hashMap.get("tourImage").equals("none")){
            Picasso.with(context).load(R.drawable.no_image2).into(tourImage);
        }else{
            Picasso.with(context).load(hashMap.get("tourImage")).into(tourImage);
        }

        return convertView;
    }
}
