package com.yqz.console.timer;

/**
 * 延迟任务实体
 */
public class TimerTaskEntry implements Comparable<TimerTaskEntry> {
    private TimerTask timerTask;
    private long expirationMs;

    private volatile TimerTaskList list = null;
    private volatile TimerTaskEntry next = null;
    private volatile TimerTaskEntry prev = null;

    public TimerTaskEntry(TimerTask timerTask, long expirationMs) {
        this.timerTask = timerTask;
        this.expirationMs = expirationMs;
        if (timerTask != null) 
            timerTask.setTimerTaskEntry(this);
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public TimerTask getTimerTask() {
        return timerTask;
    }

    public boolean isCancelled() {
        return timerTask.getTimerTaskEntry() != this;
    }    

    public void remove() {
        TimerTaskList currentList = list;
        // If remove is called when another thread is moving the entry from a task entry list to another,
        // this may fail to remove the entry due to the change of value of list. Thus, we retry until the list becomes null.
        // In a rare case, this thread sees null and exits the loop, but the other thread insert the entry to another list later.
        while (currentList != null) {
            currentList.remove(this);
            currentList = list;
        }
    }

    @Override
    public int compareTo(TimerTaskEntry that) {
        long r = this.expirationMs - that.expirationMs;
        if (r > 0)
            return 1;
        else if (r < 0)
            return -1;
        return 0;
    }


    public TimerTaskList getList() {
        return list;
    }

    public void setList(TimerTaskList list) {
        this.list = list;
    }

    public TimerTaskEntry getNext() {
        return next;
    }

    public void setNext(TimerTaskEntry next) {
        this.next = next;
    }

    public TimerTaskEntry getPrev() {
        return prev;
    }

    public void setPrev(TimerTaskEntry prev) {
        this.prev = prev;
    }
}
