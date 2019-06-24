package com.yangzhou.diplomaproject.view.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yalantis.phoenix.PullToRefreshView;
import com.yangzhou.diplomaproject.activity.AddQuestionActivity;
import com.yangzhou.diplomaproject.activity.AnswersListActivity;
import com.yangzhou.diplomaproject.activity.LoginActivity;
import com.yangzhou.diplomaproject.activity.WebViewActivity;
import com.yangzhou.diplomaproject.adapter.QuestionFragmentAdapter;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.question.BaseQuestionModel;
import com.yangzhou.diplomaproject.module.question.QuestionBodyValue;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.view.fragment.BaseFragment;
import com.yangzhou.diplomaproject.zxing.app.CaptureActivity;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import static com.aliquis.yangzhou.diplomaproject.R.id.qrcode_view;


/**
 * @author Yang
 * @function 提问listview
 * @date 09/03/2017.
 */

public class QuestionFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_QRCODE = 0x01;

    private View mContentView;

    /**
     * layout
     */
    private RelativeLayout mLoadingView;
    private ListView mListView;
    private TextView mAddQuestion;
    private TextView mQrCode;

    private PullToRefreshView mPullToRefreshView;
    public static final int REFRESH_DELAY = 2000;

    /**
     * data and adapter
     */
    private BaseQuestionModel mQuestionsData;
    private QuestionFragmentAdapter mAdapter;

    private static final String TAG = "ANSWWER_FRAGMENT";
    public static final String Q_TAG = "question_id";

    public QuestionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //send request to get data
        requestQuestionListViewData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_question_layout, container, false);
        initView();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestQuestionListViewData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return mContentView;
    }

    private void initView() {
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (RelativeLayout) mContentView.findViewById(R.id.loading_view);
        mAddQuestion = (TextView) mContentView.findViewById(R.id.add_questions_view);
        mAddQuestion.setOnClickListener(this);
        mQrCode = (TextView) mContentView.findViewById(qrcode_view);
        mQrCode.setOnClickListener(this);

        mPullToRefreshView = (PullToRefreshView) mContentView.findViewById(R.id.pull_to_refresh);
    }

    private void requestQuestionListViewData() {

        RequestCenter.requestQuestionListViewData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                //更新UI
                mQuestionsData = (BaseQuestionModel) responseObj;
                showSuccessView();
                Log.e(TAG, "OnSuccess:" + responseObj);
            }

            @Override
            public void onFailure(Object reasonObj) {
                //提示网络有问题
                //TODO...
                Log.e(TAG, "onFailure:" + reasonObj.toString());
                showErrorView();
            }
        });
    }

    /**
     *
     */
    private void showSuccessView() {
        //判断数据是否为空
        if (mQuestionsData.data != null && mQuestionsData.data.size() > 0) {
            //隐藏loadingView
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                // set adapter
                mAdapter = new QuestionFragmentAdapter(mContext, mQuestionsData.data);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.onDataChange(mQuestionsData.data);
            }
        } else {
            showErrorView();
        }
    }

    /**
     *
     */
    private void showErrorView() {
        Log.e(TAG, "OnFailure");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_questions_view:
                if (UserManager.getInstance().hasLogin()) {
                    Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.qrcode_view:
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_QRCODE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        QuestionBodyValue value = (QuestionBodyValue) mAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), AnswersListActivity.class);
        intent.putExtra(Q_TAG, value.id);
        startActivity(intent);
    }

    //TODO......
    /**
     * 在哪调用扫码就添加到哪里
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_QRCODE:
                if (resultCode == Activity.RESULT_OK) {
                    String code = data.getStringExtra("SCAN_RESULT");
                    if (code.contains("http") || code.contains("https")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.URL_KEY, code);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
