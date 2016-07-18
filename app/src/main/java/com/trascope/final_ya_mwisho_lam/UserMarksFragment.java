package com.trascope.final_ya_mwisho_lam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmigayi on 09-Nov-15.
 */
public class UserMarksFragment extends Fragment {
String [] items = {"http://www.goodsclearanceclub.com/LAM_final/posted_images/balloon1.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist3.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist1.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist4.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/sham.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/balloon1.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist3.png",
        "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist1.png"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_marks, container, false);
        //ListView listView = (ListView) view.findViewById(R.id.listView);
        StaggeredGridView gridView = (StaggeredGridView) view.findViewById(R.id.grid_view);

        ListAdapter listAdapter = new CustomUserMarksGridview(getContext(),items);
        gridView.setAdapter(listAdapter);

//        CustomUserMarksGridview customUserMarksGridview = new CustomUserMarksGridview(getContext(),items);
//        listView.setAdapter(customUserMarksGridview);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        return view;
    }
}
