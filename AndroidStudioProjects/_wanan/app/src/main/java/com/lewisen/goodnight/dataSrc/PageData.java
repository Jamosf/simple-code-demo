package com.lewisen.goodnight.dataSrc;

import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.bean.ArticlePage;
import com.lewisen.goodnight.bean.HomePage;
import com.lewisen.goodnight.bean.PicturePage;

import org.json.JSONObject;


/**
 * 获取数据  来自数据库或者网络
 * Created by Lewisen on 2015/9/9.
 */
public class PageData {
    public static final int NULL_DB = -10;
    public static final int SUCCEED_DB = 10;
    public static final int FAIL_NET = -11;
    public static final int SUCCEED_NET = 11;
    public static final int TYPE_HOME = 1;
    public static final int TYPE_ARTICLE = 2;
    public static final int TYPE_PICTURE = 3;
    private int reTry = 0;//联网重试
    private boolean isCancel;//取消所有数据请求

    private RequestQueue requestQueue = MyApplication.requestQueue;
    private Handler mHandler;

    public PageData(Handler mHandler) {
        isCancel = false;
        this.mHandler = mHandler;
    }

    /**
     * 从数据库加载数据
     *
     * @param type   类型
     * @param pageID 编号
     */
    public void getDataFromDb(final int type, final int pageID) {
        //加载数据库  需要在线程操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (type == 1) {
                    HomePage homePage = (HomePage) OperateDb.getPage(new HomePage(), pageID);
                    if (isCancel) return;
                    if (homePage == null) {
                        //如果数据库不存在该数据，则发送为空的ID
                        mHandler.obtainMessage(NULL_DB, pageID).sendToTarget();
                    } else {
                        mHandler.obtainMessage(SUCCEED_DB, homePage).sendToTarget();
                    }
                } else if (type == 2) {
                    ArticlePage articlePage = (ArticlePage) OperateDb.getPage(new ArticlePage(), pageID);
                    if (isCancel) return;
                    if (articlePage == null) {
                        //如果数据库不存在该数据，则发送为空的ID
                        mHandler.obtainMessage(NULL_DB, pageID).sendToTarget();
                    } else {
                        mHandler.obtainMessage(SUCCEED_DB, articlePage).sendToTarget();
                    }
                } else if (type == 3) {
                    PicturePage picturePage = (PicturePage) OperateDb.getPage(new PicturePage(), pageID);
                    if (isCancel) return;
                    if (picturePage == null) {
                        //如果数据库不存在该数据，则发送为空的ID
                        mHandler.obtainMessage(NULL_DB, pageID).sendToTarget();
                    } else {
                        mHandler.obtainMessage(SUCCEED_DB, picturePage).sendToTarget();
                    }

                }
            }
        }).start();

    }


    /**
     * 加载网络数据
     *
     * @param type
     * @param pageID
     */
    public void getDateFromNet(int type, int pageID) {
        String url = null;
        if (type == 1) {
            url = MyServer.HOME_PAGE + pageID;
            loadFromNet(url, type, pageID);
        } else if (type == 2) {
            url = MyServer.ARTICLE_PAGE + pageID;
            loadFromNet(url, type, pageID);
        } else if (type == 3) {
            url = MyServer.PICTURE_PAGE + pageID;
            loadFromNet(url, type, pageID);
        }
    }

    /**
     * 从网络加载
     *
     * @param url
     * @param type
     */
    private void loadFromNet(final String url, final int type, final int pageID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            //onResponse是在main线程中执行
            @Override
            public void onResponse(final JSONObject response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (type == 1) {
                            HomePage homePage = new HomePage();
                            homePage.setPage(response);
                            OperateDb.addPage(homePage);
                            if (isCancel) return;
                            mHandler.obtainMessage(SUCCEED_NET, homePage).sendToTarget();
                        } else if (type == 2) {
                            ArticlePage articlePage = new ArticlePage();
                            articlePage.setPage(response);
                            OperateDb.addPage(articlePage);
                            if (isCancel) return;
                            mHandler.obtainMessage(SUCCEED_NET, articlePage).sendToTarget();
                        } else if (type == 3) {
                            PicturePage picturePage = new PicturePage();
                            picturePage.setPage(response);
                            OperateDb.addPage(picturePage);
                            if (isCancel) return;
                            mHandler.obtainMessage(SUCCEED_NET, picturePage).sendToTarget();
                        }
                    }
                }).start();
            }
        }, new Response.ErrorListener() {
            //若联网获取数据失败
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isCancel) return;
                mHandler.obtainMessage(FAIL_NET).sendToTarget();
                reTry++;
                if (reTry <= 5) {
                    getDataFromDb(type, pageID);
                }

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 取消所有数据请求
     */
    public void cancelAll() {
        isCancel = true;
        requestQueue.cancelAll(this);
    }

    /**
     * 设置加载标志位为开始状态
     */
    public void start() {
        isCancel = false;
    }

}
