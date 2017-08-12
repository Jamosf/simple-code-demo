package com.lewisen.goodnight.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lewisen.goodnight.cache.AppConfig;
import com.lewisen.goodnight.player.Music;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.sdkmanager.LocationSDKManager;
import com.umeng.comm.ui.location.DefaultLocationImpl;

/**
 * ***开源***
 * 代码地址 https://github.com/Liukeshen1221/wanan
 * 作者：qq592614804
 * Created by Lewisen on 2015/9/4.
 */
public class MyApplication extends Application {
    public static RequestQueue requestQueue;
    public static AppConfig appConfig;
    public static CommunitySDK mCommSDK;
    public static Music music;

    @Override
    public void onCreate() {
        super.onCreate();
        Context mContext = getApplicationContext();

        //创建Volley请求队列对象
        requestQueue = Volley.newRequestQueue(mContext);

        //ActiveAndroid数据库初始化
        ActiveAndroid.initialize(mContext);

        //配置初始化
        appConfig = new AppConfig(mContext);

        //友盟社区初始化
        mCommSDK = CommunityFactory.getCommSDK(mContext);
        mCommSDK.initSDK(mContext);

        //image load 初始化配置
        ImageLoaderConfiguration imageConfig = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(4 * 1024 * 1024)
                .diskCacheSize(30 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(getApplicationContext(),
                        5 * 1000, 20 * 1000)).build();
        ImageLoader.getInstance().init(imageConfig);

        //高德地图
        LocationSDKManager.getInstance().addAndUse(new DefaultLocationImpl());

        //音乐
        music = new Music();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        //释放数据库
        ActiveAndroid.dispose();
    }
}
