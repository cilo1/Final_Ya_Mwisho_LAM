package com.trascope.final_ya_mwisho_lam;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 26-Oct-15.
 */
public class Posts extends Fragment {
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;
    ListView listView;
    LinearLayout loadingLayout;
    CustomPostsListview customPostsListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts,container,false);
        listView = (ListView) view.findViewById(R.id.listView);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadinglayout);
        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();
        serverRequest = new ServerRequest(getContext());
        serverRequest.fetchUserPosts(user, new UrlCallBack() {
            @Override
            public void done(User user) {  }

            @Override
            public void done(Boolean b) {

            }

            @Override
            public void done(final ArrayList<HashMap<String, String>> arrayList) {
                if(arrayList==null){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
                    //textNoData.setVisibility(View.VISIBLE);
                    //listView.setVisibility(View.INVISIBLE);
                }else{
                    customPostsListview = new CustomPostsListview(getActivity(),arrayList);
                    listView.setAdapter(customPostsListview);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap = arrayList.get(position);

                            displaySelectedItem(hashMap,getContext());
                        }
                    });
                    loadingLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void done(ArrayList<HashMap<String, String>> arrayList, Commons ef) {}
        });

        return  view;
    }

    private void displaySelectedItem(HashMap<String, String> hashMap,Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_post_item_selected);
        dialog.setCancelable(false);
        ImageView imagePlace = (ImageView) dialog.findViewById(R.id.imagePlace);
        ImageButton btnCancel = (ImageButton) dialog.findViewById(R.id.btnCancel);
        TextView textTime = (TextView) dialog.findViewById(R.id.textTime);
        final TextView comments = (TextView) dialog.findViewById(R.id.comments);
        final TextView commentBy = (TextView) dialog.findViewById(R.id.commentBy);
        TextView commentsTitle = (TextView) dialog.findViewById(R.id.commentsTitle);
        Button moreComments = (Button) dialog.findViewById(R.id.moreComments);
        //TextView tvPlaceName = (TextView) dialog.findViewById(R.id.tvPlaceName);
        Picasso.with(context).load(hashMap.get("imageUrl")).into(imagePlace);
        textTime.setText(hashMap.get("timeAgo"));
        dialog.setTitle(hashMap.get("place"));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //tvPlaceName.setText(hashMap.get("place"));
        final String [] commentDataBy = {
                "Jdoe","Lily","Aanold","Rolsal"
        };
        final String [] commentData = {
                "Jdoe","Lily","Aanold","Rolsal"
        };
        final int length = commentDataBy.length;
        commentsTitle.setText("Comments ("+length+")");
        if(length>0){
            if(length == 1){
                comments.setText(commentData[0]);
                commentBy.setText(commentDataBy[0]);
                moreComments.setVisibility(View.INVISIBLE);
            }
            comments.setText(commentData[0]);
            commentBy.setText(commentDataBy[0]);
        }
        moreComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<length;i++){
                    comments.setText(commentData[i]);
                    commentBy.setText(commentDataBy[i]);
                }
            }
        });
        dialog.show();
    }
}
