/**
 * Copyright  2015-2020 100msh.com All Rights Reserved
 */
package com.morse.basemoduel.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Morse
 * @date 2015-9-14下午3:49:28
 */

public class JsonUtils {

    /**
     * json转换成javabean对象
     *
     * @param string
     * @param t
     * @return
     */
    public static <T> T parseObject(String string, Class<T> t) {
        try {
            return JSON.parseObject(string, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串转换成json对象
     *
     * @param string
     * @return
     */
    public static <T> JSONObject parseObject(String string) {
        try {
            return JSON.parseObject(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> JSONObject toJson(Class<T> t) {
        try {
            return (JSONObject) JSON.toJSON(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
