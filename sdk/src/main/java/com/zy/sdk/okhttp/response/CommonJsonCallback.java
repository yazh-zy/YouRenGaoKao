package com.zy.sdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.zy.sdk.okhttp.exception.OkHttpException;
import com.zy.sdk.okhttp.listener.DisposeDataHandle;
import com.zy.sdk.okhttp.listener.DisposeDataListener;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Yang
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback implements Callback {

    //与服务器返回字段的对应关系
    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0; //
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    //自定义异常类型
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler; //进行消息转发
    private DisposeDataListener mListener; //回调
    private Class<?> mClass; //字节码

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 请求失败处理
     * @param call
     * @param ioexception
     */
    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }

    /**
     * 响应处理函数
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    /**
     * 处理服务器返回的响应数据
     * @param responseObj
     */
    private void handleResponse(Object responseObj) {
        //保证代码健壮性
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            JSONObject result = new JSONObject(responseObj.toString());
            //解析json
            if (result.has(RESULT_CODE)) {
                //从json对象中取出响应码，如果为0，则是正确响应
                if (result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    //不需要解析，直接返回到应用层
                    if (mClass == null) {
                        mListener.onSuccess(responseObj);
                    } else {
                        //需要将json对象转化为实体对象 gson
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(String.valueOf(result),mClass);
                        //正确转化为实体对象
                        if (obj != null) {
                            mListener.onSuccess(obj);
                        } else {
                            //返回的不是合法json
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }
                    }
                } else {
                    //将服务器返回的异常回调到应用层处理
                    mListener.onFailure(new OkHttpException(OTHER_ERROR, result.get(RESULT_CODE)));
                }
            }
        } catch (Exception e) {

            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}