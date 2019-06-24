package com.yangzhou.diplomaproject.network.http;

import com.yangzhou.diplomaproject.module.answer.BaseAnswerModel;
import com.yangzhou.diplomaproject.module.clazz.BaseClazzModel;
import com.yangzhou.diplomaproject.module.comment.BaseCommentModel;
import com.yangzhou.diplomaproject.module.news.BaseNewsModel;
import com.yangzhou.diplomaproject.module.online.BaseOnlineModel;
import com.yangzhou.diplomaproject.module.question.BaseQuestionModel;
import com.yangzhou.diplomaproject.module.question.QuestionBodyValue;
import com.yangzhou.diplomaproject.module.update.UpdateModel;
import com.yangzhou.diplomaproject.module.user.User;
import com.zy.sdk.okhttp.CommonOkHttpClient;
import com.zy.sdk.okhttp.listener.DisposeDataHandle;
import com.zy.sdk.okhttp.listener.DisposeDataListener;
import com.zy.sdk.okhttp.request.CommonRequest;
import com.zy.sdk.okhttp.request.RequestParams;
import com.zy.sdk.okhttp.response.CommonJsonCallback;

/**
 * @author Yang
 * @function 存放所有页面的数据请求
 * @date 2017/3/16.
 */

public class RequestCenter {

    //根据参数发送所有post请求
    private static void postRequest(String url, RequestParams params,
                                    DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.sendRequest(CommonRequest.createGetRequest(url, params),
                new CommonJsonCallback(new DisposeDataHandle(listener, clazz)));
    }


    /**
     * 发送Answer页请求
     *
     * @param listener
     */
    public static void requestQuestionListViewData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.QUESTION_HOME_URL, null, listener, BaseQuestionModel.class);
    }

    /**
     * 发送Online页请求
     * @param listener
     */
    public static void requestOnlineListViewData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.ONLINE_HOME_URL, null, listener, BaseOnlineModel.class);
    }

    public static void requestNewsListViewData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.NEWS_HOME_URL, null, listener, BaseNewsModel.class);
    }

    /**
     * 用户登录请求
     * @param userName
     * @param password
     * @param listener
     */
    public static void login(String userName, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("account", userName);
        params.put("password", password);
        RequestCenter.postRequest(HttpConstants.USER_LOGIN_URL, params, listener, User.class);
    }

    /**
     * 用户注册请求
     * @param account
     * @param password
     * @param listener
     */
    public static void register(String account, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("account", account);
        params.put("pwd", password);

        RequestCenter.postRequest(HttpConstants.USER_REGISTER_URL, params, listener, User.class);
    }

    public static void checkAccount(String account, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("account", account);

        RequestCenter.postRequest(HttpConstants.USER_REGISTER_CHECK_ACCOUNT, params, listener, User.class);
    }

    public static void addQuestions(String title, String content, String authorId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("authorId", authorId);
        params.put("title", title);
        params.put("content", content);

        RequestCenter.postRequest(HttpConstants.ADD_QUESTIONS, params, listener, QuestionBodyValue.class);
    }

    public static void getAnswersList(String questionId, String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("questionId", questionId);
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.GET_ANSWERS_LIST, params, listener, BaseAnswerModel.class);
    }

    public static void addAnswerRequest(String questionId, String userId, String content, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("questionId", questionId);
        params.put("userId", userId);
        params.put("content", content);

        RequestCenter.postRequest(HttpConstants.ADD_ANSWER, params, listener, BaseAnswerModel.class);
    }

    public static void editAnswerRequest(String questionId, String userId, String content, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("questionId", questionId);
        params.put("userId", userId);
        params.put("content", content);

        RequestCenter.postRequest(HttpConstants.EDIT_ANSWER, params, listener, BaseAnswerModel.class);
    }

    public static void getCommentList (String answerId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("answerId", answerId);
        RequestCenter.postRequest(HttpConstants.GET_COMMENT_LIST, params, listener, BaseCommentModel.class);
    }

    public static void addComment (String comment, String answerId, String parentId, String userId, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("answerId", answerId);
        params.put("comment", comment);
        params.put("parentId", parentId);
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.ADD_COMMENT, params, listener, BaseCommentModel.class);

    }

    public static void changeUsername(String userId, String userName, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("userName", userName);

        RequestCenter.postRequest(HttpConstants.CHANGE_USERNAME, params, listener, User.class);
    }

    public static void changePassword(String userId, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("password", password);

        RequestCenter.postRequest(HttpConstants.CHANGE_PASSWORD, params, listener, User.class);
    }

    public static void sendUserPhoto(String image, String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("image", image);
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.SEND_PHOTO, params, listener, User.class);
    }

    public static void sendLikeRequest (String answerId, String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("answerId", answerId);
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.LIKE, params, listener, BaseAnswerModel.class);
    }

    public static void getClassDetail(String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.GET_CLASS_DETAIL, params, listener, BaseClazzModel.class);
    }

    public static void startClass(String classId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("classId", classId);

        RequestCenter.postRequest(HttpConstants.START_CLASS, params, listener, BaseClazzModel.class);
    }

    public static void endClass(String classId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("classId", classId);

        RequestCenter.postRequest(HttpConstants.END_CLASS, params, listener, BaseClazzModel.class);
    }

    public static void getClazzComment(String clazzId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("clazzId", clazzId);

        RequestCenter.postRequest(HttpConstants.CLAZZ_COMMENT, params, listener, BaseCommentModel.class);
    }

    public static void sendClazzComment(String comment, float stars, String clazzId, String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("clazzId", clazzId);
        params.put("comment", comment);
        params.put("stars", stars+"");
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.SEND_CLAZZ_COMMENT, params, listener, BaseCommentModel.class);
    }

    public static void getAnswer(String questionId, String userId, DisposeDataListener listener){
        RequestParams params = new RequestParams();
        params.put("questionId", questionId);
        params.put("userId", userId);

        RequestCenter.postRequest(HttpConstants.GET_ANSWER, params, listener, BaseAnswerModel.class);
    }


    /**
     * 应用版本号请求
     *
     * @param listener
     */
    public static void checkVersion(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateModel.class);
    }
}