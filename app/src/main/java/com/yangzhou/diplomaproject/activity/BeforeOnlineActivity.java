package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.zy.sdk.imageloader.ImageLoaderManager;

/**
 * Created by zy on 2017/4/18.
 */

public class BeforeOnlineActivity extends BaseActivity implements View.OnClickListener{

    private TextView mBackView;
    private ImageView mOnlineImage;
    private TextView mOnlineTitle;

    private String title;
    private String image;

    ImageLoaderManager mImageLoader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_before_online_layout);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");

        initView();

        setContentView();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.before_online_back_view);
        mBackView.setOnClickListener(this);
        mOnlineImage = (ImageView) findViewById(R.id.before_online_pic);
        mOnlineTitle = (TextView) findViewById(R.id.before_online_title);

        mImageLoader = ImageLoaderManager.getInstance(this);
    }

    private void setContentView() {
        mOnlineTitle.setText(title);
        mImageLoader.displayImage(mOnlineImage, image);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.before_online_back_view:
                finish();
                break;
        }
    }
}
