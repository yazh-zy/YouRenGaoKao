package com.yangzhou.diplomaproject.manager;

import com.yangzhou.diplomaproject.db.SPManager;
import com.yangzhou.diplomaproject.module.user.User;
import com.yangzhou.diplomaproject.module.user.UserContent;

/**
 * Created by zy on 2017/3/23.
 */

public class UserManager {

    private static UserManager userManager = null;

    private User mUser;
    private UserContent mUserContent;

    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
                return userManager;
            }
        }else {
            return userManager;
        }
    }

    //保存user方法
    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    //判断用户是否已经登录
    public boolean hasLogin() {
        return mUser == null ? false : true;
    }

    public void remove() {
        SPManager.getInstance().clear();
        mUser = null;
    }
}
