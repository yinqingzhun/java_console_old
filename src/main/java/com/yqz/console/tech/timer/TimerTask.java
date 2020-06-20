package com.yqz.console.tech.timer;


public abstract class TimerTask implements Runnable {
   private Long delayMs;// timestamp in millisecond
    private TimerTaskEntry timerTaskEntry;

    public TimerTask(long delayMs) {
        this.delayMs = delayMs;
       
    }

    public void cancel() {
        synchronized (TimerTask.this) {
            if (timerTaskEntry != null)
                timerTaskEntry.remove();
            timerTaskEntry = null;
        }
    }

    public void setTimerTaskEntry(TimerTaskEntry entry) {
        synchronized (TimerTask.this) {
            // if this timerTask is already held by an existing timer task entry,
            // we will remove such an entry first.
            if (timerTaskEntry != null && timerTaskEntry != entry)
                timerTaskEntry.remove();

            timerTaskEntry = entry;
        }
    }

    public TimerTaskEntry getTimerTaskEntry() {
        return timerTaskEntry;
    }


    public Long getDelayMs() {
        return delayMs;
    }
}
