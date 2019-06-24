package com.yangzhou.diplomaproject.view.online;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.WebViewActivity;
import com.yangzhou.diplomaproject.module.online.OnlineFooterValue;
import com.zy.sdk.imageloader.ImageLoaderUtil;

/**
 * Created by zy on 2017/3/29.
 */

public class OnlineBottomItem extends RelativeLayout {
    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private TextView mTitleView;
    private TextView mInfoView;
    private TextView mInterestingView;
    private ImageView mImageOneView;
    private ImageView mImageTwoView;

    /**
     * Data
     */
    private OnlineFooterValue mData;
    private ImageLoaderUtil mImageLoader;

    public OnlineBottomItem(Context context, OnlineFooterValue data) {
        this(context, null, data);
    }

    public OnlineBottomItem(Context context, AttributeSet attrs, OnlineFooterValue data) {
        super(context, attrs);
        mContext = context;
        mData = data;
        mImageLoader = ImageLoaderUtil.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.item_online_recommand_layout, this); //添加到当前布局中
        mTitleView = (TextView) mRootView.findViewById(R.id.title_view);
        mInfoView = (TextView) mRootView.findViewById(R.id.info_view);
        mInterestingView = (TextView) mRootView.findViewById(R.id.interesting_view);
        mImageOneView = (ImageView) mRootView.findViewById(R.id.icon_1);
        mImageTwoView = (ImageView) mRootView.findViewById(R.id.icon_2);
        //TODO
        mRootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_KEY, mData.destationUrl);
                mContext.startActivity(intent);
            }
        });
        mTitleView.setText(mData.title);
        mInfoView.setText(mData.info);
        mInterestingView.setText(mData.from);
        mImageLoader.displayImage(mImageOneView, mData.imageOne);
        mImageLoader.displayImage(mImageTwoView, mData.imageTwo);
    }
}
