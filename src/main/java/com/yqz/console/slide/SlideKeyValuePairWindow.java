package com.yqz.console.slide;

import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SlideKeyValuePairWindow<K> {
    private int maxSize = 0;
    private volatile int startIndex = 0;
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, K> map = Collections.synchronizedMap(new HashMap<>());
    private Consumer<SortedMap<Integer, K>> reachMaxSizeListener;
    private Consumer<SortedMap<Integer, K>> timeoutListener;
    private Timer timer = new Timer();
    private long timeoutInMilli;
    private boolean timerRunning = false;


    public SlideKeyValuePairWindow(int maxSize
            , Consumer<SortedMap<Integer, K>> reachMaxSizeListener
            , long timeoutInMilli
            , Consumer<SortedMap<Integer, K>> timeoutListener) {
        this.maxSize = maxSize;
        this.reachMaxSizeListener = reachMaxSizeListener;
        if (this.reachMaxSizeListener != null && this.maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than zero with reachMaxSizeListener");
        }

        this.timeoutInMilli = timeoutInMilli;
        this.timeoutListener = timeoutListener;

        if (timeoutListener != null && this.timeoutInMilli <= 0) {
            throw new IllegalArgumentException("timeoutInMilli must be greater than zero with timeoutListener");
        }
    }

    public SlideKeyValuePairWindow(int maxSize, Consumer<SortedMap<Integer, K>> reachMaxSizeListener) {
        this(maxSize, reachMaxSizeListener, 0, null);
    }

    public SlideKeyValuePairWindow(
            long timeoutInMilli
            , Consumer<SortedMap<Integer, K>> timeoutListener) {
        this(0, null, timeoutInMilli, timeoutListener);

    }

    private void safeIncreaseIndex(Integer index) {
        if (index > startIndex) {
            startIndex = index;
        }

    }

    public void put(Integer index, K value) {
        if (index > startIndex && map.putIfAbsent(index, value) == null) {
            safeIncreaseIndex(index);
            counter.incrementAndGet();
            if (reachMaxSizeListener != null) {
                if (counter.get() >= maxSize) {
                    SortedMap<Integer, K> result = SlideKeyValuePairWindow.this.getAndClean();
                    if (result.size() > 0)
                        reachMaxSizeListener.accept(result);
                }
            }

            if (timeoutListener != null) {
                startSchedule();
            }
        }
    }


    private void startSchedule() {
        if (!timerRunning) {
            synchronized (this.map) {
                if (!timerRunning) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            SortedMap<Integer, K> result = SlideKeyValuePairWindow.this.getAndClean();
                            if (result.size() > 0)
                                timeoutListener.accept(result);

                            timerRunning = false;
                        }
                    }, timeoutInMilli);
                    timerRunning = true;
                }

            }
        }

    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    private SortedMap<Integer, K> getAndClean() {

        synchronized (map) {
            SortedMap<Integer, K> resultMap = new TreeMap<>();
            resultMap.putAll(this.map);
            this.map.clear();
            this.counter.set(0);
            return resultMap;
        }


    }


    private static ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    static Set<Integer> set = new HashSet<>();
    static Set<Integer> set2 = new HashSet<>();

    private static Thread test(SlideKeyValuePairWindow<Integer> window) {
        int size = 1000;
        Random random = new Random();
        return new Thread() {
            @Override
            public void run() {
                for (int i = 1; i <= size; i++) {
                    try {
                        if (System.currentTimeMillis() % i != 0) {
                            window.put(i, i);
                            set.add(i);
                        }


                        Thread.sleep(random.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

    }

    public static void main(String[] args) throws Exception {
        SlideKeyValuePairWindow<Integer> window = new SlideKeyValuePairWindow<>(10,
                map -> {
                    set2.addAll(map.keySet());
                    System.out.println(ObjectUtils.nullSafeToString(map.keySet().stream().sorted(Integer::compareTo).toArray()));
                }
                , 500
                , map -> {
            set2.addAll(map.keySet());
            System.out.println(ObjectUtils.nullSafeToString(map.keySet().stream().sorted(Integer::compareTo).toArray()));
        });


        Thread t1 = test(window);
        t1.run();

        Thread t2 = test(window);
        t2.run();

        Thread t3 = test(window);
        t3.run();

        t1.join();
        t2.join();
        t3.join();

        System.out.println(window.getStartIndex());

        Integer[] integers = set.toArray(new Integer[0]);
        Integer[] integers2 = set2.toArray(new Integer[0]);
        System.out.println(integers.length == integers2.length);
        //for (int i = 0; i < integers2.length; i++) {
        //    if (!integers[i].equals(integers2[i]))
        //        System.out.println("");
        //}

        System.out.println(com.yqz.console.utils.CollectionUtils.difference(set,set2));
        //ses.scheduleAtFixedRate(() -> {
        //    window.safeIncreaseIndex(atomicInteger.incrementAndGet());
        //}, 10, 2, TimeUnit.MILLISECONDS);

        //ses.scheduleAtFixedRate(() -> {
        //    System.out.println(slidingWindowCounter);
        //    slidingWindowCounter.advance();
        //}, 1, 1, TimeUnit.SECONDS);
        //
        //ses.scheduleAtFixedRate(() -> {
        //    slidingWindowCounter.resizeWindow(new Random().nextInt(10));
        //}, 1, 10, TimeUnit.SECONDS);


    }

}
