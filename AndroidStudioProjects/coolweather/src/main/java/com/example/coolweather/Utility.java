package com.example.coolweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fengshangren on 16/8/17.
 */
public class Utility {

    //解析和处理服务器返回的数据
    public synchronized static boolean handleProvincesResponse(Database database,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p : allProvinces){
                    String[] arry = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(arry[0]);
                    province.setProvinceName(arry[1]);
                    database.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }


    public synchronized static boolean handleCitiesResponse(Database database,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p : allProvinces){
                    String[] arry = p.split("\\|");
                    City city = new City();
                    city.setCityCode(arry[0]);
                    city.setCityName(arry[1]);
                    city.setProvinceId(provinceId);
                    database.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountriesResponse(Database database,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p : allProvinces){
                    String[] arry = p.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(arry[0]);
                    country.setCountryName(arry[1]);
                    country.setCityId(cityId);
                    database.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    //解析服务器返回JSON的数据
    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherInfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){
        SimpleDateFormat datatime = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",datatime.format(new Date()));
        editor.commit();
    }

}
