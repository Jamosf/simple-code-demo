package com.lewisen.goodnight.view;


import android.app.Activity;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.bean.ArticlePage;
import com.lewisen.goodnight.bean.HomePage;
import com.lewisen.goodnight.bean.PicturePage;
import com.lewisen.goodnight.collection.Favourite;
import com.lewisen.goodnight.controller.LoadPageData;
import com.lewisen.goodnight.dataSrc.PageData;
import com.lewisen.goodnight.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import me.maxwin.view.XListView;

/**
 *
 */
public class FavouriteFragment extends Fragment {
    private Context mContext;
    private XListView listView;
    private View favView;
    private TextView noContent;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, String>> listItem;
    private Favourite favourite;
    private ProgressBar loadingBar;
    private SimpleAdapter simpleAdapter;
    private LoadPageData loadHomePage;
    private LoadPageData loadPicPage;
    private LoadPageData loadArtPage;
    private int homeCount;
    private int artCount;
    private int picCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        listItem = new ArrayList<>();
        favourite = new Favourite(mContext);
        simpleAdapter = new SimpleAdapter(mContext, listItem, R.layout.favorite_list_item,
                new String[]{"title_item", "text_item"}, new int[]{R.id.title_item, R.id.text_item});

        loadPageInit();
    }

    private void loadPageInit() {
        loadHomePage = new LoadPageData(PageData.TYPE_HOME, new LoadPageData.LoadPageDataListener() {
            @Override
            public void onLoadPage(Object object) {
                HomePage homePage = (HomePage) object;
                homeCount--;
                if (homePage == null) return;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("type", "home");
                hashMap.put("pageID", homePage.getHomePageID() + "");
                hashMap.put("title_item", homePage.getTitle());
                hashMap.put("text_item", Util.getDisText(homePage.getText(), 50));
                listItem.add(hashMap);
                notifyAdapter();

            }

            @Override
            public void onLoadError() {

            }

            @Override
            public void onLoadMaxID(boolean state) {

            }
        });
        loadArtPage = new LoadPageData(PageData.TYPE_ARTICLE, new LoadPageData.LoadPageDataListener() {
            @Override
            public void onLoadPage(Object object) {
                ArticlePage articlePage = (ArticlePage) object;
                artCount--;
                if (articlePage == null) return;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("type", "article");
                hashMap.put("pageID", articlePage.getArticlePageID() + "");
                hashMap.put("title_item", articlePage.getTitle());
                hashMap.put("text_item", Util.getDisText(articlePage.getText(), 50));
                listItem.add(hashMap);
                notifyAdapter();

            }

            @Override
            public void onLoadError() {

            }

            @Override
            public void onLoadMaxID(boolean state) {

            }
        });
        loadPicPage = new LoadPageData(PageData.TYPE_PICTURE, new LoadPageData.LoadPageDataListener() {
            @Override
            public void onLoadPage(Object object) {
                PicturePage picturePage = (PicturePage) object;
                picCount--;
                if (picturePage == null) return;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("type", "picture");
                hashMap.put("pageID", picturePage.getPicturePageID() + "");
                hashMap.put("title_item", "图片");
                hashMap.put("text_item", Util.getDisText(picturePage.getText(), 50));
                listItem.add(hashMap);
                notifyAdapter();
            }

            @Override
            public void onLoadError() {

            }

            @Override
            public void onLoadMaxID(boolean state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyAdapter();
    }

    private void notifyAdapter() {
        //待页面加载完毕后 刷新界面
        if ((homeCount <= 0) && (artCount <= 0) && (picCount <= 0)) {
            simpleAdapter.notifyDataSetChanged();
            loadingBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        favView = mInflater.inflate(R.layout.list_view, container, false);
        listView = (XListView) favView.findViewById(R.id.list_view_list);
        loadingBar = (ProgressBar) favView.findViewById(R.id.loading_bar_list);
        noContent = (TextView) favView.findViewById(R.id.no_content);

        listView.setAdapter(simpleAdapter);
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = listItem.get(position - 1);
                if (hashMap == null) return;
                String ty = hashMap.get("type");
                int type;
                if ("home".equals(ty)) {
                    type = PageData.TYPE_HOME;
                } else if ("article".equals(ty)) {
                    type = PageData.TYPE_ARTICLE;
                } else if ("picture".equals(ty)) {
                    type = PageData.TYPE_PICTURE;
                } else {
                    return;
                }

                int pageID;
                try {
                    pageID = Integer.parseInt(hashMap.get("pageID"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                Intent intent = new Intent(mContext, DisplayDetailActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("pageID", pageID);
                //来自收藏栏目
                intent.putExtra("isFav", true);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });
        //开始加载收藏内容
        beginLoadPage();
        return favView;
    }


    private void beginLoadPage() {

        Set<String> homeSet = favourite.getFavourite(PageData.TYPE_HOME);
        homeCount = homeSet.size();
        for (String p : homeSet) {
            int pageID;
            try {
                pageID = Integer.parseInt(p);
                loadHomePage.startLoadPageData(pageID, false);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Set<String> artSet = favourite.getFavourite(PageData.TYPE_ARTICLE);
        artCount = artSet.size();
        for (String p : artSet) {
            int pageID;
            try {
                pageID = Integer.parseInt(p);
                loadArtPage.startLoadPageData(pageID, false);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Set<String> picSet = favourite.getFavourite(PageData.TYPE_PICTURE);
        picCount = picSet.size();
        for (String p : picSet) {
            int pageID;
            try {
                pageID = Integer.parseInt(p);
                loadPicPage.startLoadPageData(pageID, false);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if ((homeCount + picCount + artCount) == 0) {
            loadingBar.setVisibility(View.INVISIBLE);
            noContent.setVisibility(View.VISIBLE);
        }
    }
}
