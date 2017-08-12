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
import com.lewisen.goodnight.adapter.PictureListAdapter;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.bean.PicturePage;
import com.lewisen.goodnight.controller.LoadPageData;
import com.lewisen.goodnight.dataSrc.PageData;
import com.lewisen.goodnight.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.maxwin.view.XListView;


/**
 *
 */
public class PictureListFragment extends Fragment implements LoadPageData.LoadPageDataListener, XListView.IXListViewListener {
    private final int eachLoadPage = 8;//每次刷新加载的页面数量
    private final int minPageID = 25;//加载的最小ID值
    private final int type = PageData.TYPE_PICTURE;
    private XListView listView;
    private ArrayList<HashMap<String, String>> listItem;
    private PictureListAdapter pictureListAdapter;
    private LayoutInflater mInflater;
    private View pictureListView;
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
        pictureListAdapter = new PictureListAdapter(mContext);
        maxPageID = MyApplication.appConfig.getMaxId("picturePageMaxId");
        nowPageID = maxPageID;
        loadPageData = new LoadPageData(type, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        //获取界面布局
        pictureListView = mInflater.inflate(R.layout.list_view, container, false);
        listView = (XListView) pictureListView.findViewById(R.id.list_view_list);
        loadingBar = (ProgressBar) pictureListView.findViewById(R.id.loading_bar_list);

        pictureListAdapter.setArrayList(listItem);
        listView.setAdapter(pictureListAdapter);
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
        return pictureListView;
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
        PicturePage picturePage = (PicturePage) object;
        int index = 0;
        HashMap<String, String> map = new HashMap<>();

        if (picturePage != null) {
            index = maxPageID - picturePage.getPicturePageID();
            map.put("date", Util.dateFormat(picturePage.getDate()));
            map.put("image", picturePage.getImageSrc());
            map.put("text", picturePage.getText().split("。")[0] + "。");
        }

        try {
            listItem.set(index, map);
            count++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通知数据加载后刷新界面
        if (count >= eachLoadPage) {
            loadingBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            pictureListAdapter.notifyDataSetChanged();
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
            int id = MyApplication.appConfig.getMaxId("picturePageMaxId");
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
