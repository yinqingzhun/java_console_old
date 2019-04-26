package com.yqz;

import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SpeedBox {
    private AtomicInteger counter = new AtomicInteger();
    private Thread thread = null;
    private int speed = 0;
    private List<Consumer<Integer>> speedChangedEvents=new ArrayList<>();

    public SpeedBox(int rateInSecond) {
        final int rate = rateInSecond * 1000;
        thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(rate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speed = counter.getAndSet(0);
                speedChangedEvents.forEach(event->event.accept(speed));
            }
        });
        thread.start();
    }

    public void receive(int length) {
        counter.addAndGet(length);
    }

    public void speedChanged(Consumer<Integer> consumer){
        speedChangedEvents.add(consumer);
    }
}