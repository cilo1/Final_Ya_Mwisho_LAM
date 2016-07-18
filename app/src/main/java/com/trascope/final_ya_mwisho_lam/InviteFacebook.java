package com.trascope.final_ya_mwisho_lam;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;

import bolts.AppLinks;

/**
 * Created by cmigayi on 23-Nov-15.
 */
public class InviteFacebook extends Fragment {

    Button inviteBtn;
    Commons commons;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_facebook,container,false);
        inviteBtn = (Button) view.findViewById(R.id.inviteBtn);

        commons = new Commons();

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean network = commons.checkInternetConnetion(getContext());
                if(network == true){
                    inviteFriends();
                }else{
                    Toast.makeText(getContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void inviteFriends(){
        String appLinkUrl, previewImageUrl;
        appLinkUrl = "http://www.goodsclearanceclub.com/LAM_final/app-debug.apk"; // this applink url you will get from facebook developer when you register your application there
        previewImageUrl = "http://www.goodsclearanceclub.com/LAM_final/facebook_image.png"; // this is the image link which you needed to post on app invite
        if (AppInviteDialog.canShow())
        {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
        else
        {
            Toast.makeText(getActivity(), "Native facebook app is missing", Toast.LENGTH_SHORT).show();
        }
    }


}
