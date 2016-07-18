package com.trascope.final_ya_mwisho_lam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by cmigayi on 09-Nov-15.
 */
public class UserLikesFragment extends Fragment {
    String [] items = {"http://www.goodsclearanceclub.com/LAM_final/posted_images/balloon1.png",
            "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist3.png",
            "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist1.png",
            "http://www.goodsclearanceclub.com/LAM_final/posted_images/tourist4.png",
            "http://www.goodsclearanceclub.com/LAM_final/posted_images/sham.png"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_likes,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        CustomUserMarksGridview customUserMarksGridview = new CustomUserMarksGridview(getContext(),items);
        gridView.setAdapter(customUserMarksGridview);
        return view;
    }
}
