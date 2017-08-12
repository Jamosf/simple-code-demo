package com.lewisen.goodnight.controller;

import android.os.Handler;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.dataSrc.MyServer;

import org.json.JSONObject;

/**
 * ID管理
 * Created by Lewisen on 2015/9/9.
 */
public class IDManage {
    public static final int ID_SUCCEED = 1;
    public static final int ID_FAIL = -1;

    /**
     * @param handler 不指定handler 为null
     */
    public static void setMaxID(final Handler handler) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(MyServer.MAX_ID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyApplication.appConfig.setMaxId(response);
                        if (handler != null) {
                            handler.obtainMessage(ID_SUCCEED).sendToTarget();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (handler != null) {
                    handler.obtainMessage(ID_FAIL).sendToTarget();
                }
            }
        });

        //设置超时时间20s  重试1次
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        MyApplication.requestQueue.add(jsonObjectRequest);
    }
}
