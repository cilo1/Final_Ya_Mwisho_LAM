package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;


public class HowItWorks extends AppIntro {
    String register = null;
    String email = null;

    @Override
    public void init(@Nullable Bundle bundle) {
        Intent intent = getIntent();
        register = intent.getStringExtra("register");
        email = intent.getStringExtra("email");

        addSlide(SampleSlide.newInstance(R.layout.fragment_intro0));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro1));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro2));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro3));

        //setFadeAnimation();
        //setZoomAnimation();
          setFlowAnimation();
        //setSlideOverAnimation();
        //setDepthAnimation();

        setBarColor(Color.parseColor("#ee0000"));
        setSeparatorColor(Color.parseColor("#2196F3"));
    }

    @Override
    public void onSkipPressed() {
        if(register != null){
            Toast.makeText(this,"Registration successful, you can now login!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,Login.class);
            intent.putExtra("register","register");
            intent.putExtra("email",email);
            startActivity(intent);
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onDonePressed() {
        if(register != null){
            Toast.makeText(this,"Registration successful, you can now login!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,Login.class);
            intent.putExtra("register","register");
            intent.putExtra("email",email);
            startActivity(intent);
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_it_works, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
