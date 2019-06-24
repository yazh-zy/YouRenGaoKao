package com.yangzhou.diplomaproject.view.news;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.adapter.PhotoPagerAdapter;
import com.yangzhou.diplomaproject.module.news.NewsHeadValue;
import com.yangzhou.diplomaproject.view.viewpagerindictor.CirclePageIndicator;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by zy on 2017/3/29.
 */

public class NewsHeaderLayout extends RelativeLayout {


    private Context mContext;

    //UI
    private RelativeLayout mRootView;
    private AutoScrollViewPager mViewPager;  //轮播
    private CirclePageIndicator mPagerIndictor;  //轮播指示器
    private PhotoPagerAdapter mAdapter;

    //Data
    private NewsHeadValue mHeaderValue;

    public NewsHeaderLayout(Context context, NewsHeadValue headerValue) {
        this(context, null, headerValue);
    }


    public NewsHeaderLayout(Context context, AttributeSet attrs, NewsHeadValue headerValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = headerValue;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_news_head_layout, this);
        mViewPager = (AutoScrollViewPager) mRootView.findViewById(R.id.news_pager);
        mPagerIndictor = (CirclePageIndicator) mRootView.findViewById(R.id.news_pager_indictor_view);

        //为View填充数据
        mAdapter = new PhotoPagerAdapter(mContext, mHeaderValue.ads, true);
        mViewPager.setAdapter(mAdapter);
        mViewPager.startAutoScroll(3000);
        mPagerIndictor.setViewPager(mViewPager);
    }

}
