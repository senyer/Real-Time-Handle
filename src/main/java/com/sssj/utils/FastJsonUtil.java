package com.sssj.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author  TSY
 *
 * @Date:  2018-10-11
 *
 * 	fastjson工具类
 */
public class FastJsonUtil {
 
    private static final SerializeConfig config;
 
    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }
 
    private static final SerializerFeature[] features = {
    		SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };
    
    //转为对象json字符串--带features设置的
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, config, features);
    }
    //转为对象json字符串-- 不带features设置的
    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }
    
 
    //转为object对象
    public static Object toBean(String text) {
        return JSON.parse(text);
    }
    //转为特定对象
    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }
 
    // 转换为数组
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }
 
    // 转换为数组
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }
 
    // 转换为List
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    //获取所有的keySet集合
    public static Set<String> getKeySet(String str) {
    	JSONObject parseObject = JSON.parseObject(str);
		return parseObject.keySet();
    }
    
    
    /**
     *	 字符串转json对象
     * @param keyvalue
     * @return
     */
    public static Object strToJson(String str) {
        Object objectJson  = JSON.parse(str);
        return objectJson;
    }
    
    /**
     * json字符串转化为map
     * @param s
     * @return Map
     */
    public static Map stringToMap(String s) {
        Map m = JSONObject.parseObject(s);
        return m;
    }
    
    /**
     * 	将map转化为string
     * @param m
     * @return String
     */
    public static String mapToString(Map m) {
        String s = JSONObject.toJSONString(m);
        return s;
    }
    
}

