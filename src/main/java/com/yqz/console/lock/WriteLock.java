package com.yqz.console.lock;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class WriteLock {
    private ReentrantLock msgQueueLock = new ReentrantLock();
    private Condition msgQueueFull = msgQueueLock.newCondition();
    private volatile long msgQueueSize = 0;
    private final long MSG_QUEUE_MAX_SIZE = 1024;
    private final LinkedBlockingQueue<byte[]> msgQueue = new LinkedBlockingQueue();//原始包队列（有限制）

    public static void main(String[] args) throws Exception {
        WriteLock writeLock = new WriteLock();


        Thread d = new Thread(() -> {
            Random random = new Random();
            while (true) {
                int count = random.nextInt(1024);
                //System.out.println("count: " + count);
                writeLock.add(new byte[count]);
            }
        });
        d.start();

        Thread t = new Thread(() -> writeLock.consume());
        t.start();

        t.join();
        d.join();
    }

    public void add(byte[] bytes) {

        msgQueueLock.lock();
        try {
            System.out.println("add data :" + bytes.length);
            if (msgQueueSize >= MSG_QUEUE_MAX_SIZE) {
                System.out.printf("-----传输层消息队列空间已满，size=%s/%s bytes \r\n", msgQueueSize, MSG_QUEUE_MAX_SIZE);
                //msgQueueFull.await();
            }

            msgQueue.put(bytes);
            msgQueueSize += bytes.length;

        } catch (InterruptedException e) {
            System.out.println("添加待发送消息失败,");
        } finally {
            msgQueueLock.unlock();
        }

    }

    public void consume() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msgQueueLock.lock();
            try {
                byte[] bytes = msgQueue.poll();
                if (bytes == null) {
                    
                } else {
                    msgQueueSize -= bytes.length;
                    System.out.println("consume data: "+bytes.length);
                }

                if (msgQueueSize < MSG_QUEUE_MAX_SIZE) {
                    if(msgQueue.size()>0)
                    System.out.printf("传输层消息队列空间空闲，size=%s bytes\r\n", msgQueueSize);
                    msgQueueFull.signal();
                }


            } finally {
                msgQueueLock.unlock();
            }
        }
    }
}
