package com.yangzhou.diplomaproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by zy on 2017/4/10.
 */

public class AddQuestionActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBack;
    private TextView mNextStep;
    private EditText mTitle;
    private EditText mContent;
    private int checkContent = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_layout);

        initView();

    }

    private void initView() {
        mBack = (TextView) findViewById(R.id.add_question_back);
        mBack.setOnClickListener(this);

        mNextStep = (TextView) findViewById(R.id.add_question_nextstep);
        mNextStep.setOnClickListener(this);

        mTitle = (EditText) findViewById(R.id.add_question_title);
        mContent = (EditText) findViewById(R.id.add_question_content);

    }

    @Override
    public void onClick(View v) {
        String title = mTitle.getText().toString().trim();

        switch (v.getId()) {
            case R.id.add_question_back:
                finish();
                break;
            case R.id.add_question_nextstep:
                if (title == null || title.equals("") || title.length() == 0) {
                    //title太短
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else if (title.length() <= 10) {
                    //title没有写
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else{
                    sendAddQuestionRequest();
                }
                break;
        }
    }

    private void sendAddQuestionRequest() {
        String title = mTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();

        RequestCenter.addQuestions(title, content, UserManager.getInstance().getUser().data.userId, new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
                Toast.makeText(AddQuestionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.e("ADD_QUESTION", reasonObj.toString());
            }
        });
    }
}
