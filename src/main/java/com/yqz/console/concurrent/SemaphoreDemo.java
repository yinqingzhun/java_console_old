package com.yqz.console.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(5);

        // 模拟20个客户端访问
        for (int index = 0; index < 20; index++) {
            final int NO = index;
            Runnable run = () -> {
                try {
                    // 获取许可
                    semaphore.acquire();
                    System.out.println("No." + NO + " Accessing");
                    Thread.sleep((long) (Math.random() * 2000));
                    // 访问完后，释放
                    semaphore.release();
                    System.out.println("No." + NO +" Released, Left Permits:" + semaphore.availablePermits());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService.execute(run);
        }

        // 退出线程池
        executorService.shutdown();

    }

} 