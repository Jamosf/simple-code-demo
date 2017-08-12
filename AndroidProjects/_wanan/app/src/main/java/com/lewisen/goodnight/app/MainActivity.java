package com.lewisen.goodnight.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.controller.IDManage;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * ***开源***
 * 代码地址 https://github.com/Liukeshen1221/wanan
 * 作者：qq592614804
 * Created by Lewisen on 2015/9/4.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_main, null);
        view.findViewById(R.id.first_text).setVisibility(View.VISIBLE);
        view.findViewById(R.id.wanan_image).setVisibility(View.VISIBLE);
        view.findViewById(R.id.wanan_text).setVisibility(View.VISIBLE);
        setContentView(view);

        // 渐变展示启动屏,这里通过动画来设置了开启应用程序的界面
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1500);
        // 给动画添加监听方法
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                OnlineConfigAgent.getInstance().updateOnlineConfig(MainActivity.this);
                String adSwitch = OnlineConfigAgent.getInstance().getConfigParams(MainActivity.this, "adSwitch");

                if ("true".equals(adSwitch)) {// 显示广告
                    //跳转到广告
                    redirectToAD();
                } else {
                    redirectToSecond();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });

        view.startAnimation(aa);
        //加载最大值
        IDManage.setMaxID(null);
        //加载第二屏图片
        new SecondImage().loadSecondImagePath();
    }

    /**
     * 跳转到第二屏界面的方法
     */
    private void redirectToSecond() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

    private void redirectToAD() {
        Intent intent = new Intent(this, CSplashActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }


}
