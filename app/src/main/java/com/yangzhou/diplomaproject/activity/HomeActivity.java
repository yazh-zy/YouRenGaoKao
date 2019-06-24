package com.yangzhou.diplomaproject.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.view.fragment.home.QuestionFragment;
import com.yangzhou.diplomaproject.view.fragment.home.NewsFragment;
import com.yangzhou.diplomaproject.view.fragment.home.MineFragment;
import com.yangzhou.diplomaproject.view.fragment.home.OnlineFragment;

/**
 * @author Yang
 * @function 主页面，实现4个Fragment，继承自有BaseActivity
 * @date 09/03/2017.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fm;
    private NewsFragment mNewsFragment;
    private MineFragment mMineFragment;
    private QuestionFragment mQuestionFragment;
    private OnlineFragment mOnlineFragment;
    private Fragment mCurrent;

    private RelativeLayout mNewsLayout;
    private RelativeLayout mMineLayout;
    private RelativeLayout mAnswerLayout;
    private RelativeLayout mOnlineLayout;
    private TextView mNewsView;
    private TextView mMineView;
    private TextView mAnswerView;
    private TextView mOnlineView;

    //日志
    private static final String TAG = "MAIN";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);

        //初始化控件
        initView();

        //首次进入app显示新闻页面
        mNewsFragment = new NewsFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, mNewsFragment);
        fragmentTransaction.commit();
    }

    /**
     * 初始化控件
     * 注册底部菜单点击事件
     */
    private void initView() {
        mNewsLayout = (RelativeLayout) findViewById(R.id.news_layout_view);
        mNewsLayout.setOnClickListener(this);
        mAnswerLayout = (RelativeLayout) findViewById(R.id.answer_layout_view);
        mAnswerLayout.setOnClickListener(this);
        mOnlineLayout = (RelativeLayout) findViewById(R.id.online_layout_view);
        mOnlineLayout.setOnClickListener(this);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);
        mMineLayout.setOnClickListener(this);

        mNewsView = (TextView) findViewById(R.id.news_image_view);
        mAnswerView = (TextView) findViewById(R.id.answer_image_view);
        mOnlineView = (TextView) findViewById(R.id.online_image_view);
        mMineView = (TextView) findViewById(R.id.mine_image_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @function 隐藏某个Fragment
     * @param fragment
     * @param ft
     */
    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.news_layout_view:
                //设置底部button点击后的效果
                mNewsView.setBackgroundResource(R.drawable.home_news_select);
                mAnswerView.setBackgroundResource(R.drawable.home_question_normal);
                mOnlineView.setBackgroundResource(R.drawable.home_online_normal);
                mMineView.setBackgroundResource(R.drawable.home_user_normal);

                //隐藏其余三个Fragment
                hideFragment(mQuestionFragment, fragmentTransaction);
                hideFragment(mOnlineFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                //如果要显示的Fragment没有创建就创建并添加，如果创建过就显示
                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                    fragmentTransaction.add(R.id.content_layout, mNewsFragment);
                } else {
                    mCurrent = mNewsFragment;
                    fragmentTransaction.show(mNewsFragment);
                }
                break;
            case R.id.answer_layout_view:
                mNewsView.setBackgroundResource(R.drawable.home_news_normal);
                mAnswerView.setBackgroundResource(R.drawable.home_question_select);
                mOnlineView.setBackgroundResource(R.drawable.home_online_normal);
                mMineView.setBackgroundResource(R.drawable.home_user_normal);

                hideFragment(mNewsFragment, fragmentTransaction);
                hideFragment(mOnlineFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mQuestionFragment == null) {
                    mQuestionFragment = new QuestionFragment();
                    fragmentTransaction.add(R.id.content_layout, mQuestionFragment);
                } else {
                    mCurrent = mQuestionFragment;
                    fragmentTransaction.show(mQuestionFragment);
                }
                break;
            case R.id.online_layout_view:
                mNewsView.setBackgroundResource(R.drawable.home_news_normal);
                mAnswerView.setBackgroundResource(R.drawable.home_question_normal);
                mOnlineView.setBackgroundResource(R.drawable.home_online_select);
                mMineView.setBackgroundResource(R.drawable.home_user_normal);

                hideFragment(mNewsFragment, fragmentTransaction);
                hideFragment(mQuestionFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mOnlineFragment == null) {
                    mOnlineFragment = new OnlineFragment();
                    fragmentTransaction.add(R.id.content_layout, mOnlineFragment);
                } else {
                    mCurrent = mOnlineFragment;
                    fragmentTransaction.show(mOnlineFragment);
                }
                break;
            case R.id.mine_layout_view:
                mNewsView.setBackgroundResource(R.drawable.home_news_normal);
                mAnswerView.setBackgroundResource(R.drawable.home_question_normal);
                mOnlineView.setBackgroundResource(R.drawable.home_online_normal);
                mMineView.setBackgroundResource(R.drawable.home_user_select);

                hideFragment(mNewsFragment, fragmentTransaction);
                hideFragment(mQuestionFragment, fragmentTransaction);
                hideFragment(mOnlineFragment, fragmentTransaction);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mMineFragment);
                } else {
                    mCurrent = mMineFragment;
                    fragmentTransaction.show(mMineFragment);
                }

                break;
        }

        fragmentTransaction.commit();
    }
}
