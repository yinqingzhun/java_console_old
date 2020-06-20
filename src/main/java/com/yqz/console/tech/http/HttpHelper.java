package com.yqz.console.tech.http;

import com.google.common.base.Stopwatch;
import com.yqz.console.tech.utils.JacksonHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();


    public static HttpResult httpGet(String url) {
        return httpGet(url, null, null);
    }

    public static HttpResult httpGet(String url, Map<String, Object> fields) {
        return httpGet(url, fields, null);
    }

    public static HttpResult httpGet(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();

        if (fields != null && fields.size() > 0) {
            String query = String.join("&", fields.entrySet().stream().map(p -> p.getKey() + "=" + (p.getValue() == null ? "" : p.getValue().toString())).collect(Collectors.toList()));
            if (URI.create(url).getQuery() != null && URI.create(url).getQuery().trim() != "")
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
        log.debug("GET request url:{}", url);
        return call(request);
    }

    public static <T, K> T httpGet(String url, Map<String, Object> fields, Map<String, Object> headers, Class<T> parametrized, Class<K> parameterClass) {
        HttpResult httpGet = httpGet(url, fields, headers);
        if (httpGet != null) {
            return getResultFormJson(httpGet, parametrized, parameterClass);
        }
        return null;
    }

    public static <T, K> T getResultFormJson(HttpResult httpResult, Class<T> parametrized, Class<K> parameterClass) {
        if (httpResult.getStatusCode() == 200 && httpResult.getBody() != null) {
            return JacksonHelper.deserialize(httpResult.getBody(), parametrized, parameterClass);
        }
        return null;
    }

    public static <T> T getResultFormJson(HttpResult httpResult, Class<T> clazz) {
        if (httpResult.getStatusCode() == 200 && httpResult.getBody() != null) {
            return (T) JacksonHelper.deserialize(httpResult.getBody(), clazz);
        }
        return null;
    }

    public static <T, K> T httpPost(String url, Map<String, Object> fields, Map<String, Object> headers, Class<T> parametrized, Class<K> parameterClass) {
        HttpResult httpPost = httpPost(url, fields, headers);
        if (httpPost != null) {
            return getResultFormJson(httpPost, parametrized, parameterClass);
        }
        return null;
    }

    private static HttpResult call(Request request) {
        String url = request.url().toString();

        Stopwatch stopwatch = Stopwatch.createStarted();
        Call call = okHttpClient.newCall(request);
        HttpResult build = HttpResult.build(0, null);
        try {
            Response response = call.execute();
            build = HttpResult.build(response.code(), response.body().string());

            return build;
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            stopwatch.stop();
            long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(request.method().toUpperCase()).append(" url:").append(url).append("\t");
            stringBuilder.append("costs:").append(duration).append("ms").append("\t");
            // 请求耗时大于100毫秒的记录日志

            if (build.getStatusCode() != 200) {
                stringBuilder.append("ResponseCode:").append(build.getStatusCode()).append("\n")
                        .append("ResponseBody:").append(build.getBody());
                log.error(stringBuilder.toString());
            } else if (duration > 1000) {
                log.warn(stringBuilder.toString());
            } else if (duration > 300) {
                log.info(stringBuilder.toString());
            } else if (duration > 100) {
                log.debug(stringBuilder.toString());
            } else {
                log.trace(stringBuilder.toString());
            }

        }
        return build;
    }

    public static HttpResult httpPost(String url, Map<String, Object> fields) {
        return httpPost(url, fields, null);
    }

    public static HttpResult httpPostJson(String url, String body) {
        return httpPostJson(url, body, null);
    }

    public static HttpResult httpPostJson(String url, String body, Map<String, Object> headers) {

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
        log.debug("request url:{}", url);
        return call(request);
    }


    public static HttpResult httpPost(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (fields != null) {
            fields.entrySet().forEach(p -> bodyBuilder.add(p.getKey(), (p.getValue() == null ? "" : p.getValue().toString())));
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
        log.debug("POST request url: {}, BODY: {}", url, fields != null ? params(fields) : "");
        return call(request);
    }


    public static HttpResult httpPostMultipart(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Object contentType = headers.get("Content-Type");
        Assert.notNull(contentType, "Content-Type header is null");

        Request.Builder requestBuilder = new Request.Builder();
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MediaType.parse(contentType.toString()));
        if (fields != null) {
            fields.entrySet().forEach(p -> bodyBuilder.addFormDataPart(p.getKey(), (p.getValue() == null ? "" : p.getValue().toString())));
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
        log.debug("POST Multipart request url: {}, BODY: {}", url, fields != null ? params(fields) : "");
        return call(request);
    }


    public static class HttpResult {
        private int httpStatusCode;
        private String body;

        private HttpResult(int httpStatusCode, String body) {
            this.httpStatusCode = httpStatusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return httpStatusCode;
        }

        public String getBody() {
            return body;
        }

        public static HttpResult build(int httpStatusCode, String body) {
            return new HttpResult(httpStatusCode, body);
        }
    }

    public static String params(Map<String, Object> params) {
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
