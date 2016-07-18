package com.trascope.final_ya_mwisho_lam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 26-Oct-15.
 */
public class CustomPostsListview extends ArrayAdapter implements View.OnClickListener {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    ImageButton btnDelete;
    ServerRequest serverRequest;
    User user;
    LocalUserStorage localUserStorage;
    int postID;

    public CustomPostsListview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.posts_frag_listview_item, arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        serverRequest = new ServerRequest(context);
        localUserStorage = new LocalUserStorage(context);
        user = localUserStorage.getLoggedInUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.posts_frag_listview_item,parent,false);
        TextView tvPlaceName = (TextView) convertView.findViewById(R.id.tvPlaceName);
        TextView textTime = (TextView) convertView.findViewById(R.id.textTime);
        TextView textBucketlist = (TextView) convertView.findViewById(R.id.textBucketlist);
        TextView textLikes = (TextView) convertView.findViewById(R.id.textLikes);
        TextView textPostedBy = (TextView) convertView.findViewById(R.id.textPostedBy);
        ImageView imagePlace = (ImageView) convertView.findViewById(R.id.imagePlace);

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = arrayList.get(position);

        postID = Integer.parseInt(hashMap.get("content_id"));

        Picasso.with(context).load(hashMap.get("imageUrl")).into(imagePlace);
        textTime.setText(hashMap.get("timeAgo"));
        tvPlaceName.setText(hashMap.get("place"));
        textBucketlist.setText(hashMap.get("totalBucketlist")+" Bucketlist");
        textLikes.setText(hashMap.get("totalLikes")+" Likes");
        textPostedBy.setText("Post by You");

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        }
    }

    public void confirmDeleteDialog(final int itemID){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Post");
        builder.setMessage("Confirm to delete this post");
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSelectedPost(user.userID,itemID);
            }
        });
        builder.show();
    }

    public void deleteSelectedPost(int userID,int itemID){
        serverRequest.deleteUserPost(userID,itemID,new UrlCallBack() {
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
                if(ef.getStatus() != null){
                    Toast.makeText(getContext(),"Post deleted",Toast.LENGTH_LONG).show();
                    new Posts();
                }else{
                    Log.i("Bucketlist user:", "Failed");
                }
            }
        });
    }
}
