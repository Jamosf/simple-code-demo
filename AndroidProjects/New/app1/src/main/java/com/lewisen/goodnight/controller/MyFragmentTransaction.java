package com.lewisen.goodnight.controller;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.view.AboutMeFragment;
import com.lewisen.goodnight.view.ArticleListFragment;
import com.lewisen.goodnight.view.FavouriteFragment;
import com.lewisen.goodnight.view.HomeListFragment;
import com.lewisen.goodnight.view.MainView;
import com.lewisen.goodnight.view.PictureListFragment;
import com.lewisen.goodnight.view.ShakeRandomFragment;
import com.umeng.comm.ui.fragments.CommunityMainFragment;

/**
 * Fragment切换控制
 * Created by Lewisen on 2015/9/8.
 */
public class MyFragmentTransaction implements View.OnClickListener {
    private Button homeButton;
    private Button articleButton;
    private Button pictureButton;
    private Button communityButton;
    private Button collectionButton;
    private Button aboutButton;
    private Button randomButton;
    private Context mContext;
    private CharSequence mTitle;
    //由mainView实现的监听器调用接口
    private onFragmentChange fragmentChange;
    private FragmentManager fragmentManager;
    //显示的fragment
    private CommunityMainFragment commFragment;
    private HomeListFragment homeListFragment;
    private ArticleListFragment articleListFragment;
    private PictureListFragment pictureListFragment;
    private FavouriteFragment favouriteFragment;
    private AboutMeFragment aboutMeFragment;
    private ShakeRandomFragment shakeRandomFragment;

    public MyFragmentTransaction(Context mContext) {
        this.mContext = mContext;
        Activity activity = (Activity) mContext;
        homeButton = (Button) activity.findViewById(R.id.home_button);
        articleButton = (Button) activity.findViewById(R.id.article_button);
        pictureButton = (Button) activity.findViewById(R.id.picture_button);
        communityButton = (Button) activity.findViewById(R.id.community_button);
        collectionButton = (Button) activity.findViewById(R.id.collection_button);
        aboutButton = (Button) activity.findViewById(R.id.about_button);
        randomButton = (Button) activity.findViewById(R.id.random_button);

        fragmentChange = (onFragmentChange) mContext;
    }

    public void init() {

        homeButton.setOnClickListener(this);
        articleButton.setOnClickListener(this);
        pictureButton.setOnClickListener(this);
        communityButton.setOnClickListener(this);
        collectionButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);
        fragmentManager = ((MainView) mContext).getSupportFragmentManager();

        //第一次启动 不显示主页栏目
        if (MyApplication.appConfig.isFirstStart()) {
            MyApplication.appConfig.setFirstStart(false);
        } else {
            showFragment(1);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_button:
                showFragment(1);
                break;
            case R.id.article_button:
                showFragment(2);
                break;
            case R.id.picture_button:
                showFragment(3);
                break;
            case R.id.community_button:
                showFragment(4);
                break;
            case R.id.random_button:
                showFragment(5);
                break;
            case R.id.collection_button:
                showFragment(6);
                break;
            case R.id.about_button:
                showFragment(7);
                break;
        }
        //调用mainview接口关闭drawer
        fragmentChange.onDrawerClose();
    }

    public CharSequence getmTitle() {
        return mTitle;
    }

    public interface onFragmentChange {
        void onDrawerClose();
    }

    private void showFragment(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (index) {
            case 1:
                if (homeListFragment != null)
                    transaction.replace(R.id.container_main_view, homeListFragment);
                else {
                    homeListFragment = new HomeListFragment();
                    transaction.replace(R.id.container_main_view, homeListFragment);
                }
                mTitle = "主页";
                break;
            case 2:
                if (articleListFragment != null)
                    transaction.replace(R.id.container_main_view, articleListFragment);
                else {
                    articleListFragment = new ArticleListFragment();
                    transaction.replace(R.id.container_main_view, articleListFragment);
                }
                mTitle = "文章";
                break;
            case 3:
                if (pictureListFragment != null)
                    transaction.replace(R.id.container_main_view, pictureListFragment);
                else {
                    pictureListFragment = new PictureListFragment();
                    transaction.replace(R.id.container_main_view, pictureListFragment);
                }
                mTitle = "图片";
                break;
            case 4:
                if (commFragment != null)
                    transaction.replace(R.id.container_main_view, commFragment);
                else {
                    commFragment = new CommunityMainFragment();
                    commFragment.setBackButtonVisibility(View.INVISIBLE);
                    transaction.replace(R.id.container_main_view, commFragment);
                }
                mTitle = "社区";
                break;

            case 5:
                if (shakeRandomFragment != null)
                    transaction.replace(R.id.container_main_view, shakeRandomFragment);
                else {
                    shakeRandomFragment = new ShakeRandomFragment();
                    transaction.replace(R.id.container_main_view, shakeRandomFragment);
                }
                mTitle = "旧时光";
                break;

            case 6:
                if (favouriteFragment != null)
                    transaction.replace(R.id.container_main_view, favouriteFragment);
                else {
                    favouriteFragment = new FavouriteFragment();
                    transaction.replace(R.id.container_main_view, favouriteFragment);
                }
                mTitle = "收藏";
                break;
            case 7:
                if (aboutMeFragment != null)
                    transaction.replace(R.id.container_main_view, aboutMeFragment);
                else {
                    aboutMeFragment = new AboutMeFragment();
                    transaction.replace(R.id.container_main_view, aboutMeFragment);
                }
                mTitle = "关于";
                break;
        }
        transaction.commit();
    }

}
