package com.yqz.console.algorithm;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo {

    public static class DelayItem implements Delayed {
        private long future;
        private String name;

        public DelayItem(String name, long delayInMilliSeconds) {
            this.name = name;
            this.future =System.nanoTime()+delayInMilliSeconds*1000000;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.future - System.nanoTime(), unit);
        }

        @Override
        public int compareTo(Delayed o) {
            if (o.getDelay(TimeUnit.NANOSECONDS) - this.getDelay(TimeUnit.NANOSECONDS) > 0)
                return -1;
            else if (o.getDelay(TimeUnit.NANOSECONDS) - this.getDelay(TimeUnit.NANOSECONDS) < 0)
                return 1;
            else return 0;
        }

        public String getName() {
            return name;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayItem> queue = new DelayQueue<>();

        queue.put(new DelayItem("1", 30));
        queue.put(new DelayItem("2",20 ));
        queue.put(new DelayItem("3", 10));


        int i = 0;
        while (true) {
            DelayItem item = queue.take();
            System.out.println(item.getName());
            i++;
            if (i >= 3)
                break;
        }
    }
}
