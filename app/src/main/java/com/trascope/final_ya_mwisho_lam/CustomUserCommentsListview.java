package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.text.Html;
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
 * Created by cmigayi on 06-Nov-15.
 */
public class CustomUserCommentsListview extends ArrayAdapter {
    Context context;
    ArrayList<HashMap<String, String>> arrayList;
    LayoutInflater layoutInflater;

    public CustomUserCommentsListview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context,R.layout.user_comments_frag_item,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.user_comments_frag_item, parent, false);
        ImageView userImage = (ImageView) convertView.findViewById(R.id.userImage);
        TextView userComment = (TextView) convertView.findViewById(R.id.userComment);
        TextView timeAgo = (TextView) convertView.findViewById(R.id.timeAgo);

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = arrayList.get(position);

        userComment.setText(Html.fromHtml("<font color=\"#4F9EF7\">"+hashMap.get("username")+
                " _family </font>"+hashMap.get("comment")));
        timeAgo.setText(hashMap.get("timeAgo"));
        Picasso.with(context).load(hashMap.get("userImage")).transform(new CircleTransform()).into(userImage);
        return convertView;
    }
}
