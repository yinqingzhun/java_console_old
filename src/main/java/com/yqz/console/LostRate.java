package com.yqz.console;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LostRate {

    private int size;
    private volatile int indexCounter = 0;
    private LostRateItem[] lostRateItemArray = null;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public LostRate(int intervalInMillisecond, int size) {
        this.size = size;
        this.lostRateItemArray = new LostRateItem[size];
        for (int i = 0; i < size; i++) {
            lostRateItemArray[i] = new LostRateItem();
        }

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            int idx = incrementIndex();
            LostRateItem lostRateItem = lostRateItemArray[idx];

            lostRateItem.sendedCounter.set(0);
            lostRateItem.receivedCounter.set(0);

        }, intervalInMillisecond, intervalInMillisecond, TimeUnit.MILLISECONDS);
    }

    private int getIndex() {
        return indexCounter;
    }

    private synchronized int incrementIndex() {
        if (++indexCounter >= this.size)
            indexCounter = 0;
        return indexCounter;
    }


    public void regSend(int count) {
        LostRateItem lostRateItem = lostRateItemArray[getIndex()];
        lostRateItem.sendedCounter.addAndGet(count);
    }

    public void regAck(int count) {
        LostRateItem lostRateItem = lostRateItemArray[getIndex()];
        lostRateItem.receivedCounter.addAndGet(count);
    }

    /**
     * 发送数据的丢包率
     *
     * @return 丢包率。当有数据发送时，丢包率不小于0；否则，丢包率为-1
     */
    public double rate() {
        int rcv = Arrays.stream(lostRateItemArray).map(p -> p.receivedCounter.get()).reduce(0, (a, b) -> a + b).intValue();
        int send = Arrays.stream(lostRateItemArray).map(p -> p.sendedCounter.get()).reduce(0, (a, b) -> a + b).intValue();
        if (send == 0) {
            log.info("send count is 0");
            return -1;
        }

        double rate = Math.round((1 - rcv * 1.0 / send) * 100) * 1.0 / 100;
        log.info("lostRate  send:{}, rcv:{}, rate:{}", send, rcv, rate);
        return rate < 0 ? 0 : rate;
    }


    private class LostRateItem {
        public AtomicInteger sendedCounter = new AtomicInteger();
        public AtomicInteger receivedCounter = new AtomicInteger();
    }


    public static void main(String[] args) throws Exception {
        int intervalInMs = 100;
        final int count = 50;
        LostRate lostRate = new LostRate(intervalInMs, count);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        for (int i = 0; i < count; i++) {
            scheduledExecutorService.schedule(() -> lostRate.regSend(1), intervalInMs, TimeUnit.MILLISECONDS);
            scheduledExecutorService.schedule(() -> lostRate.regAck(1), intervalInMs + 300, TimeUnit.MILLISECONDS);
        }


        Thread thread = new Thread(() -> {
            int c = count;
            while (c-- > 0) {
                System.out.printf("\tlostRate: %s\n",lostRate.rate()*100);
                try {
                    TimeUnit.MILLISECONDS.sleep(intervalInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
        thread.join();


    }
}
