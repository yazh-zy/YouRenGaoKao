package com.yangzhou.diplomaproject.view.online;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.module.online.OnlineFooterValue;
import com.yangzhou.diplomaproject.module.online.OnlineHeadValue;
import com.zy.sdk.imageloader.ImageLoaderManager;

/**
 * Created by zy on 2017/3/29.
 */

public class OnlineHeaderLayout extends RelativeLayout {


    private Context mContext;

    //UI
    private RelativeLayout mRootView;
    private TextView mHotView;  //今日最新
    private ImageView[] mImageViews = new ImageView[4];  //展示的最新课程的图片
    private LinearLayout mFootLayout;

    //Data
    private OnlineHeadValue mHeaderValue;
    private ImageLoaderManager mImageLoader;

    public OnlineHeaderLayout(Context context, OnlineHeadValue headerValue) {
        this(context, null, headerValue);
    }


    public OnlineHeaderLayout(Context context, AttributeSet attrs, OnlineHeadValue headerValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = headerValue;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_online_head_layout, this);
        mHotView = (TextView) mRootView.findViewById(R.id.zuixing_view);
        mImageViews[0] = (ImageView) mRootView.findViewById(R.id.head_image_one);
        mImageViews[1] = (ImageView) mRootView.findViewById(R.id.head_image_two);
        mImageViews[2] = (ImageView) mRootView.findViewById(R.id.head_image_three);
        mImageViews[3] = (ImageView) mRootView.findViewById(R.id.head_image_four);
        mFootLayout = (LinearLayout) mRootView.findViewById(R.id.content_layout);


        for (int i = 0; i < mImageViews.length; i++) {
            mImageLoader.displayImage(mImageViews[i], mHeaderValue.middle.get(i));
        }

        for (OnlineFooterValue value : mHeaderValue.footer) {
            mFootLayout.addView(createItem(value));
        }

        mHotView.setText(mContext.getString(R.string.today_newest));
    }

    private OnlineBottomItem createItem(OnlineFooterValue value) {
        OnlineBottomItem item = new OnlineBottomItem(mContext, value);
        return item;
    }
}
