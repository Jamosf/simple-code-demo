package com.lewisen.goodnight.controller;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.util.Util;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.login.LoginListener;

/**
 * 用户登陆控制
 * Created by Lewisen on 2015/9/7.
 */
public class Login implements View.OnClickListener, LoginListener {
    private TextView loginView;
    private ImageView userIconView;
    private Context mContext;
    private boolean isLogined;
    private DisplayImage displayImage;

    /**
     * 用户登陆构造函数
     *
     * @param mContext     context
     * @param loginView    登陆
     * @param userIconView 登陆后显示用户头像
     */
    public Login(Context mContext, TextView loginView, ImageView userIconView) {
        this.mContext = mContext;
        this.loginView = loginView;
        this.userIconView = userIconView;
        displayImage = new DisplayImage();
        this.loginView.setOnClickListener(this);
        this.userIconView.setOnClickListener(this);


    }

    /**
     * 如果已经登陆，则显示图片
     */
    public void displayUserInfo() {
        //获取当前登陆状态
        isLogined = MyApplication.mCommSDK.isLogined(mContext);
        CommUser commUser = MyApplication.mCommSDK.getConfig().loginedUser;
        String iconUrl = commUser.iconUrl;
        String name = commUser.name;

        if ((isLogined) && (!TextUtils.isEmpty(iconUrl))) {
            displayImage.display(userIconView, iconUrl, 180);
            loginView.setText(name);
        }
    }

    /**
     * 登陆按钮监听器
     */
    @Override
    public void onClick(View v) {
        Util.printInfo("login click");
        //调用友盟登陆
        if (isLogined) {
            Util.printInfo("已经登录");
        } else {
            MyApplication.mCommSDK.login(mContext, this);
        }
    }

    /**
     * 开始登陆授权
     */
    @Override
    public void onStart() {

    }

    /**
     * 登陆完成
     *
     * @param i        登陆
     * @param commUser 需是否为空，key name, iconUrl
     */
    @Override
    public void onComplete(int i, CommUser commUser) {
        displayUserInfo();
    }

}
