//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.community.share;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.umeng.comm.core.beans.ShareContent;
import com.umeng.comm.core.share.Shareable;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.community.share.dialog.ShareDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UMShareServiceImpl implements Shareable {
    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.social.share");
    private static Pattern mDoubleBytePattern = Pattern.compile("[^\\x00-\\xff]");

    UMShareServiceImpl() {
    }

    public void share(Activity activity, ShareContent shareItem) {
        this.prepareShareContent(activity, shareItem);
        ShareDialog dialog = new ShareDialog(activity);
        dialog.setShareContent(shareItem);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void prepareShareContent(Activity activity, ShareContent shareItem) {
        CircleShareContent circleShareContent = new CircleShareContent();
        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
        QQShareContent qqShareContent = new QQShareContent();
        QZoneShareContent qZoneShareContent = new QZoneShareContent();
        SinaShareContent sinaShareContent = new SinaShareContent();
        UMImage image = null;
        String title;
        if(shareItem.mImageItem != null) {
            title = shareItem.mImageItem.thumbnail;
            image = new UMImage(activity, title);
        } else {
            try {
                Drawable title1 = activity.getPackageManager().getApplicationIcon(activity.getPackageName());
                if(title1 == null) {
                    return;
                }

                image = new UMImage(activity, ((BitmapDrawable)title1).getBitmap());
            } catch (NameNotFoundException var11) {
                var11.printStackTrace();
            }
        }

        circleShareContent.setShareImage(image);
        weiXinShareContent.setShareImage(image);
        qqShareContent.setShareImage(image);
        qZoneShareContent.setShareImage(image);
        sinaShareContent.setShareImage(image);
        weiXinShareContent.setShareContent(shareItem.mText);
        circleShareContent.setShareContent(shareItem.mText);
        circleShareContent.setTitle(shareItem.mText);
        qqShareContent.setShareContent(shareItem.mText);
        qZoneShareContent.setShareContent(shareItem.mText);
        title = shareItem.mTitle;
        if(!TextUtils.isEmpty(title) && title.length() > 16) {
            title = title.substring(0, 16);
        }

        weiXinShareContent.setTitle(title);
        circleShareContent.setTitle(title);
        qqShareContent.setTitle(title);
        qZoneShareContent.setTitle(title);
        String shareContent = this.policyShareContent(activity, SHARE_MEDIA.SINA, shareItem.mText, shareItem.mTargetUrl);
        sinaShareContent.setShareContent(shareContent);
        circleShareContent.setTargetUrl(this.policyShareTargetUrl(activity, shareItem.mTargetUrl, SHARE_MEDIA.WEIXIN_CIRCLE));
        weiXinShareContent.setTargetUrl(this.policyShareTargetUrl(activity, shareItem.mTargetUrl, SHARE_MEDIA.WEIXIN));
        qqShareContent.setTargetUrl(this.policyShareTargetUrl(activity, shareItem.mTargetUrl, SHARE_MEDIA.QQ));
        qZoneShareContent.setTargetUrl(this.policyShareTargetUrl(activity, shareItem.mTargetUrl, SHARE_MEDIA.QZONE));
        this.mController.setShareMedia(circleShareContent);
        this.mController.setShareMedia(weiXinShareContent);
        this.mController.setShareMedia(qqShareContent);
        this.mController.setShareMedia(qZoneShareContent);
        this.mController.setShareMedia(sinaShareContent);
    }

    private String policyShareTargetUrl(Activity activity, String targeturl, SHARE_MEDIA platform) {
        StringBuilder builder = new StringBuilder(targeturl);
        builder.append("?").append("ak").append("=").append(CommonUtils.getAppkey(activity));
        builder.append("&platform=").append(platform.toString());
        return builder.toString();
    }

    private String policyShareContent(Activity activity, SHARE_MEDIA platform, String sourceShareContent, String targetUrl) {
        if(platform != SHARE_MEDIA.SINA) {
            return sourceShareContent;
        } else {
            String tempTargetUrl = targetUrl = this.policyShareTargetUrl(activity, targetUrl, SHARE_MEDIA.SINA);
            int textLen = this.countContentLength(sourceShareContent);
            if(textLen + this.countContentLength(tempTargetUrl) < 137) {
                return sourceShareContent + "..." + targetUrl;
            } else {
                int desireLen = 140 - this.countContentLength(tempTargetUrl) - 3;
                return desireLen > 0?sourceShareContent.substring(0, desireLen) + "..." + targetUrl:(textLen < 140?sourceShareContent:sourceShareContent.substring(0, 140));
            }
        }
    }

    private int countContentLength(String string) {
        string = string.trim();
        boolean singlebyte = false;
        int doublebyte = 0;

        for(Matcher matcher = mDoubleBytePattern.matcher(string); matcher.find(); ++doublebyte) {
            ;
        }

        int var5 = string.length() - doublebyte;
        if(var5 % 2 != 0) {
            doublebyte += (var5 + 1) / 2;
        } else {
            doublebyte += var5 / 2;
        }

        return doublebyte;
    }
}
