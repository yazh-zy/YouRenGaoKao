package com.yangzhou.diplomaproject.network.http;

/**
 * @author Yang
 * @function 所有请求相关地址
 * @date 2017/3/16.
 */
public class HttpConstants {

    //
    private static final String ROOT_URL = "http://139.129.119.33/DiplomaProject";

    private static final String TEST_ROOT_URL = "http://192.168.86.2:8080/DiplomaProject";

    //Answer页面列表请求
    public static String QUESTION_HOME_URL = ROOT_URL + "/QuestionsList";

    public static String ONLINE_HOME_URL = ROOT_URL + "/OnlineClassList";

    public static String NEWS_HOME_URL = ROOT_URL + "/NewsList";

    //LoginActivity用户登录请求
    public static String USER_LOGIN_URL = ROOT_URL + "/Login";

    //RegisterActivity用户注册
    public static String USER_REGISTER_URL = ROOT_URL + "/RegisterServlet";

    //RegisterActivity用户注册邮箱唯一性检查
    public static String USER_REGISTER_CHECK_ACCOUNT = ROOT_URL + "/CheckAccount";

    //提问
    public static String ADD_QUESTIONS = ROOT_URL + "/AddQuestion";

    //获取回答列表
    public static String GET_ANSWERS_LIST = ROOT_URL + "/GetAnswersList";

    public static String ADD_ANSWER = ROOT_URL + "/AddAnswer";

    public static String GET_COMMENT_LIST = ROOT_URL + "/GetAnswerComment";


    public static String ADD_COMMENT = ROOT_URL + "/AddComment";

    public static String CHANGE_USERNAME = ROOT_URL + "/ChangeName";

    public static String CHANGE_PASSWORD = ROOT_URL + "/ChangePassword";

    public static String SEND_PHOTO = ROOT_URL + "/SendUserPhoto";

    public static String LIKE = ROOT_URL + "/Like";

    public static String GET_CLASS_DETAIL = ROOT_URL + "/GetClazzDetail";

    public static String START_CLASS = ROOT_URL + "/StartClazz";

    public static String END_CLASS = ROOT_URL + "/EndClazz";

    public static String CLAZZ_COMMENT = ROOT_URL + "/GetClazzComment";

    public static String SEND_CLAZZ_COMMENT = ROOT_URL + "/SendClazzComment";

    public static String GET_ANSWER = ROOT_URL + "/GetAnswer";

    public static String EDIT_ANSWER = ROOT_URL + "/EditAnswer";

    public static String CHECK_UPDATE = ROOT_URL + "/CheckUpdate";

}
