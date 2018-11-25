package com.example.mike.week5weekend;

import android.Manifest;
import android.content.ContentProvider;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contactsList;
    private RecyclerViewAdapter adapter;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<>();
        contactsList = findViewById( R.id.rvContactList );
        adapter = new RecyclerViewAdapter(contacts);
        contactsList.setAdapter(adapter);
        contactsList.setLayoutManager(new LinearLayoutManager(this));


        // Check permissions
        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            loadContacts();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            loadContacts();
        }
    }

    public void loadContacts(){

        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                        ContactsContract.Contacts.LOOKUP_KEY
                },
                null,
                null,
                null
        );

        if ( c!=null ){
            while ( c.moveToNext() ){
                // Load address
                Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                Cursor postal_cursor  = getContentResolver().query(postal_uri,null,  ContactsContract.Data.CONTACT_ID + "="+c.getString(0), null,null);
                String address = "";

                if ( postal_cursor != null ){
                    while( postal_cursor.moveToNext() )
                    {
                        String strt = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String cntry = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                        address += strt;

                        if ( city != null ){
                            address += ", "+city;
                        }
                        if ( cntry!= null ){
                            address += ", " + cntry;
                        }
                    }
                    postal_cursor.close();
                }

                contacts.add(new Contact(
                        c.getString(0),
                        c.getString(1),
                        address
                ));
            }
            c.close();
        }
        adapter.contacts = contacts;
        adapter.notifyDataSetChanged();
    }

}
