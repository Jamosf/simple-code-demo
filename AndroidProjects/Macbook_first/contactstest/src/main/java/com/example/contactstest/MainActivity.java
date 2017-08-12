package com.example.contactstest;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ArrayAdapter<String> adapter;

    List<String> contactsList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);

        listView.setAdapter(adapter);
//        Toast.makeText(this,"shujubuweikong",Toast.LENGTH_SHORT).show();


        readContacts();
//        Toast.makeText(this, "shujubuweikong", Toast.LENGTH_SHORT).show();
    }

    private void readContacts() {
        Cursor cursor = null;
//        Toast.makeText(this, "45677", Toast.LENGTH_SHORT).show();
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//            Toast.makeText(this, "1234", Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext()) {
                String dispalyname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(dispalyname + "\n" + number);
//                Toast.makeText(this, "shujubuweikong", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
//                Toast.makeText(this, "shujubuweikong", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
