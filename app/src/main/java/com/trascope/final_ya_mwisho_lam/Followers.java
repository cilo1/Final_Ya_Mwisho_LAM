package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 03-Nov-15.
 */
public class Followers extends Fragment {
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    TextView textTotalFollowers,nodata;
    ListView listViewFollowers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        listViewFollowers = (ListView) view.findViewById(R.id.listViewFollowers);
        textTotalFollowers = (TextView) view.findViewById(R.id.textTotalFollowers);
        nodata = (TextView) view.findViewById(R.id.nodata);

        localUserStorage = new LocalUserStorage(getContext());
        serverRequest = new ServerRequest(getContext());

        User user = localUserStorage.getLoggedInUser();

        serverRequest.fetchUserFollowersData(user.userID, new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(final ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    Toast.makeText(getContext(), "Check internet connection!", Toast.LENGTH_LONG).show();
                }else {
                    int total = arrayList.size();
                    if (total > 0) {
                        textTotalFollowers.setText("Following (" + Integer.toString(total) + ")");
                        CustomFollowersListview customFollowersListview = new CustomFollowersListview(getContext(), arrayList);
                        listViewFollowers.setAdapter(customFollowersListview);
                        listViewFollowers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap = arrayList.get(position);
                                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                                intent.putExtra("hashmap", hashMap);
                                getActivity().startActivity(intent);
                            }
                        });
                        Log.i("Arraylist status:", "1");
                    } else {
                        Log.i("Arraylist status:", "0");
                        textTotalFollowers.setText("Following (" + Integer.toString(total) + ")");
                        nodata.setVisibility(View.VISIBLE);
                        listViewFollowers.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });

        return view;
    }
}
