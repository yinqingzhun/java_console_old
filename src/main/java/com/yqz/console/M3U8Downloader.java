package com.yqz.console;

import java.io.IOException;

import com.yqz.console.http.HttpHelper;
import com.yqz.console.m3u8.ParseException;
import com.yqz.console.m3u8.Playlist;

public class M3U8Downloader {

	public static void main(String[] args) throws IOException, InterruptedException, ParseException {

		HttpHelper.Tuple2<Integer,String> s = HttpHelper.httpGet(
				"http://n1-pl-agv.autohome.com.cn/v/agv/2017/7/28/eb43a429f02b469ca1eb60c45940d290_w1280_h720.wm.m3u8");

		Playlist plist = Playlist.parse(s.getT2());
		plist.getElements().stream()
				.forEach(p -> System.out.printf("duration=%s,url=%s\r\n", p.getDuration(), p.getURI()));

		System.out.print("total duration is "+plist.getElements().stream().map(p -> p.getDuration()).reduce((a, b) -> a + b).get());

	}

}
