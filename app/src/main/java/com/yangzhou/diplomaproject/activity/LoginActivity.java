package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.manager.DialogManager;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.user.User;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * @author Yang
 * @function 登录页面
 * @date 2017/3/23.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mLoginView;
    private TextView mBackView;
    private TextView mRegisterView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_layout);
        initView();
    }


    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.login_input_username);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);

        mLoginView = (TextView) findViewById(R.id.login_button_view);
        mLoginView.setOnClickListener(this);
        mBackView = (TextView) findViewById(R.id.login_back_view);
        mBackView.setOnClickListener(this);
        mRegisterView = (TextView) findViewById(R.id.login_register_view);
        mRegisterView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            //登录按钮
            case R.id.login_button_view:
                login();
                break;
            //返回
            case R.id.login_back_view:
                finish();
                break;
            //注册
            case R.id.login_register_view:
                toRegister();
                break;
        }

    }

    /**
     * 登录请求
     */
    private void login() {
        String userName = mUserNameView.getText().toString().trim();
        String pwd = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            return;
        }

        DialogManager.getInstnce().showProgressDialog(this);

        //发送登录请求
        RequestCenter.login(userName, pwd, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DialogManager.getInstnce().dismissProgressDialog();
                //用户信息
                User user = (User) responseObj;
                UserManager.getInstance().setUser(user); //通过UserManager管理用户信息
                //发送登录广播
                sendLoginBroadcast();

                setUserInfo(user);

                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                DialogManager.getInstnce().dismissProgressDialog();
                Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static final String LOGIN_ACTION = "com.zy.action.LOGIN_ACTION";

    /**
     * 发送登录成功的局部广播
     */
    private void sendLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(
            LOGIN_ACTION
        ));
    }

    private void setUserInfo(User user) {
        SPManager.getInstance().putString(SPManager.USER_ID, user.data.userId);
        SPManager.getInstance().putString(SPManager.USER_NAME, user.data.name);
        SPManager.getInstance().putString(SPManager.USER_TICK, user.data.tick);
        SPManager.getInstance().putString(SPManager.USER_PHOTO, user.data.photoUrl);
        SPManager.getInstance().putInt(SPManager.USER_TAG, user.data.userTag);
    }

    /**
     * 跳转到注册页面
     */
    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
