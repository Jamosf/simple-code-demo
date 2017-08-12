package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fengshangren on 16/8/23.
 */
public class weatherActivity extends AppCompatActivity {

    private LinearLayout weatherInfo;

    private TextView cityNameText;

    private TextView publishText;

    private TextView weatherDespText;

    private TextView temp1Text;

    private TextView temp2Text;

    private TextView currentDateText;

    private Button switchCity;

    private Button refreshWeather;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weatherInfo = (LinearLayout) findViewById(R.id.weather_info_text);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_des);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_data);
        switchCity = (Button) findViewById(R.id.btn_switch);
        refreshWeather = (Button) findViewById(R.id.btn_refresh);
        String countryCode = getIntent().getStringExtra("country_code");
        if(!TextUtils.isEmpty(countryCode)){
            publishText.setText("同步中...");
            weatherInfo.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countryCode);
        }else{
            showWeather();
        }
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(weatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
            }
        });

        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weatherActivity.this);
                String weatherCode = prefs.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
            }
        });

    }

    private void queryWeatherCode(String country_code){
        String address = "http://www.weather.com.cn/data/list3/city" + country_code + ".xml";
        Toast.makeText(weatherActivity.this,country_code,Toast.LENGTH_SHORT).show();

        queryFromServer(address,"countryCode");
//        Toast.makeText(weatherActivity.this,"pppp",Toast.LENGTH_SHORT).show();

    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Toast.makeText(weatherActivity.this,"oooooo",Toast.LENGTH_SHORT).show();
        cityNameText.setText(prefs.getString("city_name",""));
        publishText.setText(prefs.getString("publish_text",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        currentDateText.setText(prefs.getString("current_date","") + "发布");
        weatherInfo.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        Toast.makeText(weatherActivity.this,weatherCode,Toast.LENGTH_SHORT).show();
        queryFromServer(address,"weatherCode");

    }

    private void queryFromServer(final String address,final String type){
//        Toast.makeText(weatherActivity.this,"bbbb",Toast.LENGTH_SHORT).show();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
//                Toast.makeText(weatherActivity.this,"aaaa",Toast.LENGTH_SHORT).show();
                if("countryCode".equals(type)){
//                    Toast.makeText(weatherActivity.this,"qqqq",Toast.LENGTH_SHORT).show();
                    if(!TextUtils.isEmpty(response)){
                        String [] array = response.split("\\|");
                        if(array != null && array.length > 0){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
//                            Toast.makeText(weatherActivity.this,"xxxxx",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(weatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
//                                Toast.makeText(weatherActivity.this,"llll",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });

    }



}
