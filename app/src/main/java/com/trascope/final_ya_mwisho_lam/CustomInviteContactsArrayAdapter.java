package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 23-Nov-15.
 */
public class CustomInviteContactsArrayAdapter extends ArrayAdapter implements View.OnClickListener {
    ArrayList<HashMap<String,String>> contacts;
    Context context;
    LayoutInflater layoutInflater;
    HashMap<String,String> hashMap;

    public CustomInviteContactsArrayAdapter(Context context,  ArrayList<HashMap<String,String>> contacts) {
        super(context, R.layout.invite_contacts_listview_item,contacts);
        this.contacts = contacts;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.invite_contacts_listview_item,parent,false);
        TextView textNumber = (TextView) convertView.findViewById(R.id.textNumber);
        TextView textName = (TextView) convertView.findViewById(R.id.textName);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        Button btnInvite = (Button) convertView.findViewById(R.id.btnInvite);

        hashMap = new HashMap<String,String>();
        hashMap = contacts.get(position);

        btnInvite.setOnClickListener(this);
        btnInvite.setTag(position);

        textName.setText(hashMap.get("name"));
        textNumber.setText(hashMap.get("number"));
        //Picasso.with(context).load(hashMap.get("photo")).into(imageView);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnInvite:
                    int position=(Integer)v.getTag();
                    hashMap = contacts.get(position);
                    String msg = "Hey there, have you tried LeaveAMark yet? I think" +
                            " you will like it. Check it out http://www.goodsclearanceclub.com/LAM_final/app-debug.apk";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("vnd.android-dir/mms-sms");
                    intent.putExtra("address",hashMap.get("number"));
                    intent.putExtra("sms_body",msg);
                    getContext().startActivity(intent);
                break;
        }

    }
}
