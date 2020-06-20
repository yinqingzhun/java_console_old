package com.yqz.console.tech.timer;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TimingWheel {
    int level = 0;
    long tickMs;//指针移动一次代表的时长
    int wheelSize;//指针一圈走的次数
    long startMs;//开始时刻
    AtomicInteger taskCounter;//任务计数器
    DelayQueue queue;//延时队列

    long interval;//每一圈的总时长
    TimerTaskList[] buckets;//每个刻度对应的任务列表

    long currentTime; // rounding down to multiple of tickMs

    // overflowWheel can potentially be updated and read by two concurrent threads through add().
    // Therefore, it needs to be volatile due to the issue of Double-Checked Locking pattern with JVM
    TimingWheel overflowWheel = null;

    public TimingWheel(long tickMs, int wheelSize, long startMs, AtomicInteger taskCounter, DelayQueue delayQueue) {
        this(tickMs, wheelSize, startMs, taskCounter, delayQueue, 0);
    }

    private TimingWheel(long tickMs, int wheelSize, long startMs, AtomicInteger taskCounter, DelayQueue delayQueue, int level) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.startMs = startMs;
        this.taskCounter = taskCounter;
        this.queue = delayQueue;
        this.interval = tickMs * wheelSize;
        this.level = level;

        this.currentTime = startMs - (startMs % tickMs);

        this.buckets = Arrays.stream(new Object[wheelSize]).map(p -> new TimerTaskList(taskCounter)).toArray(TimerTaskList[]::new);

        log.info("create level {} TimingWheel, tickMs={}, startMs={}, currentTime={}", level, tickMs, startMs, currentTime);
    }

    //添加上层时间轮
    private void addOverflowWheel() {
        synchronized (TimingWheel.this) {
            if (overflowWheel == null) {
                overflowWheel = new TimingWheel(interval, wheelSize, currentTime, taskCounter, queue, level + 1);
            }
        }
    }

    //添加一个延迟任务
    public boolean add(TimerTaskEntry timerTaskEntry) {
        long expiration = timerTaskEntry.getExpirationMs();

        if (timerTaskEntry.isCancelled()) {
            // Cancelled
            return false;
        } else if (expiration < currentTime + tickMs) {
            // Already expired
            return false;
        } else if (expiration < currentTime + interval) {//未超过本层时钟
            // Put in its own bucket
            long virtualId = expiration / tickMs;
            TimerTaskList bucket = buckets[(int) (virtualId % wheelSize)];
            bucket.add(timerTaskEntry);

            // Set the bucket expiration time
            if (bucket.setExpiration(virtualId * tickMs)) {
                // The bucket needs to be enqueued because it was an expired bucket
                // We only need to enqueue the bucket when its expiration time has changed, i.e. the wheel has advanced
                // and the previous buckets gets reused; further calls to set the expiration within the same wheel cycle
                // will pass in the same value and hence return false, thus the bucket with the same expiration will not
                // be enqueued multiple times.
                queue.offer(bucket);
                log.info("add new tasklist {} delay {} ms at level {}", bucket, bucket.getDelay(TimeUnit.MILLISECONDS), this.level);
            }else
            log.info("add new task {} delay {} ms at level {}", bucket, bucket.getDelay(TimeUnit.MILLISECONDS), this.level);
            return true;
        } else {//超过本层，升级到高层时钟
            // Out of the interval. Put it into the parent timer
            if (overflowWheel == null)
                addOverflowWheel();
            overflowWheel.add(timerTaskEntry);

        }
        return true;
    }

    /**
     * Try to advance the clock-推进时钟
     *
     * @param timeMs 推进的时刻，单位毫秒
     */
    public void advanceClock(Long timeMs) {
        //log.info("try advanceClock on {} at level {} TimingWheel advanceClock, tickMs={}, currentTime={}", timeMs, level, tickMs, currentTime);
        if (timeMs >= currentTime + tickMs) {
            currentTime = timeMs - (timeMs % tickMs);
            log.info("level {} TimingWheel --> advanceClock, tickMs={}, currentTime={}", level, tickMs, currentTime);
            // Try to advance the clock of the overflow wheel if present
            if (overflowWheel != null)
                overflowWheel.advanceClock(currentTime);
        }
    }
}
