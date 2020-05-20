package com.yqz.console.tech.timer;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class TimerTaskList implements Delayed {
    AtomicInteger taskCounter;
    // TimerTaskList forms a doubly linked cyclic list using a dummy root entry
    // root.next points to the head
    // root.prev points to the tail

   private TimerTaskEntry root = null;

    public TimerTaskList(AtomicInteger atomicInteger) {
        this.taskCounter = atomicInteger;

        root = new TimerTaskEntry(null, -1);
        root.setNext(root);
        root.setPrev(root);
    }


    private AtomicLong expiration = new AtomicLong(-1L);

    // Set the bucket's expiration time
    // Returns true if the expiration time is changed
    public Boolean setExpiration(Long expirationMs) {
        return expiration.getAndSet(expirationMs) != expirationMs;
    }

    // Get the bucket's expiration time
    public Long getExpiration() {
        return expiration.get();
    }

    // Apply the supplied function to each of tasks in this list
    private void foreach(Consumer<TimerTask> f) {
        synchronized (TimerTaskList.this) {
            TimerTaskEntry entry = root.getNext();
            while (entry != root) {
                TimerTaskEntry nextEntry = entry.getNext();

                if (!entry.isCancelled())
                    f.accept(entry.getTimerTask());

                entry = nextEntry;
            }
        }
    }

    // Add a timer task entry to this list
    public void add(TimerTaskEntry timerTaskEntry) {
        boolean done = false;
        while (!done) {
            // Remove the timer task entry if it is already in any other list
            // We do this outside of the sync block below to avoid deadlocking.
            // We may retry until timerTaskEntry.list becomes null.
            timerTaskEntry.remove();

            synchronized (TimerTaskList.this) {
                synchronized (timerTaskEntry) {
                    if (timerTaskEntry.getList() == null) {
                        // put the timer task entry to the end of the list. (root.prev points to the tail entry)
                        TimerTaskEntry tail = root.getPrev();
                        timerTaskEntry.setNext(root);
                        timerTaskEntry.setPrev(tail);
                        timerTaskEntry.setList(this);
                        tail.setNext(timerTaskEntry);
                        root.setPrev(timerTaskEntry);
                        taskCounter.incrementAndGet();
                        done = true;
                    }
                }
            }
        }
    }

    // Remove the specified timer task entry from this list
    public void remove(TimerTaskEntry timerTaskEntry) {
        synchronized (this) {
            synchronized (timerTaskEntry) {
                if (timerTaskEntry.getList() == this) {
                    timerTaskEntry.getNext().setPrev(timerTaskEntry.getPrev());
                    timerTaskEntry.getPrev().setNext(timerTaskEntry.getNext());
                    timerTaskEntry.setNext(null);
                    timerTaskEntry.setPrev(null);
                    timerTaskEntry.setList(null);
                    taskCounter.decrementAndGet();
                }
            }
        }
    }


    /**
     * Remove all task entries and apply the supplied function to each of them
     * 删除任务表中的所有任务，并重新安排任务在时间轮中的位置（降级轮盘）
     * @param f
     */
    public void flush(Consumer<TimerTaskEntry> f) {
        synchronized (TimerTaskList.this) {
            TimerTaskEntry head = root.getNext();
            while (head != root) {
                remove(head);
                f.accept(head);
                head = root.getNext();
            }
            expiration.set(-1L);
        }
    }

    public long getDelay(TimeUnit unit) {
        long left = getExpiration() - System.nanoTime() / 1000000;
        return unit.convert(Math.max(left, 0), TimeUnit.MILLISECONDS);
    }

    public int compareTo(Delayed d) {

        TimerTaskList other = (TimerTaskList) d;

        if (getExpiration() < other.getExpiration())
            return -1;
        else if (getExpiration() > other.getExpiration())
            return 1;
        else
            return 0;
    }

}

 
 
 
