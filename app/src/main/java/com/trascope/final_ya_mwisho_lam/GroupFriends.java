package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 03-Nov-15.
 */
public class GroupFriends extends Fragment{
    String [] items = {"Family","Just Friends","School Mates","Work Mates"};
    ServerRequest serverRequest;
    User user;
    LocalUserStorage localUserStorage;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_friends,container,false);
        listView = (ListView) view.findViewById(R.id.listViewGroup);

        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();

        serverRequest = new ServerRequest(getContext());
        serverRequest.fetchUserGroups(user.userID,new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList == null){
                    Toast.makeText(getContext(),"Check your connection, no data was retrieved",Toast.LENGTH_LONG).show();
                    Log.d("Groups:","None");
                }else{
                    CustomGroupListview customGroupListview = new CustomGroupListview(getContext(), arrayList);
                    listView.setAdapter(customGroupListview);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(getActivity(),"item "+items[position],Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });

        return view;
    }
}
