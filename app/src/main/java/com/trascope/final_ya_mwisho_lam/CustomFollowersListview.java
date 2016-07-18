package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.content.Intent;
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
 * Created by cmigayi on 19-Nov-15.
 */
public class CustomFollowersListview extends ArrayAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;

    public CustomFollowersListview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.following_frag_listview_item,arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        hashMap = new HashMap<String, String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.followers_frag_listview_item, parent, false);
        TextView textUsername = (TextView) convertView.findViewById(R.id.textUsername);
        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        TextView textPlace = (TextView) convertView.findViewById(R.id.textPlace);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        hashMap = arrayList.get(position);

        textUsername.setText(hashMap.get("username"));
        textGroup.setText("_"+hashMap.get("group"));
        textPlace.setText(hashMap.get("lastPlace"));
        Picasso.with(context).load(hashMap.get("userImage")).into(imageView);

        textUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserProfileActivity.class);
                intent.putExtra("hashmap",hashMap);
                context.startActivity(intent);
            }
        });
        textPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FeedItemActivity.class);
                intent.putExtra("hashmap",hashMap);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
