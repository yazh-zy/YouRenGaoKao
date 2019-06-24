package com.yangzhou.diplomaproject.view.answer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;

/**
 * Created by zy on 2017/4/19.
 */

public class ReplyDialog extends Dialog implements View.OnClickListener {

    private TextView mReplyView;
    private TextView mCancelView;

    private Context mContext;
    private DisplayMetrics dm;

    private String mParentId = null;
    private String mAnswerId = null;

    public ReplyDialog (Context context) {
        super(context);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_comment_reply);
        initView();
    }

    private void initView(){
        /**
         * 通过获取到dialog的window来控制dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        mReplyView = (TextView) findViewById(R.id.reply_view);
        mReplyView.setOnClickListener(this);
        mCancelView = (TextView) findViewById(R.id.reply_cancel_view);
        mCancelView.setOnClickListener(this);
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
            case R.id.reply_view:
                ReplyEditDialog dialog = new ReplyEditDialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setmParentId(mParentId);
                dialog.setmAnswerId(mAnswerId);
                dialog.show();
                dismiss();
                break;
            case R.id.reply_cancel_view:
                dismiss();
                break;
        }

    }
}
