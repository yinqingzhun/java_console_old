package com.yqz.console.tech.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RetreenLockDemo {
    public static void main(String[] args) throws Exception {
        Queue<Integer> queue = new Queue<Integer>(3);
        queue.put(1);
        queue.put(2);
        //System.out.println(queue.take());
        //System.out.println(queue.take());
        queue.put(3);
        //queue.put(4);
        //System.out.println(queue.take());
        System.out.println(queue.count);
    }

    public static class Queue<T> {
        private final T[] items;
        private final ReentrantLock lock = new ReentrantLock();
        private Condition notFull = lock.newCondition();
        private Condition notEmpty = lock.newCondition();
        private int head, tail, count;

        public Queue(int maxSize) {
            items = (T[]) new Object[maxSize];
        }

        public Queue() {
            this(10);
        }

        public void put(T t) throws InterruptedException {
            lock.lock();
            try {
                while (count == items.length) {
                    //数组满时，线程进入等待队列挂起。线程被唤醒时，从这里返回。
                    notFull.await();
                }
                items[tail] = t;
                if (++tail == items.length) {
                    tail = 0;
                }
                ++count;
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        public T take() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    notEmpty.await();
                }
                T o = items[head];
                items[head] = null;//GC
                if (++head == items.length) {
                    head = 0;
                }
                --count;
                notFull.signal();
                return o;
            } finally {
                lock.unlock();
            }
        }
    }

}
