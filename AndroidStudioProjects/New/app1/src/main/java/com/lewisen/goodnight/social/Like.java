package com.lewisen.goodnight.social;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.dataSrc.MyServer;
import com.lewisen.goodnight.dataSrc.PageData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lewisen on 2015/9/15.
 */
public class Like {
    private Context mContext;
    private int likeCount;
    private boolean likeState;
    private String item;
    private LikeListener likeListener;

    public Like(Context mContext, int type, int pageID, LikeListener likeListener) {
        if (type == PageData.TYPE_HOME) {
            item = "HomePage" + pageID;
        } else if (type == PageData.TYPE_ARTICLE) {
            item = "ArticlePage" + pageID;
        } else if (type == PageData.TYPE_PICTURE) {
            item = "PicturePage" + pageID;
        }
        this.mContext = mContext;
        this.likeListener = likeListener;
    }

    /**
     * 获取like的状态和数量
     */
    public void getLikeCount() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(MyServer.LIKE + "?order=get&item=" + item
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    likeCount = response.getInt("count");
                    likeState = getLikeItemState();
                    likeListener.onLikeEvent(likeCount, likeState);
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

    /**
     * 更改Like的状态
     *
     * @param count 1 喜欢次数+1； -1 取消喜欢状态
     */
    public void changeLikeState(int count) {
        if ((count == 1) || (count == -1)) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    MyServer.LIKE + "?order=update&item=" + item + "&count=" + count
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            MyApplication.requestQueue.add(jsonObjectRequest);

            saveLikeItemState(count == 1);
        }


    }

    /**
     * 保存喜欢状态
     *
     * @param state
     */
    private void saveLikeItemState(boolean state) {
        SharedPreferences preferences = mContext.getSharedPreferences("like",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(item, state);
        editor.apply();
    }

    /**
     * 获取已经存储的喜欢状态， false不喜欢，true喜欢
     *
     * @return
     */
    private boolean getLikeItemState() {
        SharedPreferences preferences = mContext.getSharedPreferences("like",
                Context.MODE_PRIVATE);
        return preferences.getBoolean(item, false);
    }

    public interface LikeListener {
        void onLikeEvent(int count, boolean state);
    }
}
