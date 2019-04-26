package com.yqz.console;

import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

/*public class HttpHelper {
    static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static HttpResult httpGet(String url) {
        logger.debug("http get from " + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return HttpResult.build(response.code(), new String(response.body().bytes())).url(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResult httpPost(String url, Map<String, String> fields, Map<String, String> headers) {
        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        if (fields != null) {
            fields.entrySet().forEach(p -> builder.add(p.getKey(), p.getValue()));
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return HttpResult.build(response.code(), response.body().toString()).url(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // public static BasicNameValuePair httpPostJson(String url, String json,
    // Map<String, String> headers) {
    // OkHttpClient okHttpClient = new OkHttpClient();
    // MediaType jsonMediaType = MediaType.parse("application/json;
    // charset=utf-8");
    // RequestBody body = RequestBody.create(jsonMediaType, json);
    //
    // Request request = new Request.Builder().url(url).post(body).build();
    //
    // Call call = okHttpClient.newCall(request);
    // try {
    // Response response = call.execute();
    // return new BasicNameValuePair(String.valueOf(response.code()),
    // response.body().string());
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    static String getBodyString(Response response) {
        try {
            if (!String.valueOf(response.code()).startsWith("2"))
                logger.warn("unexpected http status code {} returned from {}", response.code(),
                        response.request().url().toString());

            ResponseBody body = response.body();

            Charset charset = Charsets.UTF_8;
            if (body.contentType() != null && body.contentType().charset() != null)
                charset = body.contentType().charset();

            return new String(getBytesFromInputStream(body.byteStream()), charset);
        } catch (IOException e) {
            logger.warn(e.toString());
        }
        return null;
    }

    static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {

        try {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (OutOfMemoryError e) {
            logger.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    public static class HttpResult {

        private Integer code;
        private String body;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public HttpResult(Integer code, String body) {
            this.code = code;
            this.body = body;
        }

        public Integer getCode() {
            return this.code;
        }

        public String getBody() {
            return this.body;
        }

        public static HttpResult build(Integer code, String body) {
            return new HttpResult(code, body);
        }

        public HttpResult url(String url) {
            this.url = url;
            return this;
        }

    }
}*/
