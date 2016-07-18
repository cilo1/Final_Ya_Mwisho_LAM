package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.logging.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 21-Oct-15.
 */
public class CustomSearchPeopleGridview extends ArrayAdapter implements Filterable{
    String [] items;
    ArrayList<HashMap<String,String>> arrayList;
    ArrayList<HashMap<String,String>> mArrayList;
    Context context;
    LayoutInflater layoutInflater;
    ValueFilter valueFilter;
    HashMap<String,String> map;

    public CustomSearchPeopleGridview(Context context, ArrayList<HashMap<String,String>> arrayList) {
        super(context, R.layout.search_people_frag_gridview_item,arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.mArrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        map = new HashMap<String,String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.search_people_frag_gridview_item,parent,false);
        TextView textItemName = (TextView) convertView.findViewById(R.id.textItemName);
        ImageView imageItem = (ImageView) convertView.findViewById(R.id.imageItem);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap = arrayList.get(position);

        textItemName.setText(hashMap.get("username"));
        Picasso.with(context).load(hashMap.get("userImage")).into(imageItem);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(valueFilter == null){
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public class ValueFilter extends Filter{
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList = (ArrayList<HashMap<String, String>>) results.values;
            notifyDataSetChanged();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults  results = new FilterResults();
            if(constraint != null && constraint.length()>0){
                ArrayList<HashMap<String,String>> filterList = new ArrayList<HashMap<String,String>>();
                for(int i=0;i<mArrayList.size();i++){
                    map = mArrayList.get(i);
                    if(map.get("username").contains(constraint.toString())){
                        filterList.add(map);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }else{
                results.count = mArrayList.size();
                results.values = mArrayList;
            }

            return results;
        }
    }
}
