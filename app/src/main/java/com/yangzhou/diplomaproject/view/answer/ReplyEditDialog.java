package com.yangzhou.diplomaproject.view.answer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.LoginActivity;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

/**
 * Created by zy on 2017/4/19.
 */

public class ReplyEditDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DisplayMetrics dm;

    private EditText mEditView;
    private TextView mSendView;

    private String mParentId = null;
    private String mAnswerId = null;

    public ReplyEditDialog(Context context) {
        super(context);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_comment_reply_edit);
        initView();
    }

    private void initView() {

        /**
         * 通过获取到dialog的window来控制dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        mEditView = (EditText) findViewById(R.id.reply_add_comment_et);
        mSendView = (TextView) findViewById(R.id.reply_add_comment_send);
        mSendView.setOnClickListener(this);
    }

    public void setmParentId(String parentId) {
        mParentId = parentId;
    }

    public void setmAnswerId (String answerId) {
        mAnswerId = answerId;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.reply_add_comment_send:
                if (UserManager.getInstance().hasLogin()) {

                    sendAddCommentRequest();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }
        }
    }

    private void sendAddCommentRequest() {
        String commentContent = mEditView.getText().toString().trim();

        if (commentContent.equals("") || commentContent == null) {
            return;
        }

        RequestCenter.addComment(commentContent, mAnswerId, mParentId, UserManager.getInstance().getUser().data.userId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dismiss();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });

    }

    private void sendCommentRequest() {
        RequestCenter.getCommentList(mAnswerId, new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

}
