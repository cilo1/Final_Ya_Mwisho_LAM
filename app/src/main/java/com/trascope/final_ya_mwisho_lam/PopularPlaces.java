package com.trascope.final_ya_mwisho_lam;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by cmigayi on 20-Oct-15.
 */
public class PopularPlaces extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    ArrayList<HashMap<String,String>> arrayList;
    User user;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    TextView textViewTotalPlaces;
    ListAdapter listAdapter;
    LinearLayout loadingLayout;
    Commons commons;
    boolean network;
    int arrayTotal;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_places, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.red,R.color.bluedeep);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = (ListView) view.findViewById(R.id.listPopular);
        textViewTotalPlaces = (TextView) view.findViewById(R.id.textViewTotalPlaces);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadinglayout);
        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();
        arrayTotal = 0;

        commons = new Commons();
        network = commons.isNetworkAvailable(getContext());
        if(network == false){
            //loadDataFromSqlite();
            Toast.makeText(getContext(),"No internet connetion", Toast.LENGTH_LONG).show();
        }else{
            loadDataFromServer();
            //Toast.makeText(getContext(),"Internet connection available!",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public void loadDataFromSqlite(){
        FeedReaderDBHelper frDBHelper = new FeedReaderDBHelper(getContext());
        Cursor cursor = frDBHelper.getInfoPopularPlaces(frDBHelper);
        cursor.moveToFirst();
        do{
            arrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("popular_id",cursor.getString(0));
            hashMap.put("username",cursor.getString(1));
            hashMap.put("userImg",cursor.getString(2));
            hashMap.put("place",cursor.getString(3));
            hashMap.put("imageUrl",cursor.getString(4));
            hashMap.put("totalLikes",cursor.getString(5));
            hashMap.put("totalBucketlist",cursor.getString(6));
            arrayList.add(hashMap);
        }while(cursor.moveToNext());
        cursor.close();
        arrayTotal = arrayList.size();
        textViewTotalPlaces.setText(Integer.toString(arrayTotal)+" Places Listed");
        listAdapter = new CustomPopularPlacesListview(getActivity(), arrayList);
        listView.setAdapter(listAdapter);
    }

    public void loadDataFromServer(){
        serverRequest = new ServerRequest(getContext());
        serverRequest.fetchPopularMarks(user, new UrlCallBack() {
            @Override
            public void done(User user) {
            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if (arrayList == null) {
                    Log.d("Popular arraylist","null");
                } else {
                    //inserDataToSqlite(arrayList);
                    arrayTotal = arrayList.size();
                    textViewTotalPlaces.setText(Integer.toString(arrayTotal) + " Places Listed");
                    listAdapter = new CustomPopularPlacesListview(getActivity(), arrayList);
                    listView.setAdapter(listAdapter);
                    loadingLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {
            }
        });
    }

    public void inserDataToSqlite(ArrayList<HashMap<String,String>> arrayList){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        for(int i=0;i<arrayList.size();i++){
            hashMap = arrayList.get(i);

            FeedReaderDBHelper fdDBHelper = new FeedReaderDBHelper(getContext());
            fdDBHelper.putInfoPopularMarks(fdDBHelper, hashMap.get("popular_id"), hashMap.get("username"),
                    hashMap.get("userImg"), hashMap.get("place"), hashMap.get("imageUrl"),
                    hashMap.get("totalLikes"), hashMap.get("totalBucketlist"));
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                network = commons.checkInternetConnetion(getContext());
                if(network == false){
                    //loadDataFromSqlite();
                    Toast.makeText(getContext(),"No internet connetion", Toast.LENGTH_LONG).show();
                }else{
                    loadDataFromServer();
                    Toast.makeText(getContext(),"Internet connection available!",Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
