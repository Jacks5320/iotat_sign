package com.iotat.sign.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Result {
    public static String resultStatus(Integer code, String msg) {
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("msg", msg);
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 数据格式返回
     * @param code
     * @param data
     * @return
     */
    public static String resultData (Integer code, Object data){
        JSONObject obj = new JSONObject();
        obj.put("code",code);
        obj.put("msg","成功");
        obj.put("data",data);

        return JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
    }

}
