package com.lewisen.goodnight.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.controller.DisplayImage;
import com.lewisen.goodnight.dataSrc.MyServer;
import com.lewisen.goodnight.view.MainView;

/**
 * 第二屏图片显示
 * Created by Lewisen on 2015/9/17.
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_main, null);
        ImageView secondImageView = (ImageView) view
                .findViewById(R.id.first_view);

        String last = MyApplication.appConfig.getSecondLastPath();
        String path = MyApplication.appConfig.getSecondPath();
        new DisplayImage().display(secondImageView, MyServer.PICTURE_URL + path);

        setContentView(view);
        if ((MyApplication.appConfig.isFirstStart()) ||
                (!last.equals(path))) {
            MyApplication.appConfig.setSecondLastPath(path);
            redirectTo();
        } else {
            // 渐变展示启动屏,这里通过动画来设置了开启应用程序的界面
            AlphaAnimation aa = new AlphaAnimation(0.8f, 1.0f);
            aa.setDuration(2000);
            // 给动画添加监听方法
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    redirectTo();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

            });
            view.startAnimation(aa);
        }
    }

    /**
     * 跳转到主界面的方法
     */
    private void redirectTo() {
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

}
