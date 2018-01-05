package com.yqz.console;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class PeriodicAtomicInteger {

    private volatile AtomicInteger atomicInteger=new AtomicInteger();
    private long timestamp=System.currentTimeMillis();
    private long tick=0;
    private Timer timer=new Timer();

    public PeriodicAtomicInteger(long tickInSeconds){
        this.tick=tickInSeconds;

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                atomicInteger.set(0);
            }
        };
         timer.schedule(task,tickInSeconds*1000,tickInSeconds*1000);

    }

    public AtomicInteger getAtomicInteger(){
        return this.atomicInteger;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
