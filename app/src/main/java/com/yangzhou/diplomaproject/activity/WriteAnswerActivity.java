package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.view.fragment.home.QuestionFragment;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by zy on 2017/4/12.
 */

public class WriteAnswerActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private TextView mSendView;
    private EditText mAnswerContent;
    private TextView mAddImage;

    private String q_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_answer_layout);

        Intent intent = getIntent();
        q_id = intent.getStringExtra(QuestionFragment.Q_TAG);

        initView();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.write_answer_back_view);
        mBackView.setOnClickListener(this);

        mSendView = (TextView) findViewById(R.id.write_answer_send_view);
        mSendView.setOnClickListener(this);

        mAnswerContent = (EditText) findViewById(R.id.write_answer_content);
        mAddImage = (TextView) findViewById(R.id.write_answer_addpic);
        mAddImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_answer_back_view:
                finish();
                break;
            case R.id.write_answer_send_view:
                sendAnswerRequest();
                break;
        }
    }

    private void sendAnswerRequest() {
        String content = mAnswerContent.getText().toString().trim();

        if (content == null || content.equals("")) {
            return;
        }

        RequestCenter.addAnswerRequest(q_id, UserManager.getInstance().getUser().data.userId, content, new DisposeDataListener() {
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
