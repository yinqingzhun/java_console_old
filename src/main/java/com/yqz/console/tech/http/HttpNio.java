package com.yqz.console.tech.http;

import com.google.common.base.Stopwatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import reactor.core.publisher.Flux;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HttpNio {
    public static void main(String[] args) throws InterruptedException {
        String[] urisToGet = {
                "http://www.baidu.com/",
                "http://www.so.com/",
                "https://www.qq.com/",
        };
        List<String> list = new LinkedList();
        Flux.range(0, 10).subscribe(p -> {
            list.add(urisToGet[p % 3]);
        });

        run2(list);
        run1(list);
    }

    private static void run1(List<String> list) throws InterruptedException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000)
                .setSocketTimeout(50000)
                .setConnectionRequestTimeout(1000)
                .build();

        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();

        //设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }

        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(100);


        final CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().
                setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        httpAsyncClient.start();


        Stopwatch stopwatch = Stopwatch.createStarted();
        final CountDownLatch latch = new CountDownLatch(list.size());
        for (final String uri : list) {
            final HttpGet httpget = new HttpGet(uri);

            httpAsyncClient.execute(httpget, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    latch.countDown();
                    //System.out.println(httpget.getRequestLine() + "->" + response.getStatusLine());
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    System.out.println(httpget.getRequestLine() + "->" + ex);
                }

                public void cancelled() {
                    latch.countDown();
                    System.out.println(httpget.getRequestLine() + " cancelled");
                }

            });
        }
        latch.await();
        stopwatch.stop();
        System.out.println("async cost: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private static void run2(List<String> list) {


        Stopwatch stopwatch = Stopwatch.createStarted();
        for (final String uri : list) {
            HttpHelper.HttpResult result = HttpHelper.httpGet(uri);
            //System.out.println(uri + "-->" + result.getT1());
        }
        stopwatch.stop();
        System.out.println("sync cost: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}

 
