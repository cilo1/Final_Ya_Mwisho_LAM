package com.trascope.final_ya_mwisho_lam;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;

/**
 * Created by cmigayi on 21-Oct-15.
 */
public class CustomSetTabLayout {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    public  FragmentManager supportFragmentManager;
    public String activityName;

    public CustomSetTabLayout(TabLayout tabLayout, ViewPager viewPager,
                              FragmentManager supportFragmentManager, String activityName) {
        this.pagerAdapter = pagerAdapter;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this.supportFragmentManager = supportFragmentManager;
        this.activityName = activityName;
    }

    public void addActivitySelectedTabs(String selectedActivity){
        switch(selectedActivity){
            case "Search":
                tabLayout.addTab(tabLayout.newTab().setText("People"));
                tabLayout.addTab(tabLayout.newTab().setText("Places"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                break;
            case "Friends":
                tabLayout.addTab(tabLayout.newTab().setText("Following"));
                tabLayout.addTab(tabLayout.newTab().setText("Followers"));
                tabLayout.addTab(tabLayout.newTab().setText("Group"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                break;
            default:
                tabLayout.addTab(tabLayout.newTab().setText("Home"));
                tabLayout.addTab(tabLayout.newTab().setText("Popular"));
                tabLayout.addTab(tabLayout.newTab().setText("Bucketlist"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                break;
        }
    }

    public void setTabStructure(){
        addActivitySelectedTabs(activityName);
        PagerAdapter pagerAdapter = new PagerAdapter(supportFragmentManager,
                tabLayout.getTabCount(),activityName);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
