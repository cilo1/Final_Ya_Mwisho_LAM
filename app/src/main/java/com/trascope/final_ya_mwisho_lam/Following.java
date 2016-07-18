package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 03-Nov-15.
 */
public class Following extends Fragment{
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;
    TextView textTotalFollowing,nodata;
    ListView listView;
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        listView = (ListView) view.findViewById(R.id.listViewFriends);
        textTotalFollowing = (TextView) view.findViewById(R.id.textTotalFollowing);
        nodata = (TextView) view.findViewById(R.id.nodata);

        localUserStorage = new LocalUserStorage(getContext());
        serverRequest = new ServerRequest(getContext());

        user = localUserStorage.getLoggedInUser();
        serverRequest.fetchUserFollowingData(user.userID,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(final ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    Toast.makeText(getContext(),"Check internet connection!",Toast.LENGTH_LONG).show();
                }else{
                    int total = arrayList.size();
                    if(total > 0){
                        textTotalFollowing.setText("Following ("+Integer.toString(total)+")");
                        CustomFollowingListview customFollowingListview = new CustomFollowingListview(getContext(), arrayList);
                        listView.setAdapter(customFollowingListview);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap = arrayList.get(position);
                                Intent intent = new Intent(getActivity(),UserProfileActivity.class);
                                intent.putExtra("hashmap",hashMap);
                                getActivity().startActivity(intent);
                            }
                        });
                        Log.i("Arraylist status:", "1");
                    }else{
                        Log.i("Arraylist status:", "0");
                        textTotalFollowing.setText("Following ("+Integer.toString(total)+")");
                        nodata.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
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
