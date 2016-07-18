package com.trascope.final_ya_mwisho_lam;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 20-Oct-15.
 */
interface UrlCallBack {
    public abstract void done(User user);
    public abstract void done(Boolean b);
    public abstract void done(ArrayList<HashMap<String,String>> arrayList);
    public abstract void done(ArrayList<HashMap<String,String>> arrayList, Commons ef);
}
