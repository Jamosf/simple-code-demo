package com.lewisen.goodnight.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.adapter.HomeListAdapter;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.bean.HomePage;
import com.lewisen.goodnight.controller.LoadPageData;
import com.lewisen.goodnight.dataSrc.PageData;
import com.lewisen.goodnight.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.maxwin.view.XListView;

public class HomeListFragment extends Fragment implements LoadPageData.LoadPageDataListener, XListView.IXListViewListener {
    private final int eachLoadPage = 8;//每次刷新加载的页面数量
    private final int minPageID = 25;//加载的最小ID值
    private final int type = PageData.TYPE_HOME;//加载页面类型
    private XListView listView;
    private ArrayList<HashMap<String, String>> listItem;
    private HomeListAdapter homeListAdapter;
    private LayoutInflater mInflater;
    private View homeListView;
    private Context mContext;
    private int maxPageID;//最大的页面ID
    private int nowPageID;//当前已经加载到的页面ID
    private int count = 0;//当前加载页面计数
    private LoadPageData loadPageData;
    private ProgressBar loadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItem = new ArrayList<>();
        mContext = getActivity();
        homeListAdapter = new HomeListAdapter(mContext);
        maxPageID = MyApplication.appConfig.getMaxId("homePageMaxId");
        nowPageID = maxPageID;
        loadPageData = new LoadPageData(type, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        //获取主页界面布局
        homeListView = mInflater.inflate(R.layout.list_view, container, false);
        listView = (XListView) homeListView.findViewById(R.id.list_view_list);
        loadingBar = (ProgressBar) homeListView.findViewById(R.id.loading_bar_list);

        homeListAdapter.setArrayList(listItem);
        listView.setAdapter(homeListAdapter);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        //设置刷新时间
        setRefreshTime();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Util.printInfo("click " + position);
                Intent intent = new Intent(mContext, DisplayDetailActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("pageID", maxPageID + 1 - position);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });
        loadSomePage();
        return homeListView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        loadPageData.stopLoadPageData();
        super.onDestroy();
    }


    /**
     * 每次加载页面调用的函数
     */
    public void loadSomePage() {
        for (int i = 0; i < eachLoadPage; i++) {
            listItem.add(null);
            loadPageData.startLoadPageData(nowPageID, false);
            nowPageID--;
        }
    }

    @Override
    public void onLoadPage(Object object) {
        HomePage homePage = (HomePage) object;
        int index = 0;
        HashMap<String, String> map = new HashMap<>();

        if (homePage != null) {
            index = maxPageID - homePage.getHomePageID();
            map.put("date", Util.dateFormat(homePage.getDate()));
            String[] title = homePage.getTitle().split("｜");
            map.put("category", title[0]);
            if (title.length > 1) {
                map.put("title", title[1]);
            }
            map.put("text", Util.getDisText(homePage.getText(), 50));
            String[] images = homePage.getImageSrc().split("###");
            String image = images[0];
            //用来取出imageSrc中有效的url
            if (((image.length() < 10)) && (images.length > 1)) {
                image = images[1];
            }
            if (((image.length() < 10)) && (images.length > 2)) {
                image = images[2];
            }
            map.put("image", image);
        }

        try {
            listItem.set(index, map);
            count++;
//            Util.printInfo("添加map = " + index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通知数据加载后刷新界面
        if (count >= eachLoadPage) {
            loadingBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            homeListAdapter.notifyDataSetChanged();
            onLoad();
        }
    }


    @Override
    public void onLoadError() {
        Toast.makeText(mContext, "获取更新失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadMaxID(boolean state) {
        onLoad();
        if (state) {
            int id = MyApplication.appConfig.getMaxId("homePageMaxId");
            if (maxPageID == id) {
                Toast.makeText(mContext, "当前已是最新内容~", Toast.LENGTH_LONG).show();
            } else {
                //清空现有数据 重新加载
                listItem.clear();
                maxPageID = id;
                nowPageID = maxPageID;
                loadSomePage();
            }
        } else {
            Toast.makeText(mContext, "获取更新失败", Toast.LENGTH_LONG).show();
        }
    }

    private void setRefreshTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        listView.setRefreshTime(format.format(new Date()));

    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        setRefreshTime();
    }

    //Xlistview回调方法
    @Override
    public void onRefresh() {
        loadPageData.startLoadMaxPageID();
    }

    @Override
    public void onLoadMore() {
        if (nowPageID <= minPageID) {
            Toast.makeText(mContext, "没有更多内容了", Toast.LENGTH_LONG).show();
        } else {
            loadSomePage();
        }
        onLoad();

    }
}
