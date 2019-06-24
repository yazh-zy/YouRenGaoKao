package com.zy.sdk.okhttp;

import com.zy.sdk.okhttp.response.CommonJsonCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author Yang
 * @function 请求的发送 请求参数的配置 https支持
 */
public class CommonOkHttpClient {
    private static final int TIME_OUT = 30; //超时参数
    private static OkHttpClient mOkHttpClient;

    static {
        //创建client对象的构建者
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //为构建者填充超时时间
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

        //允许重定向
        okHttpClientBuilder.followRedirects(true);

        //生成client对象
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * 发送具体的http请求
     *
     * @param request
     * @param commCallback
     * @return Call
     */
    public static Call sendRequest(Request request, CommonJsonCallback commCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commCallback);

        return call;
    }
}