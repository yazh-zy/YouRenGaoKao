package com.yangzhou.diplomaproject.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Yang
 * @function 所有Activity的基类，用来处理一些公共事件，如：数据统计
 * @date 09/03/2017
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * 输出日志
     */
    public String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
