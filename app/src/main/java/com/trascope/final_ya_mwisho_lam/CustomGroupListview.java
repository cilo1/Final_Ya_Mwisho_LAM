package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 03-Nov-15.
 */
public class CustomGroupListview extends ArrayAdapter {
    Context context;
    ArrayList<HashMap<String,String>> arrayList;
    String [] items;
    LayoutInflater layoutInflater;
    HashMap<String,String> Map;
    int groupID;

    public CustomGroupListview(Context context, ArrayList<HashMap<String,String>> arrayList) {
        super(context, R.layout.group_frag_listview_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.group_frag_listview_item, parent, false);
        TextView textGroupName = (TextView) convertView.findViewById(R.id.textGroupName);
        TextView textTotalFriends = (TextView) convertView.findViewById(R.id.textTotalFriends);
        TextView textGroupDefn = (TextView) convertView.findViewById(R.id.textGroupDefn);

        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap = arrayList.get(position);
        setHashmap(hashMap);

        groupID = Integer.parseInt(hashMap.get("groupID"));

        textGroupName.setText(hashMap.get("groupName"));
        textTotalFriends.setText("("+hashMap.get("totalGroupFollowing")+")");
        textGroupDefn.setText(hashMap.get("groupDescription"));

        textGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserGroupData.class);
                intent.putExtra("hashMap",getHashmap());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void setHashmap(HashMap<String,String> hashMap){
        this.Map = hashMap;
    }
    public HashMap<String,String> getHashmap(){
        return this.Map;
    }
}
