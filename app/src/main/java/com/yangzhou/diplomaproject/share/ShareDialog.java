package com.yangzhou.diplomaproject.share;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by renzhiqiang on 16/8/13.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DisplayMetrics dm;

    /**
     * UI
     */
    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;
    private RelativeLayout mDownloadLayout;
    private TextView mCancelView;

    /**
     * share relative
     */
    private int mShareType; //指定分享类型
    private String mShareTitle; //指定分享内容标题
    private String mShareText; //指定分享内容文本
    private String mSharePhoto; //指定分享本地图片
    private String mShareTileUrl;
    private String mShareSiteUrl;
    private String mShareSite;
    private String mUrl;

    private boolean isShowDownload;

    public ShareDialog(Context context, boolean isShowDownload) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
        this.isShowDownload = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        /**
         * 通过获取到dialog的window来控制dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        mWeixinLayout = (RelativeLayout) findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinMomentLayout = (RelativeLayout) findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(this);
        mQQLayout = (RelativeLayout) findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(this);
        mQZoneLayout = (RelativeLayout) findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(this);
        mDownloadLayout = (RelativeLayout) findViewById(R.id.download_layout);
        mDownloadLayout.setOnClickListener(this);
        if (isShowDownload) {
            mDownloadLayout.setVisibility(View.VISIBLE);
        }
        mCancelView = (TextView) findViewById(R.id.cancel_view);
        mCancelView.setOnClickListener(this);
    }

    public void setShareTitle(String title) {
        mShareTitle = title;
    }

    public void setImagePhoto(String photo) {
        mSharePhoto = photo;
    }

    public void setShareType(int type) {
        mShareType = type;
    }

    public void setShareSite(String site) {
        mShareSite = site;
    }

    public void setShareTitleUrl(String titleUrl) {
        mShareTileUrl = titleUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setShareSiteUrl(String siteUrl) {
        mShareSiteUrl = siteUrl;
    }

    public void setShareText(String text) {
        mShareText = text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                share(ShareManager.PlatformType.Wechat);
                break;
            case R.id.moment_layout:
                share(ShareManager.PlatformType.WechatMoments);
                break;
            case R.id.qq_layout:
                share(ShareManager.PlatformType.QQ);
                break;
            case R.id.qzone_layout:
                share(ShareManager.PlatformType.QZone);
                break;
            case R.id.cancel_view:
                dismiss();
                break;
        }
    }

    /**
     * 分享结果事件监听
     */
    private PlatformActionListener mListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };

    private void share(ShareManager.PlatformType platofrm) {
        ShareData mData = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(mShareType);
        params.setTitle(mShareTitle);
        params.setTitleUrl(mShareTileUrl);
        params.setSite(mShareSite);
        params.setSiteUrl(mShareSiteUrl);
        params.setText(mShareText);
        params.setImagePath(mSharePhoto);
        params.setUrl(mUrl);
        mData.type = platofrm;
        mData.params = params;
        ShareManager.getInstance().shareData(mData, mListener);
    }
}
