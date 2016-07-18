package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 21-Oct-15.
 */
public class SearchPeople extends Fragment{
    GridView gridView;
    SearchView searchView;
    TextView noData;
    String [] items = {"J.More","Aanol Sulpher","Kandle Lee","Abram"};
    CustomSearchPeopleGridview customSearchPeopleGridview;
    ServerRequest serverRequest;
    Commons commons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_people, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        noData = (TextView) view.findViewById(R.id.nodata);

        commons = new Commons();
        serverRequest = new ServerRequest(getContext());
        boolean network = commons.isNetworkAvailable(getContext());

        if(network == true){
            getPeople();
        }else{
            Toast.makeText(getContext(),"Network is not available!",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void getPeople(){
        serverRequest.fetchAllUsersData(new UrlCallBack() {
            @Override
            public void done(User user) {

            }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(final ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList != null){
                    //ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,
                    // getArray(arrayList));
                    //editSearch.setAdapter(arrayAdapter);
                    customSearchPeopleGridview = new CustomSearchPeopleGridview(getActivity(),arrayList);
                    gridView.setAdapter(customSearchPeopleGridview);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap = arrayList.get(position);
                            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                            intent.putExtra("hashmap", hashMap);
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
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {

            }
        });

    }
}
