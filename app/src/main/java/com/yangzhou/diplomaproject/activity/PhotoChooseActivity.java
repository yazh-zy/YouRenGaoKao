package com.yangzhou.diplomaproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.activity.base.BaseActivity;
import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.user.User;
import com.yangzhou.diplomaproject.network.http.RequestCenter;
import com.zy.sdk.imageloader.ImageLoaderManager;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zy on 2017/4/17.
 */

public class PhotoChooseActivity extends BaseActivity implements View.OnClickListener{

    private TextView mBackView;
    private TextView mLocalView;
    private TextView mCameraView;
    private CircleImageView mHeadView;

    private ImageLoaderManager mImageLoader;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_choose_layout);

        initView();
    }

    private void initView(){
        mBackView = (TextView) findViewById(R.id.choose_photo_back_view);
        mBackView.setOnClickListener(this);
        mLocalView = (TextView) findViewById(R.id.choose_photo_local);
        mLocalView.setOnClickListener(this);
        mCameraView = (TextView) findViewById(R.id.choose_photo_camera);
        mCameraView.setOnClickListener(this);
        mHeadView = (CircleImageView) findViewById(R.id.choose_photo_view);

        mImageLoader = ImageLoaderManager.getInstance(this);
        mImageLoader.displayImage(mHeadView, UserManager.getInstance().getUser().data.photoUrl);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.choose_photo_back_view:
                finish();
                break;
            case R.id.choose_photo_local:
                choseHeadImageFromGallery();
                break;
            case R.id.choose_photo_camera:
                choseHeadImageFromCameraCapture();
                break;
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }


        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mHeadView.setImageBitmap(photo);
            sendImageToServer(photo);
        }
    }

    private void sendImageToServer(Bitmap bm) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

        //TODO
        RequestCenter.sendUserPhoto(img, UserManager.getInstance().getUser().data.userId, new DisposeDataListener(){
            @Override
            public void onSuccess(Object responseObj) {

                Log.e("Photo", "Success");

                User value = (User) responseObj;


                SPManager.getInstance().putString(SPManager.USER_PHOTO, value.data.photoUrl);
                UserManager.getInstance().getUser().data.photoUrl = value.data.photoUrl;
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}
