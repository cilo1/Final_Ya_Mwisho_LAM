package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;


public class Report extends AppCompatActivity{
    HashMap<String, String> hashMap;
    ListView listView;
    Toolbar toolbar;
    ImageView logoIcon;
    TextView title;
    String[] items = {"Self Injury","Harassment or Bullying","Sale or promotion of illegal drugs",
            "Nudity or Pornography","Violence or Harm","Hate speech or Symbol","Intellectual property " +
            "violation","I just don't like it"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_report);
        
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashmap");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        title = (TextView) toolbar.findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.listView);

        logoIcon.setImageResource(R.drawable.back2);
        title.setText("Report");

        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CustomReportListview customReportListview = new CustomReportListview(this,items);
        listView.setAdapter(customReportListview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reportItem =items[position];
                Intent intent = new Intent(getApplicationContext(),ReportIncident.class);
                intent.putExtra("item",reportItem);
                intent.putExtra("hashmap",hashMap);
                startActivity(intent);
            }
        });
    }

}
