package com.yangzhou.diplomaproject.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * 修改用户名Activity
 * layout : activity_change_username
 * 
 * url : http://139.129.119.33/yygk/ChangeUserName
 * AsyncTask : changeNameTask.class(本文件)
 * 
 * @author Yang
 *
 */
public class ChangeUserNameActivity extends BaseActivity implements OnClickListener {

	private TextView mBack;
	private TextView mSubmit;
	private EditText mNewName;
	private String newName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_change_username);

		//初始化组件
		initView();
	}

	private void initView() {
		mBack = (TextView) findViewById(R.id.change_username_back_view);
		mBack.setOnClickListener(this);
		mSubmit = (TextView) findViewById(R.id.change_username_submit);
		mSubmit.setOnClickListener(this);
		mNewName = (EditText) findViewById(R.id.change_username_newname);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {

			case R.id.change_username_back_view:
				finish();
				break;
			case R.id.change_username_submit:
				changeUserName();
				break;
		}
	}

	private void changeUserName() {
		newName = mNewName.getText().toString().trim();
		if (newName == null || newName.equals("")){
			return;
		}

		RequestCenter.changeUsername(UserManager.getInstance().getUser().data.userId, newName, new DisposeDataListener() {

			@Override
			public void onSuccess(Object responseObj) {
				SPManager.getInstance().putString(SPManager.USER_NAME, newName);
				finish();
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});
	}
}
