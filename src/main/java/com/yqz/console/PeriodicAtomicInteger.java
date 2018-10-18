package com.yqz.console;


import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class PeriodicAtomicInteger {

    private volatile AtomicInteger atomicInteger = new AtomicInteger();
    private LocalDateTime startTime = LocalDateTime.now();
    private long tick = 0;
    private Timer timer = new Timer();

    public PeriodicAtomicInteger(long tickInSeconds) {
        this.tick = tickInSeconds;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                atomicInteger.set(0);
                startTime = LocalDateTime.now();
            }
        }, tickInSeconds * 1000, tickInSeconds * 1000);
    }

    public AtomicInteger getAtomicInteger() {
        return this.atomicInteger;
    }

    @Override
    public String toString() {
        return String.format("startTime: %s, endTime: %s, atomicInteger value: %s", startTime.toString(), startTime.plusSeconds(this.tick).toString(), this.atomicInteger.get());
    }
}
