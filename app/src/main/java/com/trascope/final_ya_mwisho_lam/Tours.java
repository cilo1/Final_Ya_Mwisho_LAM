package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 26-Oct-15.
 */
public class Tours extends Fragment {

    GridView gridView;
    TextView nodata;
    LinearLayout loadingLayout;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        nodata = (TextView) view.findViewById(R.id.nodata);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadinglayout);
        //gridView.setVisibility(View.GONE);

        serverRequest = new ServerRequest(getContext());
        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();

        serverRequest.fetchUserTours(user.userID,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(final ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList.size() == 0){
                    Toast.makeText(getContext(),"No data found",Toast.LENGTH_LONG).show();
                    nodata.setVisibility(View.VISIBLE);
                }else{
                    //gridView.setVisibility(View.VISIBLE);
                    gridView.setAdapter(new CustomUserToursListview(getContext(),arrayList));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap = arrayList.get(position);

                            Intent intent = new Intent(getContext(), MytourActivity.class);
                            intent.putExtra("hashmap", hashMap);
                            startActivity(intent);
                        }
                    });
                    loadingLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });

        return view;
    }
}
