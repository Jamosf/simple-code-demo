package com.example.coolweather;

/**
 * Created by fengshangren on 16/8/17.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
