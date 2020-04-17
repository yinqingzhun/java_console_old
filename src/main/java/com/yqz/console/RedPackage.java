package com.yqz.console;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RedPackage {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            DataCenter.currentAwardId.set(0);
            run();
        }

    }

    public static void run() throws Exception {

        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Integer>> map = new ConcurrentHashMap<>();


        int count = 100;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(count);
        Random random = new Random();
        for (int i = 0; i < count; ++i) // create and start threads
        {
            final int n = i;
            new Thread(() -> {
                try {
                    startSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int sz = 1000;
                for (int j = 0; j < sz; j++) {
                    int uid = (103540 + n);

                    final boolean[] hit = {false};

                    int newId = DataCenter.currentAwardId.updateAndGet(x -> {
                        if (x < 100) {
                            hit[0] = true;
                            return x + 1;
                        }
                        hit[0] = false;
                        return x;
                    });

                    if (hit[0]) {
                        ConcurrentLinkedQueue<Integer> queue = map.compute(uid, (k, v) ->
                                v == null ? new ConcurrentLinkedQueue() : v
                        );
                        queue.add(newId);
                    }

                }

                doneSignal.countDown();
            }).start();

        }


        // don't let run yet
        startSignal.countDown();      // let all threads proceed

        doneSignal.await();           // wait for all to finish


        List<Integer> awardIds = new ArrayList();
        map.forEach((k, v) -> {
            v.stream().forEach(p -> {

                awardIds.add(p);
                ;
            });
        });

      System.out.printf("count: %s/%s, list: %s\n", awardIds.stream().distinct().collect(Collectors.toList()).size(), awardIds.size(), Joiner.on(",").join(awardIds.stream().sorted().collect(Collectors.toList())));
        //        System.out.println(DataCenter.currentAwardId.get());
    }


    static class DataCenter {

        public static final AtomicInteger currentAwardId = new AtomicInteger();
        public static final AtomicInteger maxAwardId = new AtomicInteger(100);

    }

}