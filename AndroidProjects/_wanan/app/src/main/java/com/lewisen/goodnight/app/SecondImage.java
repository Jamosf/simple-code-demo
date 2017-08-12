package com.lewisen.goodnight.app;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lewisen.goodnight.dataSrc.MyServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 启动第二屏 图片
 * Created by Lewisen on 2015/9/17.
 */
public class SecondImage {


    public void loadSecondImagePath() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(MyServer.SECOND_IMAGE + "?order=get",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String path = "";
                            String last = "";
                            path = response.getString("path");
                            last = MyApplication.appConfig.getSecondPath();
                            if (!path.equals(last)) {
                                MyApplication.appConfig.setSecondLastPath(last);
                                MyApplication.appConfig.setSecondPath(path);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.requestQueue.add(jsonObjectRequest);

    }
}
