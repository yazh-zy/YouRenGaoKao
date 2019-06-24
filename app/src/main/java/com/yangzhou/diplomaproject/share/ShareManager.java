package com.yangzhou.diplomaproject.share;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by zy on 2017/3/21.
 */

public class ShareManager {

    private static ShareManager mShareManager = null;

    public static ShareManager getInstance() {
        if (mShareManager == null) {
            synchronized (ShareManager.class) {
                if (mShareManager == null) {
                    mShareManager = new ShareManager();
                }
            }
        }
        return mShareManager;
    }

    private ShareManager() {

    }

    public static void init(Context context) {
        ShareSDK.initSDK(context);
    }

    private Platform mCurrentPlatform;

    public void shareData(ShareData data, PlatformActionListener listener) {

        switch(data.type) {
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.NAME);
                break;
            case Wechat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WeiBo:
                mCurrentPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
                break;
            case WechatMoments:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
        }
        mCurrentPlatform.setPlatformActionListener(listener);
        mCurrentPlatform.share(data.params);

    }

    public enum PlatformType {
        QQ, QZone, Wechat, WeiBo, WechatMoments,
    }
}
