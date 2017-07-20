/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package mr.li.dance.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;
import com.yolanda.nohttp.error.ServerError;

import java.util.List;
import java.util.Map;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/4/21
 * 描述: [JSON解析管理类]
 * 修订历史:
 */
public class JsonMananger {

    static {
        TypeUtils.compatibleWithJavaBean = true;
    }

    private static final String tag = JsonMananger.class.getSimpleName();

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     * @param cls
     * @return
     * @throws ServerError
     */
    public static <T> T jsonToBean(String json, Class<T> cls) throws ServerError {
        T t = null;
        try {
            t = JSON.parseObject(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json
     * @param cls
     * @return
     * @throws ServerError
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> listT = null;
        try {
            listT = JSON.parseArray(json, cls);
        } catch (JSONException exception) {

        }
        return listT;
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj
     * @return
     * @throws ServerError
     */
    public static String beanToJson(Object obj) throws ServerError {
        String result = JSON.toJSONString(obj);
        Log.e(tag, "beanToJson: " + result);
        return result;
    }

    /**
     * 将map对象转化成json字符串
     *
     * @param obj
     * @return
     * @throws ServerError
     */
    public static String mapToJson(Map obj) throws ServerError {
        String result = JSON.toJSONString(obj);
        Log.e(tag, "mapToJson: " + result);

        return result;
    }

    public static <T> T getReponseResult(String resultStr, Class<T> cls) {
        T t = null;
        try {
            t = JsonMananger.jsonToBean(resultStr, cls);
        } catch (ServerError serverError) {
            serverError.printStackTrace();
        }
        return t;
    }


    /**
     * 将json转化成map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> JsonStrToMap(String jsonStr) {
        Map<String, Object> map = JSON.parseObject(
                jsonStr, new TypeReference<Map<String, Object>>() {
                });

        return map;
    }
}
