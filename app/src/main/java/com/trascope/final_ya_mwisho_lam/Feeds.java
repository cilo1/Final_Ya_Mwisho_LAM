package com.trascope.final_ya_mwisho_lam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class Feeds extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton favBtn;
    ListView listView;
    TextView textLocationMark;
    ListAdapter listAdapter;
    LinearLayout notifyLinearlayout;
    ServerRequest serverRequest;
    LocalUserStorage localUserStorage;
    User user;
    ArrayList<HashMap<String,String>> arrayList;
    Commons commons;
    boolean refreshed;
    LinearLayout loadingLayout;
    Button proceed,later;
    String place;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        serverRequest = new ServerRequest(getActivity());
        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();

        place = null;

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) view.findViewById(R.id.listView);
        proceed = (Button) view.findViewById(R.id.proceed);
        later = (Button) view.findViewById(R.id.later);
        textLocationMark = (TextView) view.findViewById(R.id.textLocationMark);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadinglayout);
        notifyLinearlayout = (LinearLayout) view.findViewById(R.id.notifyLinearlayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.bluedeep);
        commons = new Commons();
        boolean network = commons.isNetworkAvailable(getContext());

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyLinearlayout.setVisibility(View.GONE);
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOptionForCurrentLocation();
            }
        });

       // getBacketlistStatus();
        if(network == true){
            loadDataFromServer();
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadDataFromServer();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }else{
            Toast.makeText(getContext(),"No internet connetion", Toast.LENGTH_LONG).show();
            //Toast.makeText(getContext(),"Internet connection available!",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public void chooseOptionForCurrentLocation(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_choose_option_for_current_location);
        dialog.setTitle("Choose one action:");
        Button btnOpentour = (Button) dialog.findViewById(R.id.btnOpentour);
        btnOpentour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OpenTour.class);
                intent.putExtra("place",place);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        Button btnProceedButton = (Button) dialog.findViewById(R.id.btnProceedUpload);
        btnProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPhotos.class);
                intent.putExtra("place",place);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void loadDataFromSqlite(){
        FeedReaderDBHelper frDBHelper = new FeedReaderDBHelper(getContext());
        Cursor cursor = frDBHelper.getInformation(frDBHelper);
        if(cursor == null){
            Toast.makeText(getContext(),"No data loaded yet",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            do {
                arrayList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("content_id", cursor.getString(0));
                hashMap.put("user_id", cursor.getString(1));
                hashMap.put("postedBy", cursor.getString(2));
                hashMap.put("place", cursor.getString(3));
                hashMap.put("imageUrl", cursor.getString(4));
                hashMap.put("relation", cursor.getString(5));
                hashMap.put("totalLikes", cursor.getString(6));
                hashMap.put("totalBucketlist", cursor.getString(7));
                hashMap.put("dateTime", cursor.getString(8));
                arrayList.add(hashMap);
            } while (cursor.moveToNext());
            //cursor.close();
            refreshed = false;
            listAdapter = new CustomFeedsListview(getActivity(), arrayList);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap = arrayList.get(position);
                    //Intent intent = new Intent(getContext(),FeedItemActivity.class);
                    //intent.putExtra("hashmap",hashMap);
                    //getActivity().startActivity(intent);
                    displayFeedItem(hashMap);
                }
            });
            loadingLayout.setVisibility(View.GONE);
        }
    }

    public void loadDataFromServer(){

        serverRequest.fetchAllUsersContent(user,new UrlCallBack() {
            @Override
            public void done(User user) {
                if (user == null) {
                    Toast.makeText(getContext(),"Check internet connection!",Toast.LENGTH_LONG).show();
                }else{
                    refreshed = true;
                    // inserDataToSqlite(user.contentList);
                    listView.setAdapter(new CustomFeedsListview(getContext(), user.contentList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap = arrayList.get(position);
                            Intent intent = new Intent(getContext(),FeedItemActivity.class);
                            intent.putExtra("hashmap",hashMap);
                            startActivity(intent);
                            //displayFeedItem(hashMap);
                        }
                    });
                    loadingLayout.setVisibility(View.GONE);
                    //checkUserLocationHomeOrWork();
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

    public double[] getCurrentLocation(){
        Bundle bundle = this.getArguments();
        double[] arrayCoordinates = null;
        if(bundle == null){
            Log.d("currentLocation","No loction");
        }else{
            arrayCoordinates = bundle.getDoubleArray("arrayCoordinates");
        }
        return arrayCoordinates;
    }

    public void checkUserLocationHomeOrWork(){
        double[] arrayLatLong = getCurrentLocation();
        String locLat = Double.toString(arrayLatLong[0]);
        String locLong = Double.toString(arrayLatLong[1]);

        serverRequest.postMarkForNewLocation(user.userID, locLat, locLong, new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if (arrayList == null) {
                    Log.d("Arraylist", "null");
                } else {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map = arrayList.get(0);

                    if (!map.get("status").equals("true")) {
                        place = map.get("location");
                        notifyLinearlayout.setVisibility(View.VISIBLE);
                        textLocationMark.setText(Html.fromHtml("Do you want to leave a mark on your " +
                                "current location: <br/><b>" + map.get("location") + "</b>"));
                    }
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });
    }

    public void displayFeedItem(HashMap<String, String> hashMap){
        Dialog dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_feed_item);
        ImageView imageMain = (ImageView) dialog.findViewById(R.id.imageMain);
        TextView textPlace = (TextView) dialog.findViewById(R.id.textPlace);

        Picasso.with(getContext()).load(hashMap.get("imageUrl")).into(imageMain);
        textPlace.setText(hashMap.get("place"));
        dialog.show();
    }

    public void inserDataToSqlite(ArrayList<HashMap<String,String>> arrayList){
        FeedReaderDBHelper fdDBHelper = new FeedReaderDBHelper(getContext());
        HashMap<String,String> hashMap = new HashMap<String,String>();
        for(int i=0;i<arrayList.size();i++){
            hashMap = arrayList.get(i);
            fdDBHelper.putInformation(fdDBHelper,hashMap.get("content_id"),hashMap.get("user_id"),hashMap.get("postedBy"),hashMap.get("place"),
                    hashMap.get("imageUrl"),hashMap.get("relation"),hashMap.get("totalLikes"),
                    hashMap.get("totalBucketlist"),hashMap.get("timeAgo"));
        }
    }

}
