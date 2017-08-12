//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.community.share;

import com.umeng.comm.core.share.Shareable;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public abstract class UMShareServiceFactory {
    private static UMShareServiceImpl mShareServiceImpl;

    public UMShareServiceFactory() {
    }

    public static Shareable getShareService() {
        if(mShareServiceImpl == null) {
            Class var0 = UMShareServiceImpl.class;
            synchronized(UMShareServiceImpl.class) {
                if(mShareServiceImpl == null) {
                    mShareServiceImpl = new UMShareServiceImpl();
                }
            }
        }

        return mShareServiceImpl;
    }

    public static UMSocialService getSocialService() {
        return UMServiceFactory.getUMSocialService("com.umeng.social.share");
    }
}
