package com.lewisen.goodnight.view;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.adapter.ArticleListAdapter;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.bean.ArticlePage;
import com.lewisen.goodnight.controller.LoadPageData;
import com.lewisen.goodnight.dataSrc.PageData;
import com.lewisen.goodnight.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.maxwin.view.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleListFragment extends Fragment implements LoadPageData.LoadPageDataListener, XListView.IXListViewListener {
    private final int eachLoadPage = 8;//每次刷新加载的页面数量
    private final int minPageID = 25;//加载的最小ID值
    private final int type = PageData.TYPE_ARTICLE;//界面类型
    private XListView listView;
    private ArrayList<HashMap<String, String>> listItem;
    private ArticleListAdapter articleListAdapter;
    private LayoutInflater mInflater;
    private View articleListView;
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
        articleListAdapter = new ArticleListAdapter(mContext);
        maxPageID = MyApplication.appConfig.getMaxId("articlePageMaxId");
        nowPageID = maxPageID;
        loadPageData = new LoadPageData(type, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        //获取主页界面布局
        articleListView = mInflater.inflate(R.layout.list_view, container, false);
        listView = (XListView) articleListView.findViewById(R.id.list_view_list);
        loadingBar = (ProgressBar) articleListView.findViewById(R.id.loading_bar_list);

        articleListAdapter.setArrayList(listItem);
        listView.setAdapter(articleListAdapter);
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
        return articleListView;
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
        ArticlePage articlePage = (ArticlePage) object;
        int index = 0;
        HashMap<String, String> map = new HashMap<>();

        if (articlePage != null) {
            index = maxPageID - articlePage.getArticlePageID();
            map.put("date", Util.dateFormat(articlePage.getDate()));
            map.put("title", articlePage.getTitle());
            //新提交内容 在列表里面显示 没有就显示文章的前100字
            map.put("text", articlePage.getComment().length() > 10 ? articlePage.getComment() : Util.getDisText(articlePage.getText(), 100));
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
            articleListAdapter.notifyDataSetChanged();
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
            int id = MyApplication.appConfig.getMaxId("articlePageMaxId");
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
