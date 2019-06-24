package com.zy.sdk.okhttp.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * @author Yang
 * @function 接收请求参数，生成Request对象
 */
public class CommonRequest {
    /**
     * 创建Request key-value对
     *
     * @param url
     * @param params
     * @return Request
     */
    public static Request createPostRequest(String url, RequestParams params) {

        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到请求构件类中
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }

        //通过请求构件类的build方法获取到真正的请求体对象
        FormBody mFormBody = mFormBodyBuild.build();
        Request request = new Request.Builder()
                .url(url)
                .post(mFormBody)
                .build();
        return request;
    }


    /**
     * 可以带请求头的Get请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params) {

        //url
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            //拼接url字符串
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=")
                        .append(entry.getValue()).append("&");
            }
        }

        return new Request.Builder()
                .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .build();
    }
}