package com.yangzhou.diplomaproject.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * @author Yang
 * @function 注册页面
 * @date 2017/3/29.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private EditText mAccountView;
    private EditText mPwdView;
    private EditText mConPwdView;
    private TextView mRegisterButton;

    private boolean isAccountAvailable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);

        initView();
        checkAccount();

    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.register_back_view);
        mBackView.setOnClickListener(this);

        mAccountView = (EditText) findViewById(R.id.register_input_username);
        mPwdView = (EditText) findViewById(R.id.register_input_password);
        mConPwdView = (EditText) findViewById(R.id.register_input_conpassword);

        mRegisterButton = (TextView) findViewById(R.id.register_button_view);
        mRegisterButton.setOnClickListener(this);
    }

    private void checkAccount() {
        mAccountView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String account = mAccountView.getText().toString().trim();
                    sendCheckAccount(account);
                }
            }
        });
    }

    private void sendCheckAccount(String account) {
        RequestCenter.checkAccount(account, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                isAccountAvailable = true;
            }

            @Override
            public void onFailure(Object reasonObj) {

                Toast.makeText(getApplication(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //返回到登录页面
            case R.id.register_back_view:
                finish();
                break;
            //注册按钮
            case R.id.register_button_view:
                checkEditText();
                break;
        }

    }

    /**
     * 检查填写的信息是否正确
     */

    private void checkEditText() {
        String account = mAccountView.getText().toString().trim();
        String password = mPwdView.getText().toString().trim();
        String conPassword = mConPwdView.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            return;
        }
        if (TextUtils.isEmpty(password)) {
            return;
        }
        if (TextUtils.isEmpty(conPassword)) {
            return;
        }

        //TODO...检查用户名的合法性
        if (password.equals(conPassword)) {
            if (isAccountAvailable) {

                sendRegisterRequest(account, password);
            }
        } else {
            Toast.makeText(getApplication(), "确认密码不符", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO...要判断account是否唯一

    /**
     * 发送注册请求
     *
     * @param account
     * @param password
     */
    private void sendRegisterRequest(String account, String password) {

        RequestCenter.register(account, password, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                //注册成功后提示，并返回登录页面
                Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                Toast.makeText(getApplication(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
