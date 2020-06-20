package com.yqz.console.tech.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class RequestSignHelper {
    public static String sign(Map<String, Object> fields, String appId, String secret, long timestampInSeconds) {
        SortedMap<String, Object> sMap = new TreeMap<>(fields);
        sMap.put("_timestamp", timestampInSeconds);
        sMap.put("_appid", appId);

        StringBuilder sb = new StringBuilder(secret);
        for (Map.Entry<String, Object> m : sMap.entrySet()) {
            sb.append(m.getKey()).append(m.getValue());
        }
        sb.append(secret);
        System.out.println("before:"+sb);
        String sign = null;
        try {
            sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes("utf-8"));
            return sign;
        } catch (UnsupportedEncodingException e) {

        }

        return null;
    }

    public static Map<String, Object> signAndComplementParams(Map<String, Object> fields, String appId, String secret) {
        long ts = new Date().getTime() / 1000;
        String sign = RequestSignHelper.sign(fields, appId, secret, ts);

        Map<String, Object> newMap = new HashMap<>(fields);
        newMap.put("_sign", sign);
        newMap.put("_timestamp", ts);
        newMap.put("_appid", appId);

        return newMap;
    }

    public static String signAndSerializeParams(Map<String, Object> fields, String appId, String secret) {
        return params(signAndComplementParams(fields,appId,secret));
    }

    private static String params(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();

            for (Map.Entry<String, Object> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    String value = entry.getValue() == null ? "" : entry.getValue().toString();
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
