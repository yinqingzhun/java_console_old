package com.yqz.console.http;

import java.io.IOException;

import groovy.lang.Tuple2;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHelper {
	static OkHttpClient okHttpClient = new OkHttpClient();

	public static Tuple2<Integer, String> httpHead(String url) {
		Request request = new Request.Builder().url(url).build();
		Call call = okHttpClient.newCall(request);
		try {
			Response response = call.execute();
			return new Tuple2(response.code(), response.body());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Tuple2(0, "");

	}

	public static void main(String[] args) {
		Tuple2<Integer, String> tuple = httpHead(
				"http://n1-pl-agv.autohome.com.cn/v/agv/2017/7/12/08e94903ceb1456eb3773c9a62e0d44b_w960_h540.wm.wm.mp4");
		System.out.println(tuple.getFirst());

	}
}
