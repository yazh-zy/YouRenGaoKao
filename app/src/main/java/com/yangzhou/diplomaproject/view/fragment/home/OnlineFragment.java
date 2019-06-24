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
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yalantis.phoenix.PullToRefreshView;
import com.yangzhou.diplomaproject.activity.BeforeOnlineActivity;
import com.yangzhou.diplomaproject.activity.EndOnlineActivity;
import com.yangzhou.diplomaproject.activity.IjkplayerActivity;
import com.yangzhou.diplomaproject.activity.LoginActivity;
import com.yangzhou.diplomaproject.activity.StartOnlineActivity;
import com.yangzhou.diplomaproject.adapter.OnlineFragmentAdapter;
import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.online.BaseOnlineModel;
import com.yangzhou.diplomaproject.module.online.OnlineBodyValue;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.view.fragment.BaseFragment;
import com.yangzhou.diplomaproject.view.online.OnlineHeaderLayout;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * @autor Yang
 * @function 网络课程列表页， 包含头部轮播图， 推荐课程，课程列表
 * @date 09/03/2017.
 */

public class OnlineFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View mContentView;

    private RelativeLayout mLoadingView;
    private ListView mListView;

    private OnlineFragmentAdapter mAdapter;
    private BaseOnlineModel mOnlineData;

    private TextView mStartClass;

    private PullToRefreshView mPullToRefreshView;
    public static final int REFRESH_DELAY = 2000;

    private static final String TAG = "OnlineFragment";

    public OnlineFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //发送列表请求
        requestOnlineListViewData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_online_layout, container, false);
        initView();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestOnlineListViewData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return mContentView;
    }

    private void initView() {
        mLoadingView = (RelativeLayout) mContentView.findViewById(R.id.online_loading_view);
        mListView = (ListView) mContentView.findViewById(R.id.online_list_view);
        mListView.setOnItemClickListener(this);
        mStartClass = (TextView) mContentView.findViewById(R.id.add_onlineclass_view);
        mStartClass.setOnClickListener(this);

        mPullToRefreshView = (PullToRefreshView) mContentView.findViewById(R.id.pull_to_refresh);
    }

    private void requestOnlineListViewData() {
        RequestCenter.requestOnlineListViewData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mOnlineData = (BaseOnlineModel) responseObj;
                showSuccessView();
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
        if (mOnlineData.data.list != null && mOnlineData.data.list.size() > 0) {
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            if (mAdapter == null) {

                //为ListView添加列表头,在listview上方添加其他组件
                mListView.addHeaderView(new OnlineHeaderLayout(mContext, mOnlineData.data.head));
                mAdapter = new OnlineFragmentAdapter(mContext, mOnlineData.data.list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.onDataChange(mOnlineData.data.list);
            }

        }
    }

    private void showErrorView() {
        Log.e(TAG, "OnFailure");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_onlineclass_view:
                if (UserManager.getInstance().hasLogin()) {
                    Intent intent = new Intent(mContext, StartOnlineActivity.class);
                    mContext.startActivity(intent);
                } else {

                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OnlineBodyValue value = (OnlineBodyValue) mAdapter.getItem(position - mListView.getHeaderViewsCount());

        if (value.status == 1) {
            // 直播进行，跳转到播放器
            Intent intent = new Intent(getActivity(), IjkplayerActivity.class);
            intent.putExtra("liveUrl", value.url);
            intent.putExtra("id", value.id);
            intent.putExtra("title", value.content);
            intent.putExtra("image", value.image);
            intent.putExtra("teacher", value.author);
            getActivity().startActivity(intent);
        } else if (value.status == 0) {
            //直播没开始
            Intent intent = new Intent(getActivity(), BeforeOnlineActivity.class);
            intent.putExtra("title", value.content);
            intent.putExtra("image", value.image);
            getActivity().startActivity(intent);
        } else if (value.status == 2) {
            //直播结束
            Intent intent = new Intent(getActivity(), EndOnlineActivity.class);
            intent.putExtra("id", value.id);
            intent.putExtra("rate", value.rate);
            intent.putExtra("title", value.content);
            intent.putExtra("image", value.image);
            intent.putExtra("teacher", value.author);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SPManager.getInstance().getInt(SPManager.USER_TAG, 0) == 1) {
            mStartClass.setVisibility(View.VISIBLE);
        } else if (SPManager.getInstance().getInt(SPManager.USER_TAG, 0) == 0) {
            mStartClass.setVisibility(View.GONE);
        }
    }
}
