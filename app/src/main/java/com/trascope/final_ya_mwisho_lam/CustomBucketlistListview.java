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
 * Created by cmigayi on 28-Oct-15.
 */
public class CustomBucketlistListview extends ArrayAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<HashMap<String,String>> arrayList;

    public CustomBucketlistListview(Context context, ArrayList<HashMap<String,String>> arrayList) {
        super(context, R.layout.bucketlist_frag_listview_item,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.bucketlist_frag_listview_item, parent,false);
        ImageView imagePlace = (ImageView) convertView.findViewById(R.id.imagePlace);
        TextView textPlace = (TextView) convertView.findViewById(R.id.textPlace);
        TextView textPostedBy = (TextView) convertView.findViewById(R.id.textPostedBy);
        TextView textBucketlist = (TextView) convertView.findViewById(R.id.textBucketlist);
        TextView textLikes = (TextView) convertView.findViewById(R.id.textLikes);
        HashMap<String,String> hashMap = new HashMap<String, String>();

        hashMap = arrayList.get(position);
        Picasso.with(context).load(hashMap.get("imageUrl")).into(imagePlace);
        textPlace.setText(hashMap.get("place"));
        textPostedBy.setText("posted by "+hashMap.get("username"));
        textBucketlist.setText("Bucketlist "+hashMap.get("totalBucketlist"));
        textLikes.setText("Likes "+hashMap.get("totalLikes"));
        return convertView;
    }
}
