package com.yangzhou.diplomaproject.application;

import android.app.Application;

import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.manager.UserManager;
import com.yangzhou.diplomaproject.module.user.User;
import com.yangzhou.diplomaproject.module.user.UserContent;
import com.yangzhou.diplomaproject.share.ShareManager;

/**
 * Created by zy on 2017/3/21.
 */

public class DiplomaApplication extends Application {

    private static DiplomaApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initShareSDK();

        isLogined();
    }

    public static DiplomaApplication getInstance() {
        return mApplication;
    }

    //初始化ShareSDK
    public void initShareSDK () {
        ShareManager.init(this);
    }


    public void isLogined() {
        String userId = SPManager.getInstance().getString(SPManager.USER_ID, "");
        String userName = SPManager.getInstance().getString(SPManager.USER_NAME, "");
        String userTick = SPManager.getInstance().getString(SPManager.USER_TICK, "");
        String userPhoto = SPManager.getInstance().getString(SPManager.USER_PHOTO, "http://139.129.119.33/userhead/nouserhead.jpg");
        int userTag = SPManager.getInstance().getInt(SPManager.USER_TAG, 0);
        if (userId.equals("") || userId == null) {
            return;
        }

        if (userName.equals("") || userName == null) {
            return;
        }

        if (userTick.equals("") || userTick == null) {
            return;
        }

        if (userPhoto.equals("") || userPhoto == null) {
            return;
        }


        User user = new User();
        UserContent userContent = new UserContent();
        userContent.name = userName;
        userContent.userId = userId;
        userContent.tick = userTick;
        userContent.photoUrl = userPhoto;
        userContent.userTag = userTag;
        user.data = userContent;
        UserManager.getInstance().setUser(user);
    }
}
