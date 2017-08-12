//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.community.share.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.umeng.comm.core.beans.ShareContent;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.ToastMsg;
import com.umeng.community.share.UMShareServiceFactory;
import com.umeng.community.share.adapter.PlatformAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import java.util.List;

public class ShareDialog extends Dialog {
    private RecyclerView mRecyclerView;
    private UMSocialService mSocialService;
    private PlatformAdapter mPlatformAdapter;
    private ShareContent mShareContent;
    private Context mContext;
    SnsPostListener mPostListener;

    public ShareDialog(Context context) {
        this(context, ResFinder.getStyle("umeng_comm_share_dialog_style"));
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        this.mSocialService = UMShareServiceFactory.getSocialService();
        this.mPostListener = new SnsPostListener() {
            public void onStart() {
            }

            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                if(eCode == 200) {
                    ToastMsg.showShortMsg(ShareDialog.this.getContext(), ResFinder.getString("umeng_comm_share_success"));
                    String platformKeyWorld = "";
                    if(platform == SHARE_MEDIA.WEIXIN) {
                        platformKeyWorld = "wxsession";
                    } else if(platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                        platformKeyWorld = "wxtimeline";
                    } else if(platform == SHARE_MEDIA.GOOGLEPLUS) {
                        platformKeyWorld = "google_plus";
                    } else {
                        platformKeyWorld = platform.toString();
                    }

                    CommunityFactory.getCommSDK(ShareDialog.this.getContext()).sendFeedShareAnalysis(ShareDialog.this.mShareContent.mFeedId, platformKeyWorld);
                }
            }
        };
        this.mContext = context;
        this.initContentView();
        this.mSocialService.getConfig().closeToast();
    }

    public void setShareContent(ShareContent shareContent) {
        this.mShareContent = shareContent;
    }

    private void initContentView() {
        this.setContentView(ResFinder.getLayout("umeng_comm_share_dialog_layout"));
        this.mRecyclerView = (RecyclerView)this.findViewById(ResFinder.getId("umeng_comm_share_recyclerview"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mPlatformAdapter = new PlatformAdapter(this.getSocialPlatforms());
        this.mPlatformAdapter.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShareDialog.this.dismiss();
                ShareDialog.this.mSocialService.postShare(ShareDialog.this.mContext, ShareDialog.this.mPlatformAdapter.getItem(position).mPlatform, ShareDialog.this.mPostListener);
            }
        });
        this.mRecyclerView.setAdapter(this.mPlatformAdapter);
        this.initLayoutParams();
    }

    private void initLayoutParams() {
        Window window = this.getWindow();
        window.setLayout(-1, -2);
        LayoutParams wlp = window.getAttributes();
        wlp.gravity = 80;
        window.setAttributes(wlp);
    }

    private List<SnsPlatform> getSocialPlatforms() {
        return this.mSocialService.getConfig().getAllPlatforms(this.getContext(), this.mSocialService);
    }
}
