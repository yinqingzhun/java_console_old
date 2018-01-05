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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeriodicAtomicInteger that = (PeriodicAtomicInteger) o;

        if (timestamp != that.timestamp) return false;
        if (tick != that.tick) return false;
        if (!atomicInteger.equals(that.atomicInteger)) return false;
        return timer.equals(that.timer);
    }

    @Override
    public int hashCode() {
        int result = atomicInteger.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (int) (tick ^ (tick >>> 32));
        result = 31 * result + timer.hashCode();
        return result;
    }
}
