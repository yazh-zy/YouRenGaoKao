package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.adapter.AnswerActivityAdapter;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.answer.AnswerBodyValue;
import com.yangzhou.diplomaproject.module.answer.BaseAnswerModel;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.share.ShareDialog;
import com.yangzhou.diplomaproject.view.answer.AnswerHeaderLayout;
import com.yangzhou.diplomaproject.view.fragment.home.QuestionFragment;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import cn.sharesdk.framework.Platform;

/**
 * Created by zy on 2017/4/10.
 */

public class AnswersListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mBackView;
    private TextView mShareView;
    private RelativeLayout mLoadingView;
    private RelativeLayout mWriteAnswer;

    private ListView mListView;
    private AnswerActivityAdapter mAdapter;
    private BaseAnswerModel mAnswerData;

    private RelativeLayout mNoAnswerView;
    private TextView mNoAnswerTitle;
    private TextView mNoAnswerContent;

    private String userId = null;


    private String q_id;
    public static final String ANSWER_ID = "answerId";

    private static final String TAG = "AnswersListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answers_list);

        Intent intent = getIntent();
        q_id = intent.getStringExtra(QuestionFragment.Q_TAG);

        initView();


        if (UserManager.getInstance().hasLogin()) {
            userId = UserManager.getInstance().getUser().data.userId;
        } else {
            userId = "-1";

        }

        sendGetAnswersRequest();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.answer_back_view);
        mBackView.setOnClickListener(this);

        mShareView = (TextView) findViewById(R.id.answer_share_view);
        mShareView.setOnClickListener(this);

        mLoadingView = (RelativeLayout) findViewById(R.id.answer_loading_view);

        mListView = (ListView) findViewById(R.id.answer_list_view);
        mListView.setOnItemClickListener(this);


        mNoAnswerView = (RelativeLayout) findViewById(R.id.answer_no_list);
        mNoAnswerTitle = (TextView) findViewById(R.id.answer_question_title_view);
        mNoAnswerContent = (TextView) findViewById(R.id.answer_question_content_view);

        mWriteAnswer = (RelativeLayout) findViewById(R.id.answer_write_answer_view);
        mWriteAnswer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answer_back_view:
                finish();
                break;

            case R.id.answer_share_view:
                shareApp();
                break;

            case R.id.answer_write_answer_view:
                Log.e("AnswerList", "Success");
                if (!UserManager.getInstance().hasLogin()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, WriteAnswerActivity.class);
                    intent.putExtra(QuestionFragment.Q_TAG, q_id);
                    startActivity(intent);
                }
                break;
        }
    }

    private void sendGetAnswersRequest() {
        RequestCenter.getAnswersList(q_id, userId, new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
                mAnswerData = (BaseAnswerModel) responseObj;
                showSuccessView();
            }
            @Override
            public void onFailure(Object reasonObj) {
                showFailureView();
            }
        });
    }

    private void showSuccessView() {
        if (mAnswerData.data.list != null && mAnswerData.data.list.size() > 0) {
            mLoadingView.setVisibility(View.GONE);
            mNoAnswerView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            if (mAdapter == null) {
                //为ListView添加列表头,在listview上方添加其他组件
                mListView.addHeaderView(new AnswerHeaderLayout(this, mAnswerData.data.head, q_id));
                mAdapter = new AnswerActivityAdapter(this, mAnswerData.data.list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.onDataChange(mAnswerData.data.list);
            }
        } else {
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mNoAnswerView.setVisibility(View.VISIBLE);
            mNoAnswerTitle.setText(mAnswerData.data.head.title);
            mNoAnswerContent.setText(mAnswerData.data.head.content);
        }
    }

    private void showFailureView() {
        Log.e(TAG, "OnFailure");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AnswerBodyValue value = (AnswerBodyValue) mAdapter.getItem(position - mListView.getHeaderViewsCount());

        Intent intent = new Intent(AnswersListActivity.this, AnswerDetailActivity.class);
        intent.putExtra(ANSWER_ID, value.id);
        intent.putExtra("title", mAnswerData.data.head.title);
        intent.putExtra("author", value.author);
        intent.putExtra("content", value.content);
        intent.putExtra("like", value.like);
        intent.putExtra("comment", value.comments);
        intent.putExtra("photo", value.photo);
        intent.putExtra("likeStatus", value.likeStatus);

        startActivity(intent);
    }

    /**
     * 分享
     */
    private void shareApp() {
        ShareDialog dialog = new ShareDialog(this, false);
        dialog.setShareType(Platform.SHARE_IMAGE);
        dialog.setShareTitle(mAnswerData.data.head.title);
        dialog.setShareTitleUrl("http://139.129.119.33/yygk/");
        dialog.setShareText("");
        dialog.setShareSite("有用高考");
        dialog.setShareSiteUrl("http://139.129.119.33/yygk/");
//        dialog.setImagePhoto(Environment.getExternalStorageDirectory() + "");
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendGetAnswersRequest();
    }
}
