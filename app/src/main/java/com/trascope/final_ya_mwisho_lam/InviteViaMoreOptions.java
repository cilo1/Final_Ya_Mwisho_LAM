package com.trascope.final_ya_mwisho_lam;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by cmigayi on 23-Nov-15.
 */
public class InviteViaMoreOptions extends Fragment implements View.OnClickListener {
    Button btnSMS,btnWhatsapp,btnEmail;
    Intent intent;
    LocalUserStorage localUserStorage;
    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_via_more_options,container,false);
        btnSMS = (Button) view.findViewById(R.id.btnSMS);
        btnSMS.setOnClickListener(this);
        btnWhatsapp = (Button) view.findViewById(R.id.btnWhatsapp);
        btnWhatsapp.setOnClickListener(this);
        btnEmail = (Button) view.findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(this);

        localUserStorage = new LocalUserStorage(getContext());
        user = localUserStorage.getLoggedInUser();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSMS:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address","");
                intent.putExtra("sms_body", "Hey there, have you tried LeaveAMark yet? I think" +
                        " you will like it. Check it out http://www.goodsclearanceclub.com/LAM_final/app-debug.apk");
                startActivity(intent);
                break;
            case R.id.btnEmail:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", user.email, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Join me on Leave A Mark");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey there, have you tried LeaveAMark yet? I think" +
                        " you will like it. Check it out http://www.goodsclearanceclub.com/LAM_final/app-debug.apk");
                startActivity(Intent.createChooser(intent, "Send email..."));
                break;
            case R.id.btnWhatsapp:
                PackageManager pm=getActivity().getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "Hey there, have you tried LeaveAMark yet? I think" +
                            " you will like it. Check it out http://www.goodsclearanceclub.com/LAM_final/app-debug.apk";

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }

        }

    }
}
