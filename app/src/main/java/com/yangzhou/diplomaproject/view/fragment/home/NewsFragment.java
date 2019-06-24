package com.yangzhou.diplomaproject.view.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yalantis.phoenix.PullToRefreshView;
import com.yangzhou.diplomaproject.activity.WebViewActivity;
import com.yangzhou.diplomaproject.adapter.NewsFragmentAdapter;
import com.yangzhou.diplomaproject.module.news.BaseNewsModel;
import com.yangzhou.diplomaproject.module.news.NewsBodyValue;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.view.fragment.BaseFragment;
import com.yangzhou.diplomaproject.view.news.NewsHeaderLayout;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by yang zhou on 09/03/2017.
 */

public class NewsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private View mContentView;

    private RelativeLayout mLoadingView;
    private ListView mListView;

    private static final String TAG = "NewsFragment";

    private NewsFragmentAdapter mAdapter;
    private BaseNewsModel mNewsData;

    private PullToRefreshView mPullToRefreshView;

    public static final int REFRESH_DELAY = 2000;

    public NewsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //发送列表请求
        requestNewsListViewData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_news_layout, container, false);
        initView();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestNewsListViewData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return mContentView;
    }

    private void initView() {
        mLoadingView = (RelativeLayout) mContentView.findViewById(R.id.news_loading_view);
        mListView = (ListView) mContentView.findViewById(R.id.news_list_view);
        mListView.setOnItemClickListener(this);

        mPullToRefreshView = (PullToRefreshView) mContentView.findViewById(R.id.pull_to_refresh);
    }

    private void requestNewsListViewData() {
        RequestCenter.requestNewsListViewData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mNewsData = (BaseNewsModel) responseObj;
                showSuccessView();
                Log.d(TAG, "success:" + responseObj);
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

    private void showSuccessView() {
        if (mNewsData.data.list != null && mNewsData.data.list.size() > 0) {

            if (mAdapter == null) {

                mLoadingView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                //为ListView添加列表头,在listview上方添加其他组件
                mListView.addHeaderView(new NewsHeaderLayout(mContext, mNewsData.data.head));
                mAdapter = new NewsFragmentAdapter(mContext, mNewsData.data.list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.onDataChange(mNewsData.data.list);
            }

        }
    }

    private void showErrorView() {
        Log.e(TAG, "OnFailure");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsBodyValue value = (NewsBodyValue) mAdapter.getItem(position);

        String url = value.content;

        if (url != null && !url.equals("")) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(WebViewActivity.URL_KEY, url);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        requestNewsListViewData();
    }
}
