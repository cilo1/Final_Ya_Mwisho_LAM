package com.trascope.final_ya_mwisho_lam;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class OpenTour extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button openTourBtn,closeTourBtn;
    View fragment;
    String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_open_tour);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        place = intent.getStringExtra("place");

        openTourBtn = (Button) findViewById(R.id.openTourBtn);
        closeTourBtn = (Button) findViewById(R.id.closeTourBtn);
        fragment = findViewById(R.id.fragment);
        fragment.setVisibility(View.GONE);

        openTourBtn.setOnClickListener(this);
        closeTourBtn.setOnClickListener(this);

        ImageView logoIcon = (ImageView) toolbar.findViewById(R.id.logoIcon);
        logoIcon.setImageResource(R.drawable.back2);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("Open Tour");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openTourBtn:
                if(place != null){
                    addNewOpenTourFragment addNewOpenTourFragment = new addNewOpenTourFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("place",place);
                    addNewOpenTourFragment.setArguments(bundle);
                }
                openTourBtn.setVisibility(View.GONE);
                closeTourBtn.setVisibility(View.VISIBLE);
                fragment.setVisibility(View.VISIBLE);
                break;
            case R.id.closeTourBtn:
                this.finish();
                Toast.makeText(this,"You closed the tour, before creating.",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_tour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_read_open_tour) {
            startActivity(new Intent(this,OpenTourIntro.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
