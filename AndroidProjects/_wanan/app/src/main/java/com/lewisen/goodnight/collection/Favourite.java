package com.lewisen.goodnight.collection;

import android.content.Context;
import android.content.SharedPreferences;

import com.lewisen.goodnight.dataSrc.PageData;

import java.util.HashSet;
import java.util.Set;

/**
 * 收藏夹
 * Created by Lewisen on 2015/9/15.
 */
public class Favourite {
    private final String COLLECTED = "collected";//存储的文件名称
    private Context mContext;
    SharedPreferences preferences;


    public Favourite(Context mContext) {
        this.mContext = mContext;
        preferences = mContext.getSharedPreferences(COLLECTED, Context.MODE_PRIVATE);
    }

    /**
     * 添加一个收藏
     */
    public void addFavourite(int type, int pageID) {
        String key;
        if (type == PageData.TYPE_HOME) {
            key = "key=1";
        } else if (type == PageData.TYPE_ARTICLE) {
            key = "key=2";
        } else if (type == PageData.TYPE_PICTURE) {
            key = "key=3";
        } else {
            return;
        }
        Set<String> hashSet = getFavourite(type);
        if (hashSet == null) {
            return;
        }
        hashSet.add(pageID + "");
        preferences.edit().putStringSet(key, hashSet).apply();
    }

    /**
     * 删除一个收藏
     */
    public void deleteFavourite(int type, int pageID) {
        String key;
        if (type == PageData.TYPE_HOME) {
            key = "key=1";
        } else if (type == PageData.TYPE_ARTICLE) {
            key = "key=2";
        } else if (type == PageData.TYPE_PICTURE) {
            key = "key=3";
        } else {
            return;
        }
        Set<String> hashSet = getFavourite(type);
        if (hashSet == null) {
            return;
        }
        hashSet.remove(pageID + "");
        preferences.edit().putStringSet(key, hashSet).apply();
    }

    /**
     * 获取收藏内容
     *
     * @param type 类型
     * @return Set集合
     */
    public Set<String> getFavourite(int type) {
        Set<String> set;
        String key;
        if (type == PageData.TYPE_HOME) {
            key = "key=1";
        } else if (type == PageData.TYPE_ARTICLE) {
            key = "key=2";
        } else if (type == PageData.TYPE_PICTURE) {
            key = "key=3";
        } else {
            return null;
        }
        set = preferences.getStringSet(key, new HashSet<String>());
        return set;
    }

}
