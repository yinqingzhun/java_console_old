package com.yqz.console.rx;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BackPressure {
    public static class SlowSubscriber extends BaseSubscriber<MyEventSource.MyEvent> {

        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            request(1);     // 订阅时请求1个数据
        }

        @Override
        protected void hookOnNext(MyEventSource.MyEvent event) {
            System.out.println("                      receive <<< " + event.getMessage());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            request(2);     // 每处理完1个数据，就再请求1个
        }

        @Override
        protected void hookOnError(Throwable throwable) {
            System.err.println("                      receive <<< " + throwable);
        }

        @Override
        protected void hookOnComplete() {
            //countDownLatch.countDown();
            System.err.println("                      complete <<< " );
        }
    }

    interface MyEventListener {
        void onNewEvent(MyEventSource.MyEvent event);

        void onEventStopped();
    }

    public static class MyEventSource {
        List<MyEventListener> messageListeners = new ArrayList<>();

        public void register(MyEventListener listener) {
            messageListeners.add(listener);
        }

        public void newEvent(MyEvent event) {
            messageListeners.forEach(eventListener -> eventListener.onNewEvent(event));
        }

        public void eventStopped() {

        }

        public static class MyEvent {
            private Date date;
            private String message;

            public MyEvent(Date date, String msg) {
                this.date = date;
                this.message = msg;
            }

            public String getMessage() {
                return message;
            }
        }

    }

    MyEventSource eventSource = new MyEventSource();

    /**
     * 使用create方法生成“快的发布者”。
     *
     * @param strategy 回压策略
     * @return Flux
     */
    private Flux<MyEventSource.MyEvent> createFlux(FluxSink.OverflowStrategy strategy) {

        return Flux.create(sink -> eventSource.register(new MyEventListener() {
            @Override
            public void onNewEvent(MyEventSource.MyEvent event) {
                System.out.println("            publish >>> " + event.getMessage());
                sink.next(event);
            }

            @Override
            public void onEventStopped() {
                sink.complete();
            }
        }), strategy);  // 1
    }

    /**
     * 生成MyEvent。
     *
     * @param count  生成MyEvent的个数。
     * @param millis 每个MyEvent之间的时间间隔。
     */
    public void generateEvent(int count, int millis) {
        // 循环生成MyEvent，每个MyEvent间隔millis毫秒
        for (int i = 0; i < count; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException e) {
            }
            eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "Event-" + i));
        }
        eventSource.eventStopped();
    }

    public static void main(String[] args) throws Exception {
        BackPressure backPressure = new BackPressure();
        Flux<BackPressure.MyEventSource.MyEvent> fastPublisher = backPressure.createFlux(FluxSink.OverflowStrategy.LATEST)
                .doOnRequest(n -> System.out.println("===  request: " + n + " ==="))    // 2
                .publishOn(Schedulers.newSingle("newSingle"), 1)
        ;

        fastPublisher.subscribe(new BackPressure.SlowSubscriber());

         
        Thread thread = new Thread() {
            @Override
            public void run() {
                backPressure.generateEvent(20, 1);
            }
        };
        thread.run();
        thread.join();
    }
}
