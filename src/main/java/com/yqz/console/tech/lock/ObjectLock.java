package com.yqz.console.tech.lock;

import lombok.SneakyThrows;

public class ObjectLock {

    public static void main(String[] args) throws Exception {
        Object lock = new Object();

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    synchronized (lock) {
                        System.out.println(i++);
                        lock.wait();
                        System.out.println(i  +" - ");
                    }
                    Thread.sleep(1000);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        Thread thread1=   new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        System.out.println(System.currentTimeMillis());
                        lock.notify();
                    }
                    Thread.sleep(1000);
                }
            }
        });
//        thread1.start();

        thread.join();
    }

    public static void test() {

    }
}
