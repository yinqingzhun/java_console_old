package com.yqz.console.tech.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class GuawaRateLimit {

    public static void main(String[] args) throws Exception {



        //每秒10个令牌，预热期2秒
         RateLimiter limiter = RateLimiter.create(10, 1, TimeUnit.SECONDS);//QPS 100

        //每秒10个令牌，支持突发流量
        //   RateLimiter limiter = RateLimiter.create(5);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {
            double spentTime = limiter.acquire();
            long after = System.currentTimeMillis() - start;

            if (spentTime > 0D) {
                System.out.println(i + ",limited,等待:" + spentTime + "，已开始" + after + "毫秒");
            } else {
                System.out.println(i + ",enough" + "，已开始" + after + "毫秒");
            }
            //模拟冷却时间，下一次loop可以认为是bursty开始
            if (i == 4) {
                System.out.println("sleep for a while");
                Thread.sleep(2000);
            }
        }

        System.out.println("total time：" + (System.currentTimeMillis() - start));
    }
}
