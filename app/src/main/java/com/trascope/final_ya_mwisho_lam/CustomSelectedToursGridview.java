package com.trascope.final_ya_mwisho_lam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 18-Dec-15.
 */
public class CustomSelectedToursGridview extends ArrayAdapter implements View.OnClickListener {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    TextView textItemAbout,textItemTime,textPlace;
    ImageView imageItem;

    public CustomSelectedToursGridview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.selected_tours_gridview_item,arrayList);
        this.arrayList = arrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.selected_tours_gridview_item, parent, false);
        textItemAbout = (TextView) convertView.findViewById(R.id.textItemAbout);
        textItemTime = (TextView) convertView.findViewById(R.id.textItemTime);
        textPlace = (TextView) convertView.findViewById(R.id.textPlace);
        imageItem = (ImageView) convertView.findViewById(R.id.imageItem);

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = arrayList.get(position);

        Picasso.with(context).load(hashMap.get("imageUrl")).into(imageItem);
        if(hashMap.get("about") == null){
            textItemAbout.setVisibility(View.GONE);
        }else{
            textItemAbout.setText("\""+hashMap.get("about")+"\"");
        }
        textItemTime.setText(hashMap.get("timeAgo"));
        textPlace.setText(hashMap.get("place"));

        imageItem.setOnClickListener(this);
        imageItem.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer)v.getTag();
        Log.d("Tag", "Tag:" + position);
        HashMap<String,String> map = new HashMap<>();
        map = arrayList.get(position);
        switch(v.getId()) {
            case R.id.imageItem:
                displayFeedItem(map);
                break;
        }
    }

    public void displayFeedItem(HashMap<String, String> hashMap){
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setTitle(hashMap.get("place"));
        dialog.setContentView(R.layout.dialog_feed_item);
        ImageView imageMain = (ImageView) dialog.findViewById(R.id.imageMain);
        TextView textPlace = (TextView) dialog.findViewById(R.id.textPlace);
        TextView textAbout = (TextView) dialog.findViewById(R.id.textAbout);
        TextView textTime = (TextView) dialog.findViewById(R.id.textTime);

        Picasso.with(getContext()).load(hashMap.get("imageUrl")).into(imageMain);
        if(hashMap.get("about") == null){
            textAbout.setVisibility(View.GONE);
        }else{
            textAbout.setText("\""+hashMap.get("about")+"\"");
        }
        textPlace.setText("Posted by "+hashMap.get("postedBy"));
        textTime.setText(hashMap.get("timeAgo"));
        dialog.show();
    }
}
