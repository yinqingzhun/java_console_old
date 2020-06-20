package com.yqz.console.tech.concurrent;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试ConcurrentHashMap的元素删除是否安全
 */
public class ConcurrentMapIteration {
    private final Map<String, String> map = new ConcurrentHashMap<String, String>();
    AtomicInteger integer1 = new AtomicInteger();

    private final static int MAP_SIZE = 100000;

    public static void main(String[] args) {
        new ConcurrentMapIteration().run();
    }

    public ConcurrentMapIteration() {
        for (int i = 0; i < MAP_SIZE; i++) {
            map.put("key" + i, UUID.randomUUID().toString());
        }
    }

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final class Accessor implements Runnable {
        private final Map<String, String> map;

        public Accessor(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public void run() {
            //for (Map.Entry<String, String> entry : this.map.entrySet())
            //{
            //  System.out.println(
            //      Thread.currentThread().getName() + " - [" + entry.getKey() + ", " + entry.getValue() + ']'
            //  );
            //}


            map.forEach((k, v) -> {
                System.out.println(
                        Thread.currentThread().getName() + " - [" + k + ", " + ']'
                );
                integer1.incrementAndGet();
            });
        }
    }

    private final class Mutator implements Runnable { 

        private final Map<String, String> map;
        private final Random random = new Random();

        public Mutator(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for (int i = 0; i < MAP_SIZE; i++) {
                this.map.remove("key" + random.nextInt(MAP_SIZE));
                //System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }
    }

    private void run() {
        Accessor a1 = new Accessor(this.map);
        //Accessor a2 = new Accessor(this.map);
        Mutator m = new Mutator(this.map);

        executor.execute(a1);
        executor.execute(m);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(integer1.get());
        //executor.execute(a2);
    }
}