package com.lewisen.goodnight.controller;

import android.os.Handler;
import android.os.Message;

import com.lewisen.goodnight.dataSrc.PageData;

/**
 * 页面数据加载控制
 * Created by Lewisen on 2015/9/10.
 */
public class LoadPageData {
    private LoadPageDataListener listener;
    private MyHandler mHandler;
    private int type;
    private PageData pageData;


    public LoadPageData(int type, LoadPageDataListener listener) {

        this.listener = listener;
        this.type = type;
        mHandler = new MyHandler();
        pageData = new PageData(mHandler);

    }

    /**
     * 开始加载数据
     *
     * @param pageID 页面ID
     * @param isNet  true 强制加载网络数据； false 加载数据库，若数据库不存在，再加载网络。
     */
    public void startLoadPageData(int pageID, boolean isNet) {
        pageData.start();
        if (isNet) {
            pageData.getDateFromNet(type, pageID);
        } else {
            pageData.getDataFromDb(type, pageID);
        }
    }

    /**
     * 加载最大的ID
     */
    public void startLoadMaxPageID() {
        IDManage.setMaxID(mHandler);
    }


    /**
     * 停止加载，
     */
    public void stopLoadPageData() {
        pageData.cancelAll();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case PageData.SUCCEED_NET:
//                    Util.printInfo("加载网络成功");
                    listener.onLoadPage(msg.obj);
                    break;
                case PageData.SUCCEED_DB:
//                    Util.printInfo("加载数据库成功");
                    listener.onLoadPage(msg.obj);
                    break;

                case IDManage.ID_SUCCEED:
                    listener.onLoadMaxID(true);
                    break;

                case IDManage.ID_FAIL:
                    listener.onLoadMaxID(false);
                    break;

                case PageData.NULL_DB:
                    int id = (int) msg.obj;
//                    Util.printInfo("数据库为空 id = " + id);
                    pageData.getDateFromNet(type, id);
                    break;

                case PageData.FAIL_NET:
//                    Util.printInfo("加载网络失败");
                    listener.onLoadError();
                    break;
                default:
//                    Util.printInfo("其他 = " + msg.what);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public interface LoadPageDataListener {
        void onLoadPage(Object object);

        /**
         * 连接网络加载失败调用
         */
        void onLoadError();

        /**
         * @param state true 获取成功，false 获取失败
         */
        void onLoadMaxID(boolean state);
    }
}
