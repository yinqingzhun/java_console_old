package com.yqz.console.tech.concurrent;

import java.util.concurrent.*;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        int N = 5;
        CountDownLatch doneSignal = new CountDownLatch(N);
        Executor e = Executors.newCachedThreadPool();

        for (int i = 0; i < N; ++i) // create and start threads
            e.execute(new WorkerRunnable(doneSignal, i));

        doneSignal.await();           // wait for all to finish
        ((ExecutorService) e).shutdown();
        System.out.println("main thread exit");
       
    }
}

class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;

    WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    public void run() {
        doWork(i);
        doneSignal.countDown();
    }

    void doWork(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("work at " + i);
    }
}