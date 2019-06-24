package com.yangzhou.diplomaproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.clazz.BaseClazzModel;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.imageloader.ImageLoaderManager;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by zy on 2017/5/4.
 */

public class StartOnlineActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private ImageView mClassImg;
    private TextView mClassTitle;
    private TextView mClassLength;
    private TextView mClassUrl;
    private TextView mClassKey;
    private TextView mStartClass;
    private TextView mEndClass;
    private TextView mStartInfo;

    private ImageLoaderManager mImageLoader;

    private String classId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_online_layout);

        initView();

        getClassDetail();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.start_class_back_view);
        mBackView.setOnClickListener(this);
        mClassImg = (ImageView) findViewById(R.id.start_class_pic);
        mClassTitle = (TextView) findViewById(R.id.start_class_title);
        mClassLength = (TextView) findViewById(R.id.start_class_length_view);
        mClassUrl = (TextView) findViewById(R.id.start_class_url_view);
        mClassKey = (TextView) findViewById(R.id.start_class_key_view);
        mStartClass = (TextView) findViewById(R.id.start_class_btn);
        mStartClass.setOnClickListener(this);
        mEndClass = (TextView) findViewById(R.id.end_class_btn);
        mEndClass.setOnClickListener(this);
        mStartInfo = (TextView) findViewById(R.id.start_class_startinfo);
        mImageLoader = ImageLoaderManager.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.start_class_back_view:
                finish();
                break;
            case R.id.start_class_btn:
                startOnlineClass();
                break;
            case R.id.end_class_btn:
                endOnlineClass();
                break;
        }
    }

    private void getClassDetail() {
        /*
        * 获取这个教师最近的一堂课，在服务器上生成key并返回，然后返回课程信息
        * */
        RequestCenter.getClassDetail(UserManager.getInstance().getUser().data.userId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseClazzModel value = (BaseClazzModel) responseObj;
                classId = value.data.classId;

                mClassTitle.setText(value.data.content);
                mClassLength.setText(value.data.length);
                mImageLoader.displayImage(mClassImg, value.data.image);
                mClassUrl.setText(value.data.url);
                mClassKey.setText(value.data.key);
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void startOnlineClass() {

        /*
        * 将课程状态更改
        * */
        RequestCenter.startClass(classId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mStartInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void endOnlineClass() {

        /**
         * 将数据库中的key设置为null，课程状态更改
         */

        RequestCenter.endClass(classId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
