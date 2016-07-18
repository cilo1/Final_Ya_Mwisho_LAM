package com.trascope.final_ya_mwisho_lam;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
public class CustomFeedsListview extends ArrayAdapter implements View.OnClickListener{
    String [] items;
    ArrayList<HashMap<String, String>> arrayList;
    ArrayList<HashMap<String, String>> oneUser;
    HashMap<String, String> hashMap;
    Context context;
    int posted_user_id, contentID,groupID;
    LayoutInflater layoutInflater;
    ViewHolder viewHolder;
    LocalUserStorage localUserStorage;
    ServerRequest serverRequest;
    User user;
    String TAG = "CustomAdapter:";
   // boolean refreshed;
    Commons commons;
    boolean favorite_status, backetlist_status,follow_status;

    CheckBox chkFamily,chkFriend,chkschoolMate,chkworkMate;

    public CustomFeedsListview(Context context, ArrayList<HashMap<String, String>> arrayList) {
        super(context, R.layout.feeds_frag_listview_item, arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
       // this.refreshed = refreshed;
        commons = new Commons();
        backetlist_status = false;
        favorite_status = false;
        follow_status = false;
        localUserStorage = new LocalUserStorage(context);
        serverRequest = new ServerRequest(context);
        user = localUserStorage.getLoggedInUser();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = null;

        Log.d(TAG, "position=" + position);

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.feeds_frag_listview_item, null);

            viewHolder = new ViewHolder();
            //viewHolder.holderPosition = position;
            viewHolder.textPostedBy = (TextView) convertView.findViewById(R.id.postedBy);
            viewHolder.textPlace = (TextView) convertView.findViewById(R.id.textPlace);
            viewHolder.timeUploaded = (TextView) convertView.findViewById(R.id.timeUploaded);
            viewHolder.textRelation = (TextView) convertView.findViewById(R.id.textRelation);
            viewHolder.textAbout = (TextView) convertView.findViewById(R.id.textAbout);
            viewHolder.mainImage = (ImageView) convertView.findViewById(R.id.mainImage);
            /*viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_on_item);
            viewHolder.shareItemBtn = (ImageButton) convertView.findViewById(R.id.share_item);
            viewHolder.favBtn = (ImageButton) convertView.findViewById(R.id.favoriteBtn);
            viewHolder.bucketlistBtn = (ImageButton) convertView.findViewById(R.id.bucketlistBtn);*/
            viewHolder.followBtn = (ImageButton) convertView.findViewById(R.id.followBtn);
            viewHolder.favoriteImage = (ImageButton) convertView.findViewById(R.id.favoriteImage);
            viewHolder.bucketlistImage = (ImageButton) convertView.findViewById(R.id.bucketlistImage);
            viewHolder.moreInfoBtn = (ImageButton) convertView.findViewById(R.id.moreInfoBtn);
            viewHolder.opentourBtn = (ImageButton) convertView.findViewById(R.id.opentourBtn);
            viewHolder.opentourText = (TextView) convertView.findViewById(R.id.opentourText);
            viewHolder.postedBy_img = (ImageView) convertView.findViewById(R.id.postedBy_img);
            viewHolder.totalLikes = (TextView) convertView.findViewById(R.id.totalLikes);
            viewHolder.totalBucketlist = (TextView) convertView.findViewById(R.id.totalBucketlist);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Object item = getItem(position);
        viewHolder.holderPosition = position-1;

        viewHolder.textPostedBy.setOnClickListener(this);
        viewHolder.textPostedBy.setTag(position);
        viewHolder.postedBy_img.setOnClickListener(this);
        viewHolder.postedBy_img.setTag(position);
       /* viewHolder.commentBtn.setOnClickListener(this);
        viewHolder.commentBtn.setTag(position);
        viewHolder.shareItemBtn.setOnClickListener(this);
        viewHolder.shareItemBtn.setTag(position);
        viewHolder.favBtn.setOnClickListener(this);
        viewHolder.favBtn.setTag(position);
        viewHolder.bucketlistBtn.setOnClickListener(this);
        viewHolder.bucketlistBtn.setTag(position);
        viewHolder.followBtn.setOnClickListener(this);
        viewHolder.followBtn.setTag(position);*/
        viewHolder.moreInfoBtn.setOnClickListener(this);
        viewHolder.moreInfoBtn.setTag(position);
        viewHolder.opentourBtn.setOnClickListener(this);
        viewHolder.opentourBtn.setTag(position);
        viewHolder.mainImage.setOnClickListener(this);
        viewHolder.mainImage.setTag(position);

        hashMap = new HashMap<String, String>();
        //hashMap = (HashMap<String, String>) item;
        hashMap = arrayList.get(position);

        posted_user_id = Integer.parseInt(hashMap.get("user_id"));
        contentID = Integer.parseInt(hashMap.get("content_id"));

        if(hashMap.get("about") == null){
            Toast.makeText(getContext(),"No data from server!",Toast.LENGTH_LONG).show();
        }else if(!hashMap.get("about").equals("") && hashMap.get("about").length()>1){
            viewHolder.textAbout.setText(hashMap.get("about"));
            viewHolder.textAbout.setVisibility(View.VISIBLE);
        }

        if(Integer.parseInt(hashMap.get("tour_id")) == -1){
            viewHolder.opentourBtn.setVisibility(View.GONE);
            viewHolder.opentourText.setVisibility(View.GONE);
            viewHolder.textAbout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }else{
            if(hashMap.get("tourStatus").equals("Closed")){
                viewHolder.opentourText.setText("Closed");
                viewHolder.opentourText.setTextColor(getContext().getResources().getColor(R.color.darkgrey));
            }else{
                viewHolder.opentourText.setText("Opened");
                viewHolder.opentourText.setTextColor(getContext().getResources().getColor(R.color.green));
            }
            viewHolder.opentourBtn.setVisibility(View.VISIBLE);
            viewHolder.opentourText.setVisibility(View.VISIBLE);
            viewHolder.textAbout.setLayoutParams(new LinearLayout.LayoutParams(500,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        viewHolder.textPostedBy.setText(hashMap.get("postedBy"));
        viewHolder.textPlace.setText(hashMap.get("place"));
        viewHolder.timeUploaded.setText(hashMap.get("timeAgo"));
        if(Boolean.parseBoolean(hashMap.get("likeStatus")) == true){
            viewHolder.favoriteImage.setBackgroundResource(R.drawable.starr_selected);
            viewHolder.totalLikes.setTextColor(Color.parseColor("#F73528"));
        }else{
            viewHolder.favoriteImage.setBackgroundResource(R.drawable.starr);
            viewHolder.totalLikes.setTextColor(Color.parseColor("#878787"));
        }
        viewHolder.totalLikes.setText("Likes "+hashMap.get("totalLikes"));
        if(Boolean.parseBoolean(hashMap.get("bucketlistStatus")) == true){
            viewHolder.bucketlistImage.setBackgroundResource(R.drawable.bucketlist_selected);
            viewHolder.totalBucketlist.setTextColor(Color.parseColor("#00af4d"));
        }else{
            viewHolder.bucketlistImage.setBackgroundResource(R.drawable.bucketlist);
            viewHolder.totalBucketlist.setTextColor(Color.parseColor("#878787"));
        }
        viewHolder.totalBucketlist.setText("Bucketlist "+hashMap.get("totalBucketlist"));

        if(Boolean.parseBoolean(hashMap.get("followingStatus")) == true){
            viewHolder.followBtn.setBackgroundResource(R.drawable.follow_selected);
        }else{
            viewHolder.followBtn.setBackgroundResource(R.drawable.not_friend);
        }
        Picasso.with(context).load(hashMap.get("userImage")).transform(new CircleTransform()).into(viewHolder.postedBy_img);
        Picasso.with(context).load(hashMap.get("imageUrl")).into(viewHolder.mainImage);

        if (hashMap.get("relation").toString().equals("none")) {
            viewHolder.textRelation.setVisibility(View.INVISIBLE);
        }
        if (hashMap.get("relation").toString().equals("Work") || hashMap.get("relation").toString().equals("Home")) {
            viewHolder.textRelation.setText("At " + hashMap.get("relation"));
        } else {
            viewHolder.textRelation.setText(hashMap.get("relation") + " Visit");
        }

        return convertView;
    }

    static class ViewHolder{
        TextView textPostedBy,textPlace,textRelation,textAbout,timeUploaded,totalLikes,totalBucketlist,opentourText;
        ImageButton favBtn, shareItemBtn,bucketlistBtn,followBtn,commentBtn,moreInfoBtn,opentourBtn,
                favoriteImage,bucketlistImage;
        ImageView mainImage,postedBy_img;
        int holderPosition;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer)v.getTag();
        Log.d("Tag","Tag:"+position);
        HashMap<String,String> map = new HashMap<>();
        map = arrayList.get(position);
        int selectedContentID = Integer.parseInt(map.get("content_id"));
        int selectedPostedUserID  = Integer.parseInt(map.get("user_id"));

        switch(v.getId()){
           /* case R.id.comment_on_item:
                Intent intent = new Intent(getContext(),UserCommentsActivity.class);
                intent.putExtra("hashmap",map);
                v.getContext().startActivity(intent);
                break;
            case R.id.share_item:
                Uri bmpUri = getLocalBitmapUri(viewHolder.mainImage);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    getContext().startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                    // ...sharing failed, handle error
                }
                break;*/
            case R.id.postedBy:
                Intent intentPostedBy = new Intent(getContext(),UserProfileActivity.class);
                intentPostedBy.putExtra("hashmap",map);
                getContext().startActivity(intentPostedBy);
                break;
            case R.id.postedBy_img:
                Intent intentPosted_img = new Intent(getContext(),UserProfileActivity.class);
                intentPosted_img.putExtra("hashmap",map);
                getContext().startActivity(intentPosted_img);
                break;
            /*case R.id.favoriteBtn:
                    likeUserPost(user.userID, selectedContentID);
                    Toast.makeText(getContext(),"You liked"+position+" "+selectedContentID,Toast.LENGTH_LONG).show();
                break;
            case R.id.bucketlistBtn:
                backetlistUserPost(user.userID, selectedContentID);
                if(getBacketlistStatus() == true){
                    viewHolder.bucketlistBtn.setBackgroundResource(R.drawable.bucketlist_selected);
                    Toast.makeText(getContext(),"You bucketlisted post",Toast.LENGTH_LONG).show();
                }else{
                    viewHolder.bucketlistBtn.setBackgroundResource(R.drawable.bucketlist);
                    Toast.makeText(getContext(),"You unbucketlisted post",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.followBtn:
                displayFollowUserSelectGroup(context,map);
                followUser(user.userID,selectedPostedUserID,groupID);
                if(getFollowStatus() == true){
                    viewHolder.followBtn.setBackgroundResource(R.drawable.follow_selected);
                }else{
                    viewHolder.followBtn.setBackgroundResource(R.drawable.follow);
                }
                break;*/
            case R.id.moreInfoBtn:
                selectMoreInfo();
                break;
            case R.id.mainImage:
                //displayFeedItem(context,map);
                hashMap = arrayList.get(position);
                Intent intent = new Intent(getContext(),FeedItemActivity.class);
                intent.putExtra("hashmap",map);
                context.startActivity(intent);
                break;
        }
    }
    public void setBacketlistStatus(boolean status){
        this.backetlist_status = status;
    }
    public void setFollowStatus(boolean status){
        this.follow_status = status;
    }
    public boolean getFavoriteStatus(){
        return favorite_status;
    }
    public boolean getBacketlistStatus(){
        return backetlist_status;
    }
    public boolean getFollowStatus(){
        return follow_status;
    }
    public void backetlistUserPost(int userID, int contentID){
        serverRequest.backetlistUserPost(userID,contentID,new UrlCallBack() {
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
                    setBacketlistStatus(ef.status);
                }else{
                    Log.i("Bucketlist user:","Failed");
                }
            }
        });
    }
    public void likeUserPost(int userID, int contentID){
        serverRequest.likeUserPost(userID,contentID, new UrlCallBack() {
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
                    Log.i("favorited:",Boolean.toString(ef.status));
                    if(ef.status==true){
                        viewHolder.favBtn.setBackgroundResource(R.drawable.starr_selected);
                        Toast.makeText(getContext(),"You liked",Toast.LENGTH_LONG).show();
                    }else{
                        viewHolder.favBtn.setBackgroundResource(R.drawable.starr);
                        Toast.makeText(getContext(),"You Unliked",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.i("Favorite user:","Failed");
                }
            }
        });
    }
    public void followUser(int userID,int postedByID,int groupID){
      serverRequest.followUser(userID,postedByID,groupID,new UrlCallBack() {
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
                  setFollowStatus(ef.status);
              }else{
                  Log.i("Follow user:","Failed");
              }
          }
      });
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void selectMoreInfo(){
        final CharSequence[] items = {"Tag a friend","Report this post","Exit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Report")) {
                    Intent intent = new Intent(getContext(), Report.class);
                    intent.putExtra("hashmap", hashMap);
                    getContext().startActivity(intent);
                } else if (items[item].equals("Exit")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void displayFeedItem(Context context,HashMap<String, String> hashMap){
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(hashMap.get("place"));
        dialog.setContentView(R.layout.dialog_feed_item);
        ImageView imageMain = (ImageView) dialog.findViewById(R.id.imageMain);
        TextView textPlace = (TextView) dialog.findViewById(R.id.textPlace);
        TextView textAbout = (TextView) dialog.findViewById(R.id.textAbout);
        TextView textTime = (TextView) dialog.findViewById(R.id.textTime);

        Picasso.with(getContext()).load(hashMap.get("imageUrl")).into(imageMain);
        if(hashMap.get("about") == null){
            textAbout.setVisibility(View.GONE);
        }else{
            textAbout.setText("\""+hashMap.get("about")+"\"");
        }
        textPlace.setText("Posted by "+hashMap.get("postedBy"));
        textTime.setText(hashMap.get("timeAgo"));
        dialog.show();
    }

    public void displayFollowUserSelectGroup(Context context,HashMap<String, String> hashMap){
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Follow " + hashMap.get("postedBy"));
        dialog.setContentView(R.layout.dialog_follow_user);
        TextView text = (TextView) dialog.findViewById(R.id.textView);
        ImageView userImg = (ImageView) dialog.findViewById(R.id.userImg);
        Button btnDialogFollow = (Button) dialog.findViewById(R.id.btnDialogFollow);

        chkFamily = (CheckBox) dialog.findViewById(R.id.chkFamily);
        chkFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 1;
                chkFriend.setChecked(false);
                chkschoolMate.setChecked(false);
                chkworkMate.setChecked(false);
            }
        });
        chkFriend = (CheckBox) dialog.findViewById(R.id.chkFriend);
        chkFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 2;
                chkFamily.setChecked(false);
                chkschoolMate.setChecked(false);
                chkworkMate.setChecked(false);
            }
        });
        chkschoolMate = (CheckBox) dialog.findViewById(R.id.chkschoolMate);
        chkschoolMate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 4;
                chkFriend.setChecked(false);
                chkFamily.setChecked(false);
                chkworkMate.setChecked(false);
            }
        });
        chkworkMate = (CheckBox) dialog.findViewById(R.id.chkworkMate);
        chkworkMate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupID = 3;
                chkFriend.setChecked(false);
                chkschoolMate.setChecked(false);
                chkFamily.setChecked(false);
            }
        });

        text.setText("Who is "+hashMap.get("postedBy")+" to you?");
        Picasso.with(context).load(hashMap.get("userImage")).transform(new CircleTransform()).into(userImg);

        btnDialogFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set
            }
        });

        dialog.show();
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}
