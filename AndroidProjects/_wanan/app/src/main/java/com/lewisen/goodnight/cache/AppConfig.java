package com.lewisen.goodnight.cache;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 保存应用信息
 *
 * @author Administrator
 */
public class AppConfig {

    private SharedPreferences innerConfig;

    private static int defaultId = 10;

    public AppConfig(final Context context) {
        innerConfig = context.getSharedPreferences("app_config",
                Application.MODE_PRIVATE);
    }

    // 夜间模式
    public boolean isNightModeSwitch() {
        return innerConfig.getBoolean("night_mode_switch", false);
    }

    public void setNightModeSwitch(boolean on) {
        Editor editor = innerConfig.edit();
        editor.putBoolean("night_mode_switch", on);
        editor.apply();
    }

    //最大ID号管理
    public void setMaxId(JSONObject jsonObject) {
        int articlePageMaxId = 0;
        int homePageMaxId = 0;
        int picturePageMaxId = 0;
        if (jsonObject == null) return;

        try {
            articlePageMaxId = jsonObject.getInt("articlePageMaxId");
            homePageMaxId = jsonObject.getInt("homePageMaxId");
            picturePageMaxId = jsonObject.getInt("picturePageMaxId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Editor editor = innerConfig.edit();
        editor.putInt("articlePageMaxId", articlePageMaxId);
        editor.putInt("homePageMaxId", homePageMaxId);
        editor.putInt("picturePageMaxId", picturePageMaxId);
        editor.apply();
    }

    /**
     * 获取存储的最大ID值
     *
     * @param key homePageMaxId，articlePageMaxId，picturePageMaxId
     * @return value
     */
    public int getMaxId(String key) {
        return innerConfig.getInt(key, defaultId);
    }

    //记录是否是第一次启动
    public boolean isFirstStart() {
        return innerConfig.getBoolean("first_start", true);
    }

    public void setFirstStart(boolean on) {
        Editor editor = innerConfig.edit();
        editor.putBoolean("first_start", on);
        editor.apply();
    }

    //记录第二屏的路径
    public String getSecondPath() {
        return innerConfig.getString("second_path", " ");
    }

    public void setSecondPath(String path) {
        Editor editor = innerConfig.edit();
        editor.putString("second_path", path);
        editor.apply();
    }

    public String getSecondLastPath() {
        return innerConfig.getString("second_last_path", "");
    }

    public void setSecondLastPath(String path) {
        Editor editor = innerConfig.edit();
        editor.putString("second_last_path", path);
        editor.apply();
    }

    /**
     * 清空
     */
//    public void clear() {
//        Editor editor = innerConfig.edit();
//        editor.clear();
//        editor.apply();
//    }
}
