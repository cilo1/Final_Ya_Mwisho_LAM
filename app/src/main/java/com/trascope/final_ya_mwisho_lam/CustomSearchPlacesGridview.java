package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 28-Oct-15.
 */
public class CustomSearchPlacesGridview extends ArrayAdapter {
    Context context;
    ArrayList<HashMap<String,String>> arrayList;
    LayoutInflater layoutInflater;

    public CustomSearchPlacesGridview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.search_places_frag_gridview_item,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.search_places_frag_gridview_item,parent,false);
        TextView textItemName = (TextView) convertView.findViewById(R.id.textItemName);
        ImageView imageItem = (ImageView) convertView.findViewById(R.id.imageItem);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap = arrayList.get(position);

        textItemName.setText(hashMap.get("place"));
        Picasso.with(context).load(hashMap.get("imageUrl")).into(imageItem);
        return convertView;
    }
}
