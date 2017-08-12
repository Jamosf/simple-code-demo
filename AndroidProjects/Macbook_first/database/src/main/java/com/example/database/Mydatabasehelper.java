package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Mydatabasehelper extends SQLiteOpenHelper {

    public static final String creat_data = "Create table Book ("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text "
            + "category_id integer " +
            "amount real)";
    public static final String creat_data2 = "Create table Category ("
            + "id integer primary key autoincrement,"
            + "category_name text,"
            + "category_code real )";

    private Context mContext;

    public Mydatabasehelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creat_data);
        db.execSQL(creat_data2);
//        Toast.makeText(mContext,"create successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
//        onCreate(db);
        switch(oldVersion){
            case 6:
                db.execSQL("alter table Book add column category_id integer");
            case 7:
                db.execSQL("alter table Book add column amount real");
            default:
        }
    }
}
