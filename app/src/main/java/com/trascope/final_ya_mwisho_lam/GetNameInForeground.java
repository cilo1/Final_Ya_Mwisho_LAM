package com.trascope.final_ya_mwisho_lam;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by cmigayi on 07-Dec-15.
 */
public class GetNameInForeground extends AbstractGetNameTask{

    public GetNameInForeground(MainActivity mActivity, String mEmail, String mScope) {
        super(mActivity, mEmail, mScope);
    }

    @Override
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (GooglePlayServicesAvailabilityException playEx) {

        } catch (UserRecoverableAuthException urae) {
            mActivity.startActivityForResult(urae.getIntent(), mRequest);
        } catch (GoogleAuthException fatalException) {
            fatalException.printStackTrace();
        }

        return null;
    }
}