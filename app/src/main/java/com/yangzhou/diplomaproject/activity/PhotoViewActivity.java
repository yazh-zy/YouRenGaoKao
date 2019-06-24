package com.yangzhou.diplomaproject.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.adapter.PhotoPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zy on 2017/3/21.
 */

//TODO...

public class PhotoViewActivity extends BaseActivity implements View.OnClickListener{

    //UI
    private ViewPager mPager;
    private TextView mIndicatorView;
    private ImageView mShareView;

    //Data
    private ArrayList<String> mPhotoLists;
    private int mLength;
    private PhotoPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_layout);
        initData();
        initView();
    }

    private void initView() {
        mIndicatorView = (TextView) findViewById(R.id.indictor_view);
        mShareView = (ImageView) findViewById(R.id.share_view);
        mShareView.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.photo_pager);
//        mPager.setPageMargin();

        mAdapter = new PhotoPagerAdapter(this, mPhotoLists);
        mPager.setAdapter(mAdapter);
    }


    public static final String PHOTO_LIST = "photo_list";
    //初始化
    private void initData() {
        mPhotoLists = getIntent().getStringArrayListExtra(PHOTO_LIST);
        mLength = mPhotoLists.size();
    }

    @Override
    public void onClick(View v) {

    }
}
