package com.yangzhou.diplomaproject.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.LoginActivity;
import com.yangzhou.diplomaproject.activity.UserSettingActivity;
import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.update.UpdateModel;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.yangzhou.diplomaproject.share.ShareDialog;
import com.yangzhou.diplomaproject.update.UpdateService;
import com.yangzhou.diplomaproject.util.Util;
import com.yangzhou.diplomaproject.view.CommonDialog;
import com.yangzhou.diplomaproject.view.MyQrCodeDialog;
import com.yangzhou.diplomaproject.view.fragment.BaseFragment;
import com.zy.sdk.imageloader.ImageLoaderManager;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.aliquis.yangzhou.diplomaproject.R.id.share_app_view;

/**
 * Created by yang zhou on 09/03/2017.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View mContentView;

    private RelativeLayout mLoginLayout;
    private CircleImageView mPhotoView;
    private TextView mLoginInfoView;
    private TextView mLoginView;
    private RelativeLayout mLoginedLayout;
    private TextView mUserNameView;
    private TextView mTickView;
    private TextView mShareView;
    private TextView mQrCodeView;
    private TextView mUpdateView;

    private ImageLoaderManager mImageLoader;

    /**
     * 广播接收器
     */

    private LoginBroadcastReceiver mReceiver = new LoginBroadcastReceiver();


    public MineFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        registerLoginBroadcast();
    }

    //注册广播
    private void registerLoginBroadcast() {

        IntentFilter filter = new IntentFilter(LoginActivity.LOGIN_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
    }


    //销毁广播
    private void unregisterLoginBroadcast() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mLoginLayout = (RelativeLayout) mContentView.findViewById(R.id.login_layout);
        mLoginLayout.setOnClickListener(this);
        mLoginedLayout = (RelativeLayout) mContentView.findViewById(R.id.logined_layout);
        mLoginedLayout.setOnClickListener(this);

        mPhotoView = (CircleImageView) mContentView.findViewById(R.id.user_photo_view);
        mPhotoView.setOnClickListener(this);
        mLoginView = (TextView) mContentView.findViewById(R.id.login_view);
        mLoginView.setOnClickListener(this);

        mUserNameView = (TextView) mContentView.findViewById(R.id.username_view);
        mTickView = (TextView) mContentView.findViewById(R.id.tick_view);

        mShareView = (TextView) mContentView.findViewById(share_app_view);
        mShareView.setOnClickListener(this);
        mQrCodeView = (TextView) mContentView.findViewById(R.id.my_qrcode_view);
        mQrCodeView.setOnClickListener(this);
        mUpdateView = (TextView) mContentView.findViewById(R.id.update_view);
        mUpdateView.setOnClickListener(this);

        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterLoginBroadcast();
//        unregisterLogoutBroadcast();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_layout:
            case R.id.login_view:
                if (!UserManager.getInstance().hasLogin()) {
                    toLogin();
                }
                break;
            case R.id.logined_layout:
                if (UserManager.getInstance().hasLogin()) {
                    Intent intent = new Intent (mContext, UserSettingActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.share_app_view:
                shareApp();
                break;
            case R.id.my_qrcode_view:
                if (!UserManager.getInstance().hasLogin()) {
                    //未登陆，去登陆。
                    toLogin();
                } else {
                    //已登陆根据用户ID生成二维码显示
                    MyQrCodeDialog dialog = new MyQrCodeDialog(mContext);
                    dialog.show();
                }
                break;
            case R.id.update_view:
                checkVersion();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //根据用户信息更新我们的fragment
        if (UserManager.getInstance().hasLogin()) {
            Log.e("LOGIN", "登录了");
            if (mLoginedLayout.getVisibility() == View.GONE) {
                mLoginLayout.setVisibility(View.GONE);
                mLoginedLayout.setVisibility(View.VISIBLE);
            }
            mUserNameView.setText(SPManager.getInstance().getString(SPManager.USER_NAME, ""));
            mTickView.setText(SPManager.getInstance().getString(SPManager.USER_TICK, ""));
            mImageLoader.displayImage(mPhotoView, SPManager.getInstance().getString(SPManager.USER_PHOTO, "http://139.129.119.33/userhead/nouserhead.jpg"));
        } else {
            Log.e("LOGIN", "没有登录");
            mLoginLayout.setVisibility(View.VISIBLE);
            mLoginedLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 分享app
     */
    private void shareApp() {
        ShareDialog dialog = new ShareDialog(mContext, false);
        dialog.setShareType(Platform.SHARE_IMAGE);
        dialog.setShareTitle("Test");
        dialog.setShareTitleUrl("http://139.129.119.33/yygk/");
        dialog.setShareText("有用高考");
        dialog.setShareSite("有用高考");
        dialog.setShareSiteUrl("http://139.129.119.33/yygk/");
//        dialog.setImagePhoto(Environment.getExternalStorageDirectory() + "");
        dialog.show();
    }

    /**
     * 转到登录页面
     */
    private void toLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 自定义广播接收器，处理登录广播
     */
    private class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //更新Fragment UI
            mLoginLayout.setVisibility(View.GONE);
            mLoginedLayout.setVisibility(View.VISIBLE);
            //展示用户信息
            mUserNameView.setText(UserManager.getInstance().getUser().data.name);
            mTickView.setText(UserManager.getInstance().getUser().data.tick);
            mImageLoader.displayImage(mPhotoView, UserManager.getInstance().getUser().data.photoUrl);
        }
    }

    //检查更新
    private void checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            //请求成功回调
            @Override
            public void onSuccess(Object responseObj) {
                final UpdateModel updateModel = (UpdateModel) responseObj;
                //判断本地版本号和服务器版本号
                if(Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
                    //有新版本
                    CommonDialog dialog = new CommonDialog(mContext,
                            getString(R.string.update_new_version),
                            getString(R.string.update_title),
                            getString(R.string.update_install),
                            getString(R.string.cancel),
                            new CommonDialog.DialogClickListener() {
                                @Override
                                public void onDialogClick() {
                                    //启动更新服务安装
                                    Intent intent = new Intent(mContext, UpdateService.class);
                                    mContext.startService(intent);
                                }
                            });
                } else {
                    //没有新版本
                    Toast.makeText(mContext, R.string.no_new_version_msg, Toast.LENGTH_SHORT).show();
                }
             }
            //请求失败回调
            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
