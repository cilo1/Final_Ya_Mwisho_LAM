package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 21-Oct-15.
 */
public class SearchPlaces extends Fragment {
    EditText editSearch;
    TextView noData;
    GridView gridView,gridView2;
    ServerRequest serverRequest;
    CustomSearchPlacesGridview customSearchPlacesGridview;
    ProgressBar progressBar;
    Commons commons;
    User user;
    LocalUserStorage localUserStorage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_places, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        noData = (TextView) view.findViewById(R.id.nodata);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        commons = new Commons();
        serverRequest = new ServerRequest(getContext());
        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();
        boolean network = commons.isNetworkAvailable(getContext());

        if(network == true){
            getPlaces();
        }else{
            Toast.makeText(getContext(),"Network is not available!",Toast.LENGTH_LONG).show();
        }


        return view;
    }

    public void getPlaces(){
        serverRequest.fetchAllUsersContent(user,new UrlCallBack() {
            @Override
            public void done(final User user) {
                progressBar.setVisibility(View.GONE);
                if(user != null){
                    final ArrayList<HashMap<String, String>> arrayList = user.contentList;
                    customSearchPlacesGridview = new CustomSearchPlacesGridview(getActivity(),
                            arrayList);
                    gridView.setAdapter(customSearchPlacesGridview);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap = arrayList.get(position);
                            Intent intent = new Intent(getActivity(),FeedItemActivity.class);
                            intent.putExtra("hashmap",hashMap);
                            startActivity(intent);
                        }
                    });
                }else{
                    Toast.makeText(getContext(),"Check internet connection!",Toast.LENGTH_LONG).show();
                    gridView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }
}
