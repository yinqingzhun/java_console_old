package com.yqz.console.http;

import com.yqz.console.util.ConvertHelper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();


    public static Tuple2<Integer, String> httpGet(String url) {
        return httpGet(url, null, null);
    }

    public static Tuple2<Integer, String> httpGet(String url, Map<String, Object> fields) {
        return httpGet(url, fields, null);
    }

    public static Tuple2<Integer, String> httpGet(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();

        if (fields != null && fields.size() > 0) {
            String query = String.join("&", fields.entrySet().stream().map(p -> p.getKey() + "=" + ConvertHelper.defaultValue(p.getValue(), "").toString()).collect(Collectors.toList()));
            if (StringUtils.hasText(URI.create(url).getQuery()))
                url += "&" + query;
            else
                url += "?" + query;
        }

        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .build();
        
        return call(request);
    }

    private static Tuple2<Integer, String> call(Request request) {
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return Tuple2.build(response.code(), response.body().string());
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return Tuple2.build(0, null);
    }

    public static Tuple2<Integer, String> httpPost(String url, Map<String, Object> fields) {
        return httpPost(url, fields, null);
    }

    public static Tuple2<Integer, String> httpPostJson(String url, String body) {
        return httpPostJson(url, body, null);
    }

    public static Tuple2<Integer, String> httpPostJson(String url, String body, Map<String, Object> headers) {

        Request.Builder requestBuilder = new Request.Builder();

        RequestBody reqBody = RequestBody.create(MediaType.parse("application/json"), body);

        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .post(reqBody)
                .build();
       
        return call(request);
    }


    public static Tuple2<Integer, String> httpPost(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (fields != null) {
            fields.entrySet().forEach(p -> bodyBuilder.add(p.getKey(), ConvertHelper.defaultValue(p.getValue(), "").toString()));
        }
        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .post(bodyBuilder.build())
                .build();
    
        return call(request);
    }

    public static class Tuple2<T1, T2> {
        private final Object[] contents;

        private Tuple2(T1 first, T2 second) {
            contents = new Object[]{first, second};
        }

        public T1 getT1() {
            return (T1) contents[0];
        }

        public T2 getT2() {
            return (T2) contents[1];
        }

        public static <T1, T2> Tuple2<T1, T2> build(T1 first, T2 second) {
            return new Tuple2(first, second);
        }
    }

    public static String urlEncoding(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            sb.append("?");
            for (Map.Entry<String, String> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return url + sb.toString();
    }

  

}