package com.trascope.final_ya_mwisho_lam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class Bucketlist extends Fragment {
    LocalUserStorage localUserStorage;
    User user;
    ServerRequest serverRequest;
    TextView textTotallisted;
    ListView listView;
    LinearLayout loadingLayout;
    Commons commons;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bucketlist, container, false);
        listView = (ListView) view.findViewById(R.id.listViewBucketlist);
        textTotallisted = (TextView) view.findViewById(R.id.textTotallisted);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadinglayout);

        localUserStorage = new LocalUserStorage(getActivity());
        user = localUserStorage.getLoggedInUser();
        serverRequest = new ServerRequest(getContext());
        commons = new Commons();
        boolean network = commons.isNetworkAvailable(getContext());
        if(network == true){
            getBucketlist();
        }else{
            Toast.makeText(getContext(), "Check internet connection!", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public void getBucketlist(){
        serverRequest.fetchUserBacketlist(user, new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {
                if(arrayList == null){
                    Toast.makeText(getContext(), "Check internet connection!", Toast.LENGTH_LONG).show();
                    //textNoData.setVisibility(View.VISIBLE);
                    //listView.setVisibility(View.INVISIBLE);
                }else {
                    textTotallisted.setText(Integer.toString(arrayList.size()) + " Places Listed");
                    //textNoData.setVisibility(View.INVISIBLE);
                    CustomBucketlistListview customBucketlistListview = new CustomBucketlistListview(getActivity(), arrayList);
                    listView.setAdapter(customBucketlistListview);
                    loadingLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}
