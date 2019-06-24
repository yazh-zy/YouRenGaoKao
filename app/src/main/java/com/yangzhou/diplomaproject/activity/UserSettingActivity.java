package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;

/**
 * Created by zy on 2017/4/17.
 */

public class UserSettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBackView;
    private TextView mUserName;
    private TextView mPassword;
    private TextView mUserPhoto;
    private TextView mLogoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_setting);

        initView();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.user_setting_back_view);
        mBackView.setOnClickListener(this);
        mUserName = (TextView) findViewById(R.id.user_setting_change_username);
        mUserName.setOnClickListener(this);
        mPassword = (TextView) findViewById(R.id.user_setting_change_password);
        mPassword.setOnClickListener(this);
        mUserPhoto = (TextView) findViewById(R.id.user_setting_change_userphoto);
        mUserPhoto.setOnClickListener(this);
        mLogoutView = (TextView) findViewById(R.id.user_setting_logout);
        mLogoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.user_setting_back_view:
                finish();
                break;
            case R.id.user_setting_change_username:
                Intent intent = new Intent(UserSettingActivity.this, ChangeUserNameActivity.class);
                startActivity(intent);
                break;
            case R.id.user_setting_change_password:
                intent = new Intent(UserSettingActivity.this, ChangePswActivity.class);
                startActivity(intent);
                break;
            case R.id.user_setting_change_userphoto:
                intent = new Intent(UserSettingActivity.this, PhotoChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.user_setting_logout:
                UserManager.getInstance().remove();
                finish();
                break;
        }
    }
}
