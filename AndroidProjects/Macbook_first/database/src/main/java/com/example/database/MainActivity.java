package com.example.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Mydatabasehelper mydatabasehelper;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button creat_btn = (Button) findViewById(R.id.btn_data);
        mydatabasehelper = new Mydatabasehelper(this,"BookStore",null,1);
        assert creat_btn != null;
        creat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydatabasehelper.getWritableDatabase();


            }
        });
        button = (Button)findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = mydatabasehelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name","the Da vinci code");
                values.put("author","dd");
                values.put("pages",454);
                values.put("price",16.96);
                db.insert("Book",null, values);
                values.clear();
            }
        });
        Button update = (Button) findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase up = mydatabasehelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);
                up.update("Book",values,"name = ?",new String[]{"the Da vinci code"});
                values.clear();
            }
        });
        Button delete_btn = (Button) findViewById(R.id.btn_delete);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase delete = mydatabasehelper.getWritableDatabase();
                delete.delete("Book","name = ?",new String[]{"the Da vinci code"});
            }
        });

        Button query_btn = (Button) findViewById(R.id.btn_check);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase query = mydatabasehelper.getWritableDatabase();
                Cursor cursor = query.query("Book",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("prcie"));

                    }while(cursor.moveToNext());
                }
            }
        });
        Button button3 = (Button) findViewById(R.id.btn_manage);
        assert button3 != null;
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dd = mydatabasehelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("category_id",1100);
                values.put("amount",11.1);
                dd.insert("Book",null,values);
                values.clear();
            }
        });
    }
}
