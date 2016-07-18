package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 16-Feb-16.
 */
public class CustomFavoritesListview extends ArrayAdapter {
    ArrayList<HashMap<String,String>> arrayList;
    LayoutInflater layoutInflater;
    Context context;

    public CustomFavoritesListview(Context context, ArrayList<HashMap<String,String>> arrayList) {
        super(context, R.layout.favorite_item_listview,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.favorite_item_listview,null);

            viewHolder = new ViewHolder();
            viewHolder.imagePlace = (ImageView) convertView.findViewById(R.id.imagePlace);
            viewHolder.textPlace = (TextView) convertView.findViewById(R.id.textPlace);
            viewHolder.textPostedBy = (TextView) convertView.findViewById(R.id.textPostedBy);
            viewHolder.textBucketlist = (TextView) convertView.findViewById(R.id.textBucketlist);
            viewHolder.textLikes = (TextView) convertView.findViewById(R.id.textLikes);
            viewHolder.textTimeAgo = (TextView) convertView.findViewById(R.id.textTime);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap = arrayList.get(position);

        viewHolder.textPlace.setText(hashMap.get("place"));
        viewHolder.textPostedBy.setText(hashMap.get("username"));
        viewHolder.textBucketlist.setText("Bucketlist "+hashMap.get("totalBucketlist"));
        viewHolder.textLikes.setText("Likes "+hashMap.get("totalLikes"));
        viewHolder.textTimeAgo.setText(hashMap.get("timeAgo"));
        Picasso.with(context).load(hashMap.get("imageUrl")).into(viewHolder.imagePlace);

        return convertView;
    }

    static class ViewHolder{
        ImageView imagePlace;
        TextView textPlace,textTimeAgo,textPostedBy,textBucketlist,textLikes;
        ImageButton starrbtn;
    }
}
