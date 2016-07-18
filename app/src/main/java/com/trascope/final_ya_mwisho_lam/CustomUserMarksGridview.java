package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by cmigayi on 09-Nov-15.
 */
public class CustomUserMarksGridview extends ArrayAdapter {
    String[] items;
    Context context;
    LayoutInflater layoutInflater;

    public CustomUserMarksGridview(Context context, String[] items) {
        super(context, R.layout.user_marks_frag_gridview,items);
        this.context = context;
        this.items = items;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.user_marks_frag_gridview,parent,false);
        ImageView mainImage = (ImageView) convertView.findViewById(R.id.mainImage);
        //TextView itemImage = (TextView) convertView.findViewById(R.id.itemName);

        Picasso.with(context).load(items[position]).into(mainImage);
       // itemImage.setText("Place");

        return convertView;
    }
}
