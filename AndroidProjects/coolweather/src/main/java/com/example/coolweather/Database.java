package com.example.coolweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengshangren on 16/8/17.
 */
public class Database {

    //数据库名
    public static final String DB_NAME = "cool_weather";

    //数据库版本
    public static final int VERSION = 1;

    private static Database database;

    private SQLiteDatabase db;


    //私有化构造函数
    private Database(Context context){
        DatabaseHelper dhelper = new DatabaseHelper(context,DB_NAME,null,VERSION);
        db = dhelper.getWritableDatabase();
    }

    //单例设计模式，只产生一个实例

    public synchronized static Database getInstance(Context context){
        if(database == null){
            database = new Database(context);
        }
        return database;
    }

    //建立数据表
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("province",null,values);
        }
    }

    //读取数据
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());

            }

        if(cursor != null){
            cursor.close();

        }
        return list;
    }


    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("provinceId",city.getProvinceId());
            db.insert("city",null,values);
        }
    }

    //读取数据
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("city",null,"provinceId = ?",new String [] {String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provinceId")));
                list.add(city);
            } while (cursor.moveToNext());

        }
            if(cursor != null){
                cursor.close();
            }

        return list;

    }


    public void saveCountry(Country country){
        if(country != null){
            ContentValues values = new ContentValues();
            values.put("country_name",country.getCountryName());
            values.put("country_code",country.getCountryCode());
            values.put("cityId",country.getCityId());
            db.insert("country",null,values);
        }
    }

    //读取数据
    public List<Country> loadCountry(int cityId){
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("country",null,"cityId = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));
                list.add(country);
            } while (cursor.moveToNext());

        }
        if(cursor != null){
            cursor.close();
        }

        return list;

    }


}
