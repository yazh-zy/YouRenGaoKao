package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.adapter.CommentActivityAdapter;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.comment.BaseCommentModel;
import com.yangzhou.diplomaproject.module.comment.CommentBodyValue;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.view.answer.ReplyDialog;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zy on 2017/4/14.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private TextView mBackView;
    private TextView mUserNameView;
    private TextView mContentView;
    private TextView mDateView;
    private CircleImageView mPhotoView;
    private EditText mAddComment;
    private TextView mSendComment;
    private RelativeLayout mLoadingView;

    private TextView mNoComments;

    private ListView mList;
    private BaseCommentModel mValue;
    private CommentActivityAdapter mAdapter;

    private String answerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_comment_layout);

        Intent intent = getIntent();
        answerId = intent.getStringExtra(AnswerDetailActivity.ANSWER_ID);

        initView();

        sendCommentRequest();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.comment_back_view);
        mBackView.setOnClickListener(this);
        mUserNameView = (TextView) findViewById(R.id.comment_user_name_view);
        mContentView = (TextView) findViewById(R.id.comment_content_view);
        mDateView = (TextView) findViewById(R.id.comment_date_view);
        mPhotoView = (CircleImageView) findViewById(R.id.comment_uesr_photo_view);
        mAddComment = (EditText) findViewById(R.id.add_comment_et);
        mSendComment = (TextView) findViewById(R.id.add_comment_send);
        mSendComment.setOnClickListener(this);
        mLoadingView = (RelativeLayout) findViewById(R.id.comment_loading_view);

        mList = (ListView) findViewById(R.id.comment_list_view);
        mList.setOnItemClickListener(this);

        mNoComments = (TextView) findViewById(R.id.comment_no_comments);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_back_view:
                finish();
                break;
            case R.id.add_comment_send:
                if (UserManager.getInstance().hasLogin()) {
                    sendAddCommentRequest();
                } else {
                    Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    private void sendCommentRequest() {
        RequestCenter.getCommentList(answerId, new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
                mValue = (BaseCommentModel) responseObj;
                showListSuccessView();
            }

            @Override
            public void onFailure(Object reasonObj) {
                showListFailuserView();
            }
        });
    }

    private void showListSuccessView() {
        if (mValue.data.size() > 0 && mValue.data != null) {
            mLoadingView.setVisibility(View.GONE);
            mNoComments.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);

            // set adapter
            mAdapter = new CommentActivityAdapter(this, mValue.data);
            mList.setAdapter(mAdapter);
        } else {
            mLoadingView.setVisibility(View.GONE);
            mNoComments.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
        }


    }

    private void showListFailuserView() {
    }

    private void sendAddCommentRequest() {
        String commentContent = mAddComment.getText().toString().trim();

        if (commentContent.equals("") || commentContent == null) {
            return;
        }

        RequestCenter.addComment(commentContent, answerId, "0", UserManager.getInstance().getUser().data.userId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj){
                Toast.makeText(CommentActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommentBodyValue value = (CommentBodyValue) mAdapter.getItem(position);

        ReplyDialog dialog = new ReplyDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setmAnswerId(answerId);
        dialog.setmParentId(value.parentId);
        dialog.show();
    }
}
