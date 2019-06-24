package com.zy.sdk.util;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * @author Yang
 * @function 将json转化为实体
 * @date 2017/3/15.
 */

public class ResponseEntityToModule {

    public static Object parseJsonObjectToModule (JSONObject jsonObj, Class<?> clazz) {
        Object moduleObj = null;

        Gson gson = new Gson();

        moduleObj = gson.fromJson(String.valueOf(jsonObj),clazz);

        return moduleObj;
    }
}
