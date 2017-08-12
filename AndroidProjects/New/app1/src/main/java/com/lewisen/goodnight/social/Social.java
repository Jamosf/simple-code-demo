package com.lewisen.goodnight.social;

import android.app.Activity;
import android.content.Context;

import com.lewisen.goodnight.bean.DispalyPage;
import com.lewisen.goodnight.dataSrc.MyServer;
import com.lewisen.goodnight.dataSrc.PageData;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.List;

/**
 * 评论
 * Created by Lewisen on 2015/9/15.
 */
public class Social {
    private Context mContext;
    private String pageName = "";
    private String shareUrl = "";
    private DispalyPage dispalyPage;
    private UMSocialService umSocialService;


    public Social(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 打开分享
     */
    public void openShare() {
        if (dispalyPage == null) return;
        shareContentSet();
        umSocialService.openShare((Activity) mContext, false);
    }

    /**
     * 打开评论
     */
    public void openComment() {
        if (dispalyPage == null) return;
        umSocialService.openComment(mContext, false);
        umSocialService.getComments(mContext, new SocializeListeners.FetchCommetsListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onComplete(int i, List<UMComment> list, SocializeEntity socializeEntity) {

            }
        }, -1);
    }


    /**
     * 社会化 初始化
     */
    public void init(int type, int pageID, DispalyPage dispalyPage) {
        setPage(type, pageID);
        setContent(dispalyPage);
        umSocialService = UMServiceFactory.getUMSocialService(pageName);
    }

    private void setPage(int type, int pageID) {
        if (type == PageData.TYPE_HOME) {
            pageName = "HomePage" + pageID;
            shareUrl = MyServer.SHARE_URL + "type=1&id=" + pageID;
        } else if (type == PageData.TYPE_ARTICLE) {
            pageName = "ArticlePage" + pageID;
            shareUrl = MyServer.SHARE_URL + "type=2&id=" + pageID;
        } else if (type == PageData.TYPE_PICTURE) {
            pageName = "PicturePage" + pageID;
            shareUrl = MyServer.SHARE_URL + "type=3&id=" + pageID;
        }
    }

    /**
     * 设置分享内容
     */
    private void setContent(DispalyPage dispalyPage) {
        this.dispalyPage = dispalyPage;
    }


    private void shareContentSet() {
        UMImage urlImage = new UMImage(mContext, MyServer.APP_ICON);
        String content = dispalyPage.getText().substring(0,
                (dispalyPage.getText().length() < 50 ? dispalyPage.getText().length() : 48)).replace("###", "").replace("@@@", "");
        // 微信
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(dispalyPage.getTitle());
        weixinContent.setTargetUrl(shareUrl);
        weixinContent.setShareMedia(urlImage);
        umSocialService.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(dispalyPage.getTitle());
        circleMedia.setShareMedia(urlImage);
        circleMedia.setTargetUrl(shareUrl);
        umSocialService.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(shareUrl);
        qzone.setTitle(dispalyPage.getTitle());
        qzone.setShareMedia(urlImage);
        umSocialService.setShareMedia(qzone);

        // QQ
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(dispalyPage.getTitle());
        qqShareContent.setShareMedia(urlImage);
        qqShareContent.setTargetUrl(shareUrl);
        umSocialService.setShareMedia(qqShareContent);
    }

}
