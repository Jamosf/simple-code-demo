package com.lewisen.goodnight.controller;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.player.Music;
import com.lewisen.goodnight.player.Player;
import com.lewisen.goodnight.view.MainView;

/**
 * 抽屉菜单管理
 * Created by Lewisen on 2015/9/8.
 */
public class MyDrawer {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private MainView mainView;
    private View mDrawerView;
    private Music music = MyApplication.music;
    private RelativeLayout musicLayout;
    private ImageButton imageButton;
    private TextView musicTime;
    private ProgressBar progressBar;
    private TextView author;
    private TextView title;
    private ImageView image;
    private Player player;
    private DisplayImage displayImage;


    public MyDrawer(Context mContext) {
        this.mContext = mContext;
        mainView = (MainView) mContext;
    }

    public void init() {
        displayImage = new DisplayImage();
        mDrawerLayout = (DrawerLayout) mainView.findViewById(R.id.drawer_layout);
        mDrawerView = mainView.findViewById(R.id.navigation_drawer);
        //抽屉栏音乐控件
        musicLayout = (RelativeLayout) mDrawerView.findViewById(R.id.part_music);

        imageButton = (ImageButton) mDrawerView.findViewById(R.id.music_button_d);
        musicTime = (TextView) mDrawerView.findViewById(R.id.music_time_d);
        progressBar = (ProgressBar) mDrawerView.findViewById(R.id.music_progress_d);
        author = ((TextView) mDrawerView.findViewById(R.id.music_author_d));
        title = ((TextView) mDrawerView.findViewById(R.id.music_title_d));
        image = (ImageView) mDrawerView.findViewById(R.id.music_icon_d);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                mainView,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mainView.invalidateOptionsMenu();
//                Util.printInfo("onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mainView.invalidateOptionsMenu();
//                Util.printInfo("onDrawerOpened");
                drawerMusic();
            }
        };
        //设置监听器
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //开启抽屉
        mDrawerLayout.openDrawer(mDrawerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
//                Util.printInfo("mDrawerLayout.post");
                mDrawerToggle.syncState();
            }
        });
    }


    private void drawerMusic() {
      /*抽屉栏 音乐播放器*/
        if (music.isPlaying()) {
            musicLayout.setVisibility(View.VISIBLE);
            ImageButton imageButton = (ImageButton) mDrawerView.findViewById(R.id.music_button_d);
            TextView musicTime = (TextView) mDrawerView.findViewById(R.id.music_time_d);
            ProgressBar progressBar = (ProgressBar) mDrawerView.findViewById(R.id.music_progress_d);
            author.setText(music.getAuthor());
            title.setText(music.getTitle());
            displayImage.display(image, music.getUrlImage(), 20);
            if (player == null) {
                player = new Player(mContext, music.getUrl(), imageButton, musicTime, progressBar);
                player.initPlayer();
            } else {
                player.reSetUrl(music.getUrl());
            }
        } else {
            if (player != null) {
                player.destory();
                player = null;
            }
            musicLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 退出播放时调用
     */
    public void destoryDrawer() {
        if (player != null) {
            player.stopMusic();
            player.destory();
            player = null;
        }
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerView);
    }

//    public void openDrawer() {
//        mDrawerLayout.openDrawer(mDrawerView);
//    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mDrawerView);
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

}
