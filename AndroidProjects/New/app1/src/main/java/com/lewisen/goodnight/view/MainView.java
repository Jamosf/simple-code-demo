package com.lewisen.goodnight.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.controller.Login;
import com.lewisen.goodnight.controller.MyDrawer;
import com.lewisen.goodnight.controller.MyFragmentTransaction;
import com.lewisen.goodnight.player.PlayerService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.update.UmengUpdateAgent;

public class MainView extends AppCompatActivity implements MyFragmentTransaction.onFragmentChange {
    private Context mContext;
    private long mExitTime = 0;//退出
    private MyFragmentTransaction myFragmentTransaction;
    private MyDrawer myDrawer;
    private Login login;
    final com.umeng.socialize.controller.UMSocialService umSocialService = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        mContext = this;
        initView();
        initShare();
        //自动更新
        UmengUpdateAgent.update(this);


    }

    private void initShare() {
        // 友盟调试日志
        com.umeng.socialize.utils.Log.LOG = false;
        umSocialService.getConfig().removePlatform(SHARE_MEDIA.SINA,
                SHARE_MEDIA.TENCENT);

        String appID = "wx17d82bc6091cf085";
        String appSecret = "712598c094b3bdc6dd8ab0f52681d9ec";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, appID, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appID, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // QQ平台
        String appIDQQ = "1104623639";
        String appKeyQQ = "PBc9cmkz3f71VCUE";
        // QQ
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, appIDQQ,
                appKeyQQ);
        qqSsoHandler.addToSocialSDK();
        // QQ空间
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, appIDQQ,
                appKeyQQ);
        qZoneSsoHandler.addToSocialSDK();

        umSocialService.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    /**
     * 视图初始化
     */
    private void initView() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        TextView loginText = (TextView) findViewById(R.id.login_text);
        ImageView userIconView = (ImageView) findViewById(R.id.user_icon);

        login = new Login(this, loginText, userIconView);

        myFragmentTransaction = new MyFragmentTransaction(mContext);
        myFragmentTransaction.init();

        myDrawer = new MyDrawer(mContext);
        myDrawer.init();
    }

    @Override
    protected void onResume() {
        login.displayUserInfo();

        //友盟统计
        MobclickAgent.onResume(mContext);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(mContext);
        super.onPause();
    }

    public void restoreActionBar(CharSequence mTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
//        Util.printInfo(" mDrawerToggle.syncState()");
        myDrawer.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Util.printInfo("onCreateOptionsMenu ");
        if (!myDrawer.isDrawerOpen()) {
            //getMenuInflater().inflate(R.menu.main_view, menu);
            restoreActionBar(myFragmentTransaction.getmTitle());
            return true;
        } else {
            restoreActionBar("晚安。");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDrawerClose() {
        myDrawer.closeDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁可能播放的音乐
        myDrawer.destoryDrawer();

        Intent intent = new Intent(mContext, PlayerService.class);
        stopService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = umSocialService.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
