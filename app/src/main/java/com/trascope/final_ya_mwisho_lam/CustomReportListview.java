package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by cmigayi on 30-Nov-15.
 */
public class CustomReportListview extends ArrayAdapter {
    String[] items;
    Context context;
    LayoutInflater layoutInflater;

    public CustomReportListview(Context context, String[] items) {
        super(context, R.layout.report_listview_item,items);
        this.context = context;
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.report_listview_item, null);
            viewHolder = new ViewHolder();

            viewHolder.topText = (TextView) convertView.findViewById(R.id.topText);
            viewHolder.bottomText = (TextView) convertView.findViewById(R.id.bottomText);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.topText.setText(items[position]);
        switch (items[position]){
            case "Self Injury":
                viewHolder.bottomText.setText("Any disorder or Promoting suicide");
                break;
            case "Violence or Harm":
                viewHolder.bottomText.setText("Graphic injury, Unlawful activity, Dangerous or" +
                        " Criminal Organisations");
                break;
            case "Hate speech or Symbol":
                viewHolder.bottomText.setText("Racism, Homophobic or Sexist slur");
                break;
            case "Intellectual property":
                viewHolder.bottomText.setText("Copyright or Trademark Infringement");
                break;
        }

        return convertView;
    }

   static class ViewHolder{
        TextView topText,bottomText;
    }
}
