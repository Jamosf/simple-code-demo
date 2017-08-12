package com.example.coolweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fengshangren on 16/8/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_PROVINCE = "create table province (" +
            "id integer primary key autoincrement," +
            "province_name Text," +
            "province_code Text)";

    public static final String CREATE_CITY = "create table city (" +
            "id integer primary key autoincrement," +
            "city_name Text," +
            "city_code Text," +
            "provinceId integer)";

    public static final String CREATE_COUNTRY = "create table country (" +
            "id integer primary key autoincrement," +
            "country_name Text," +
            "country_code Text," +
            "cityId integer)";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL(CREATE_PROVINCE);
       sqLiteDatabase.execSQL(CREATE_CITY);
       sqLiteDatabase.execSQL(CREATE_COUNTRY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
