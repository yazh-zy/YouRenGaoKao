package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.share.ShareDialog;
import com.zy.sdk.imageloader.ImageLoaderManager;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zy on 2017/4/10.
 */

public class AnswerDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private TextView mShareView;
    private TextView mTitle;
    private TextView mContent;
    private TextView mLikeView;
    private TextView mCommentView;
    private CircleImageView mPhoto;
    private TextView mAuthor;
    private RelativeLayout mComment;
    private TextView mLike;
    private TextView mLiked;

    private String answerId;
    private String title;
    private String author;
    private String content;
    private String photo;
    private int like;
    private int comment;
    private int likeStatus;

    private ImageLoaderManager mImageLoader;

    public static final String ANSWER_ID = "answerId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answers_detail);

        Intent intent = getIntent();
        answerId = intent.getStringExtra(ANSWER_ID);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        author = intent.getStringExtra("author");
        like = intent.getIntExtra("like", 0);
        comment = intent.getIntExtra("comment", 0);
        photo = intent.getStringExtra("photo");
        likeStatus = intent.getIntExtra("likeStatus", 0);

        initView();

        showAnswerDetail();
        setLikeView();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.answer_detail_back_view);
        mBackView.setOnClickListener(this);
        mShareView = (TextView) findViewById(R.id.answer_detail_share_view);
        mShareView.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.answer_detail_question_title);
        mContent = (TextView) findViewById(R.id.answer_detail_content);
        mPhoto = (CircleImageView) findViewById(R.id.answer_detail_author_photo);
        mAuthor = (TextView) findViewById(R.id.answer_detail_author);
        mComment = (RelativeLayout) findViewById(R.id.answer_detail_comment);
        mComment.setOnClickListener(this);
        mLike = (TextView) findViewById(R.id.answer_detail_like_pic);
        mLike.setOnClickListener(this);
        mLiked = (TextView) findViewById(R.id.answer_detail_liked_pic);
        mLiked.setOnClickListener(this);
        mLikeView = (TextView) findViewById(R.id.answer_detail_like_view);
        mCommentView = (TextView) findViewById(R.id.answer_detail_comment_view);

        mImageLoader = ImageLoaderManager.getInstance(getApplication());
    }

    private void showAnswerDetail() {
        mTitle.setText(title);
        mContent.setText(content);
        mAuthor.setText(author);
        mLikeView.setText(like + "");
        mCommentView.setText(comment + "");
        mImageLoader.displayImage(mPhoto, photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answer_detail_back_view:
                finish();
                break;
            case R.id.answer_detail_share_view:
                shareApp();
                break;
            case R.id.answer_detail_comment:
                Intent intent = new Intent(AnswerDetailActivity.this, CommentActivity.class);
                intent.putExtra(ANSWER_ID, answerId);
                startActivity(intent);
                break;
            case R.id.answer_detail_like_pic:
            case R.id.answer_detail_liked_pic:
                if(UserManager.getInstance().hasLogin()) {
                    sendLikeRequest();
                } else {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 分享app
     */
    private void shareApp() {
        ShareDialog dialog = new ShareDialog(this, false);
        dialog.setShareType(Platform.SHARE_IMAGE);
        dialog.setShareTitle("");
        dialog.setShareTitleUrl("http://139.129.119.33/yygk/");
        dialog.setShareText("");
        dialog.setShareSite("有用高考");
        dialog.setShareSiteUrl("http://139.129.119.33/yygk/");
//        dialog.setImagePhoto(Environment.getExternalStorageDirectory() + "");
        dialog.show();
    }

    private void setLikeView() {
        if (likeStatus == 0) {
            mLiked.setVisibility(View.GONE);
            mLike.setVisibility(View.VISIBLE);
        } else if (likeStatus == 1) {
            mLiked.setVisibility(View.VISIBLE);
            mLike.setVisibility(View.GONE);
        }
    }

    private void sendLikeRequest() {
        RequestCenter.sendLikeRequest(answerId, UserManager.getInstance().getUser().data.userId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                String likeCount = mLikeView.getText().toString().trim();
                if (likeStatus == 0) {
                    //like
                    mLiked.setVisibility(View.VISIBLE);
                    mLike.setVisibility(View.GONE);
                    mLikeView.setText((Integer.parseInt(likeCount)+1) + "");
                    likeStatus = 1;
                } else if (likeStatus == 1) {
                    //dislike
                    mLiked.setVisibility(View.GONE);
                    mLike.setVisibility(View.VISIBLE);
                    mLikeView.setText((Integer.parseInt(likeCount)-1) + "");
                    likeStatus = 0;
                }
            }

            @Override
           public void onFailure(Object reasonObj) {
                Log.e(TAG, "Failed");
            }
        });
    }
}
