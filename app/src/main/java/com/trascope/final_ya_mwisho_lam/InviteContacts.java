package com.trascope.final_ya_mwisho_lam;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cmigayi on 21-Nov-15.
 */
public class InviteContacts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView lvContacts;
    TextView textTotalContacts;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_friends_from_contacts,container,false);
        lvContacts = (ListView) view.findViewById(R.id.listViewContacts);
        textTotalContacts = (TextView) view.findViewById(R.id.textTotalContacts);

        ArrayList<String> contacts = new ArrayList<>();
        ArrayList<HashMap<String,String>> contactsHm = new ArrayList<HashMap<String,String>>();

        ContentResolver cr = getActivity().getContentResolver();
        ContactsContract.CommonDataKinds.Phone Phone;

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        textTotalContacts.setText(cursor.getCount()+" Contacts");

           while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photo = ContactsContract.CommonDataKinds.Phone.PHOTO_URI;

               HashMap<String,String> hashMap = new HashMap<String,String>();
               hashMap.put("name",name);
               hashMap.put("number",number);
               hashMap.put("photo",photo);

               contactsHm.add(hashMap);
           }
        cursor.close();

        lvContacts.setAdapter(new CustomInviteContactsArrayAdapter(getContext(),contactsHm));

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
