package com.yangzhou.diplomaproject.view.answer;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.EditAnswerActivity;
import com.yangzhou.diplomaproject.activity.LoginActivity;
import com.yangzhou.diplomaproject.activity.WriteAnswerActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.answer.AnswerHeadValue;
import com.yangzhou.diplomaproject.view.fragment.home.QuestionFragment;
import com.zy.sdk.imageloader.ImageLoaderManager;

/**
 * Created by zy on 2017/4/10.
 */

public class AnswerHeaderLayout extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

    private RelativeLayout mRootView;
    private TextView mTitle;
    private TextView mContent;
//    private RelativeLayout mWriteAnswer;
    private RelativeLayout mWriteAnswerView;
    private RelativeLayout mModifyAnswerView;

    private AnswerHeadValue mAnswerHeaderValue;
    private ImageLoaderManager mImageLoader;

    private String q_id;

    public AnswerHeaderLayout(Context context, AnswerHeadValue answerHeadValue, String q_id) {
        this(context, null, answerHeadValue, q_id);
    }

    public AnswerHeaderLayout(Context context, AttributeSet attrs, AnswerHeadValue answerHeadValue, String questionId) {
        super(context, attrs);
        mContext = context;
        mAnswerHeaderValue = answerHeadValue;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        q_id = questionId;
        initView();

        mTitle.setText(mAnswerHeaderValue.title);
        mContent.setText(mAnswerHeaderValue.content);

        if (mAnswerHeaderValue.isAnswered == 0) {
            mWriteAnswerView.setVisibility(View.GONE);
            mModifyAnswerView.setVisibility(View.VISIBLE);
        } else if (mAnswerHeaderValue.isAnswered == 1){
            mWriteAnswerView.setVisibility(View.VISIBLE);
            mModifyAnswerView.setVisibility(View.GONE);
        }
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_answers_head_layout, this);
        mTitle = (TextView) mRootView.findViewById(R.id.question_title_view);
        mContent = (TextView) mRootView.findViewById(R.id.question_content_view);
        mWriteAnswerView = (RelativeLayout)mRootView.findViewById(R.id.question_add_answer);
        mWriteAnswerView.setOnClickListener(this);
        mModifyAnswerView = (RelativeLayout) mRootView.findViewById(R.id.question_modify_answer);
        mModifyAnswerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.question_add_answer:
                Log.e("AnswerHead", "Success");
                if (!UserManager.getInstance().hasLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, WriteAnswerActivity.class);
                    intent.putExtra(QuestionFragment.Q_TAG, q_id);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.question_modify_answer:
                Intent intent = new Intent(mContext, EditAnswerActivity.class);
                intent.putExtra(QuestionFragment.Q_TAG, q_id);
                mContext.startActivity(intent);
                break;

        }
    }
}
