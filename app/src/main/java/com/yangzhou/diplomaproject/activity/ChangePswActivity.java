package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * 修改密码Activity
 * layout : activity_change_psw.xml
 * url : http://139.129.119.33/yygk/ChangePsw
 * AsyncTask : changePsw.class(本文件)
 * @author Yang
 *
 */

public class ChangePswActivity extends BaseActivity implements OnClickListener {

	private EditText mNewPsw;
	private EditText mConPsw;
	private TextView mSubmit;
	private TextView mBack;

	private String newPsw;
	private String conPsw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_psw);

		//初始化组件
		initView();

	}

	private void initView() {
		mNewPsw = (EditText) findViewById(R.id.change_pwd_new);
		mConPsw = (EditText) findViewById(R.id.change_pwd_con);
		mBack = (TextView) findViewById(R.id.change_pwd_back_view);
		mBack.setOnClickListener(this);
		mSubmit = (TextView) findViewById(R.id.change_pwd_submit);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
			case R.id.change_pwd_back_view:
				finish();
				break;
			case R.id.change_pwd_submit:
				changePassword();
				break;
		}
	}

	private void changePassword() {
		newPsw = mNewPsw.getText().toString().trim();
		conPsw = mConPsw.getText().toString().trim();

		if (newPsw == null || newPsw.equals("")) {
			return;
		}

		if (conPsw == null || conPsw.equals("")) {
			return;
		}

		if (!newPsw.equals(conPsw)) {
			return;
		}

		RequestCenter.changePassword(UserManager.getInstance().getUser().data.userId, newPsw, new DisposeDataListener() {
			@Override
			public void onSuccess(Object responseObj) {
				Intent intent = new Intent(ChangePswActivity.this, LoginActivity.class);
				UserManager.getInstance().remove();
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});
	}

}
