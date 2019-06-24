package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.imageloader.ImageLoaderManager;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by zy on 2017/5/6.
 */

public class OnlineRatingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private RatingBar mRatingBar;
    private EditText mCommentView;
    private TextView mSubmit;

    private ImageView mClazzPic;
    private TextView mTitle;
    private TextView mAuthor;


    private ImageLoaderManager imageLoaderManager;

    private String clazzId;
    private String title;
    private String image;
    private String author;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online_rating_layout);

        Intent intent = getIntent();
        clazzId = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");
        author = intent.getStringExtra("author");

        initView();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.rating_online_back_view);
        mBackView.setOnClickListener(this);
        mRatingBar = (RatingBar) findViewById(R.id.rating_star_view);
        mCommentView = (EditText) findViewById(R.id.rating_comment_view);
        mSubmit = (TextView) findViewById(R.id.rating_submit_view);
        mSubmit.setOnClickListener(this);

        mClazzPic = (ImageView) findViewById(R.id.rating_online_pic);
        mTitle = (TextView) findViewById(R.id.rating_online_title);
        mAuthor = (TextView) findViewById(R.id.rating_online_teacher);

        imageLoaderManager = ImageLoaderManager.getInstance(this);

        mTitle.setText(title);
        mAuthor.setText(author);
        imageLoaderManager.displayImage(mClazzPic, image);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rating_online_back_view:
                finish();
                break;
            case R.id.rating_submit_view:
                if (UserManager.getInstance().hasLogin()) {

                    submitComment();
                } else {
                    Intent intent = new Intent(OnlineRatingActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    private void submitComment() {
        float stars = mRatingBar.getRating();
        String comment = mCommentView.getText().toString().trim();

        RequestCenter.sendClazzComment(comment, stars, clazzId, UserManager.getInstance().getUser().data.userId, new DisposeDataListener() {
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
