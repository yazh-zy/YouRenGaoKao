package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.adapter.CommentActivityAdapter;
import com.yangzhou.diplomaproject.module.comment.BaseCommentModel;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.imageloader.ImageLoaderManager;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by zy on 2017/5/6.
 */

public class EndOnlineActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private ImageView mClazzImage;
    private TextView mClazzTitle;
    private TextView mClazzTeacher;
    private RatingBar mRatingStar;
    private ListView mListView;

    private CommentActivityAdapter mAdapter;
    private BaseCommentModel mValue;

    private String title;
    private String teacher;
    private String image;
    private String id;
    private float rate;
    private ImageLoaderManager mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end_online_layout);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        teacher = intent.getStringExtra("teacher");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");
        rate = intent.getFloatExtra("rate", 5);

        initView();

        setClazzDetail();

        getClazzComment();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.comment_online_back_view);
        mBackView.setOnClickListener(this);
        mClazzImage = (ImageView) findViewById(R.id.comment_online_pic);
        mClazzTeacher = (TextView) findViewById(R.id.comment_online_teacher);
        mClazzTitle = (TextView) findViewById(R.id.comment_online_title);
        mRatingStar = (RatingBar) findViewById(R.id.comment_star_view);
        mListView = (ListView) findViewById(R.id.comment_online_listview);

        mImageLoader = ImageLoaderManager.getInstance(this);
    }


    private void setClazzDetail() {
        mClazzTitle.setText(title);
        mClazzTeacher.setText(teacher);
        mImageLoader.displayImage(mClazzImage, image);
        mRatingStar.setRating(rate);
    }

    private void getClazzComment() {
        RequestCenter.getClazzComment(id, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mValue = (BaseCommentModel) responseObj;
                showSuccessView();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.comment_online_back_view:
                finish();
                break;
        }
    }

    private void showSuccessView() {
        if (mValue.data.size() > 0 && !mValue.data.equals(null)) {
            // set adapter
            mAdapter = new CommentActivityAdapter(this, mValue.data);
            mListView.setAdapter(mAdapter);
        } else {

        }
    }
}
