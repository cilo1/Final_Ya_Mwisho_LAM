package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.util.Log;
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
 * Created by cmigayi on 27-Dec-15.
 */
public class CustomLocationListview extends ArrayAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ViewHolder viewHolder;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;

    public CustomLocationListview(Context context, ArrayList<HashMap<String, String>> arrayList){
        super(context,R.layout.custom_listview_location_items,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
        hashMap = new HashMap<String, String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.custom_listview_location_items, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.profileImg = (ImageView) convertView.findViewById(R.id.postedBy_img);
            viewHolder.mainImage = (ImageView) convertView.findViewById(R.id.mainImage);
            viewHolder.textTimeAgo = (TextView) convertView.findViewById(R.id.timeUploaded);
            viewHolder.textUsername = (TextView) convertView.findViewById(R.id.postedBy);
            viewHolder.totalLikes = (TextView) convertView.findViewById(R.id.totalLikes);
            viewHolder.totalBucketlist = (TextView) convertView.findViewById(R.id.totalBucketlist);
            viewHolder.textDistance = (TextView) convertView.findViewById(R.id.textDistance);
            viewHolder.textPlace = (TextView) convertView.findViewById(R.id.textPlace);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        hashMap = arrayList.get(position);
        Picasso.with(context).load(hashMap.get("userImage")).transform(new CircleTransform()).into(viewHolder.profileImg);
        Picasso.with(context).load(hashMap.get("imageUrl")).into(viewHolder.mainImage);
        viewHolder.textUsername.setText(hashMap.get("postedBy"));
        viewHolder.textDistance.setText("Dist. "+hashMap.get("distance")+"km away");
        viewHolder.textTimeAgo.setText(hashMap.get("timeAgo"));
        viewHolder.totalBucketlist.setText("Bucketlist "+hashMap.get("totalBucketlist"));
        viewHolder.totalLikes.setText("Likes "+hashMap.get("totalLikes"));
        viewHolder.textPlace.setText(hashMap.get("place"));

        Log.d("username:",hashMap.get("postedBy"));
        return convertView;
    }

    public class ViewHolder{
       TextView textUsername,totalLikes,totalBucketlist,textTimeAgo,textDistance,textPlace;
       ImageView profileImg,mainImage;
    }
}
