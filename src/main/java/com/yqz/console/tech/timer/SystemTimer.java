package com.yqz.console.tech.timer;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class SystemTimer implements Timer {
    String executorName;
    Long tickMs;
    int wheelSize;
    Long startMs;
    DelayQueue delayQueue = new DelayQueue<TimerTaskList>();
    AtomicInteger taskCounter = new AtomicInteger(0);
    TimingWheel timingWheel;
    // timeout timer
    ExecutorService taskExecutor = Executors.newFixedThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            return KafkaThread.nonDaemon("executor-" + executorName, runnable);
        }
    });

    // Locks used to protect data structures while ticking

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

    ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    public SystemTimer(String executorName) {
        this(executorName, 1L, 20, System.nanoTime() / 1000000);
    }

    public SystemTimer(String executorName, Long tickMs, int wheelSize, Long startMs) {
        this.executorName = executorName;
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.startMs = startMs;
        timingWheel = new TimingWheel(tickMs, wheelSize, startMs, taskCounter, delayQueue);
    }

    @Override
    public void add(TimerTask timerTask) {
        readLock.lock();
        try {
            addTimerTaskEntry(new TimerTaskEntry(timerTask, timerTask.getDelayMs() + System.nanoTime() / 1000000));
        } finally {
            readLock.unlock();
        }
    }

    private void addTimerTaskEntry(TimerTaskEntry timerTaskEntry) {
        if (!timingWheel.add(timerTaskEntry)) {
            // Already expired or cancelled
            if (!timerTaskEntry.isCancelled()) {
                taskExecutor.submit(timerTaskEntry.getTimerTask());
            }
        }
    }

    @Override
    public Boolean advanceClock(Long timeoutMs) {
        TimerTaskList bucket = null;
        try {
            //获取延迟队列中到期的任务列表
            bucket = (TimerTaskList) delayQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (bucket != null) {

            writeLock.lock();
            try {
                while (bucket != null) {
                    timingWheel.advanceClock(bucket.getExpiration());
                    bucket.flush(p -> addTimerTaskEntry(p));
                    //检查队列中是否还有其他到期的任务列表
                    bucket = (TimerTaskList) delayQueue.poll();
                }
            } finally {
                writeLock.unlock();
            }
            return true;
        }
        return false;


    }

    @Override
    public int size() {
        return taskCounter.get();
    }

    @Override
    public void shutdown() {
        taskExecutor.shutdown();
    }


    public static void main(String[] args) {
        SystemTimer timer = new SystemTimer("st");

        timer.add(newTimerTask(500));
        timer.add(newTimerTask(501));
        timer.add(newTimerTask(511));
        //timer.add(newTimerTask(10000));

        while (true) {
            timer.advanceClock(1L);
        }

        //timer.shutdown();
    }

    private static TimerTask newTimerTask(long delayMs) {
        LocalDateTime startTime = LocalDateTime.now();
        TimerTask timerTask = new TimerTask(delayMs) {
            @Override
            public void run() {
                log.info("started after {} ms", Duration.between(startTime, LocalDateTime.now()).toMillis());
            }
        };
        return timerTask;
    }
}
