package com.yqz.console.tech.rx;

import com.yqz.console.tech.http.HttpHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import reactor.core.Exceptions;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class React {
    static Logger logger = LoggerFactory.getLogger(React.class);

    public static void main(String[] args) throws InterruptedException {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);

        System.out.println("exit");
    }

    public static void zipWidth() {
        Flux.fromStream(Stream.generate(() -> System.currentTimeMillis()))
                .zipWith(Flux.interval(Duration.ofSeconds(1)))
                .take(2)
                .map(p -> p.getT1())
                .then()
                .subscribe(s -> System.out.println("consumer: " + s), System.out::println, () -> System.out.println("completion"));
        try {
            Thread.sleep(3100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void context() {
//        final String url = "http://www.baidu.com";
//        final String HTTP_CORRELATION_ID = "reactive.http.library.correlationId";
//        Mono<Tuple2<String, Optional<Context>>> dataAndContext =
//                Mono.just(HTTP_CORRELATION_ID)
//                        .zipWith(Mono.subscriberContext().map(c -> c.getOrEmpty(HTTP_CORRELATION_ID)));
//
//        dataAndContext.handle((dac, sink) -> {
//            if (dac.getBody().isPresent()) {
//                sink.next("PUT <" + dac.getStatusCode() + "> sent to " + url + " with header X-Correlation-ID = "
//                        + dac.getBody().get());
//            } else {
//                sink.next("PUT <" + dac.getStatusCode() + "> sent to " + url);
//            }
//            sink.complete();
//        })
//                .map(msg -> Tuples.of(200, msg))
//                .subscriberContext(Context.of(HTTP_CORRELATION_ID, "2-j3r9afaf92j-afkaf"))
//                .filter(t -> t.getStatusCode() < 300)
//                .map(Tuple2::getBody)
//                .subscribe(System.out::println);
//    }

    public static void windowWhile() {
        Flux.just(1, 3, 5, 2, 4, 6, 11, 12, 13)
                .windowWhile(i -> i % 2 == 0)
                .concatMap(g -> g.defaultIfEmpty(-1)) //show empty windows as -1
                .subscribe(a -> System.out.println("->" + a));
    }

    public static void refCount() {
        Flux<Integer> source = Flux.range(1, 3).doOnSubscribe(s ->
                System.out.println("subscribed to source"));

        Flux<Integer> co = source.publish().refCount(2);

        co.subscribe(System.out::println, e -> {
        }, () -> {
        });
        System.out.println("wait enough count subscribers...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("reach subscribing count soon.");
        co.subscribe(System.out::println, e -> {
        }, () -> {
        });
    }

    public static void group() {
        Flux.just(1, 3, 5, 2, 4, 6, 11, 12, 13).groupBy(i -> i % 2 == 0 ? "even" : "odd")
                .concatMap(g -> g.defaultIfEmpty(-1) // if empty groups, show them
                        .map(String::valueOf) // map to string
                        .startWith(g.key())
                )
                .subscribe(a -> System.out.println(a)); // start with the group's key
    }

    public static void hotPublish() {
        UnicastProcessor<String> hotSource = UnicastProcessor.create();

        Flux<String> hotFlux = hotSource.publish()
                .autoConnect()
                .map(String::toUpperCase);


        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot  Source: " + d));

        hotSource.onNext("blue");
        hotSource.onNext("green");

        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot  Source: " + d));

        hotSource.onNext("orange");
        hotSource.onNext("purple");
        hotSource.onComplete();
    }

    public static void lazyStream() {
        //print from 1 to 10 one by one
        Flux.range(1, 10).toStream().forEach(System.out::println);
        //print from 1 indefinitely
        Flux.interval(Duration.ofMillis(50)).toStream().forEach(System.out::println);
    }

    public static void requestData() {
        SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
        Flux<Integer> ints = Flux.range(1, 4);
        //        ints.subscribe(i -> System.out.println("Next->" + i),
        //                error -> System.err.println("->Error " + error),
        //                () -> {
        //                    System.out.println("->Done");
        //                }
        //                , s -> {
        //                    ss.request(10);
        //                }
        //        );
        ints.subscribe(ss);
    }

    public static void create1() throws InterruptedException {
        MessageProcessor myMessageProcessor = new MessageProcessor();
        Flux<String> flux = Flux.create(sink -> {
                    myMessageProcessor.register(
                            new MyMessageListener<String>() {
                                public void onMessage(List<String> messages) {
                                    for (String s : messages) {
                                        sink.next(s);
                                        //System.out.println("sink:"+s);
                                    }
                                }
                            });
                    //初始化时，响应订阅者要求的消息数
                    sink.onRequest(n -> {
                        List<String> messages = myMessageProcessor.request(1L);
                        for (String s : messages) {
                            sink.next(s);
                        }
                        System.out.println("on request");
                    });

                },
                // Overflow (backpressure) handling, default is BUFFER
                FluxSink.OverflowStrategy.DROP);

        flux.log().publishOn(Schedulers.newSingle("newSingle"), 1);

        flux.subscribe(p -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("received: "+p);
                    myMessageProcessor.receive(System.currentTimeMillis()+"");
                },
                System.out::println,
                () -> System.out.println("completion")
                , p -> p.request(10)//仅订阅了3条消息
        );

       /* AtomicInteger atomicInteger = new AtomicInteger(10);
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (atomicInteger.get() < 1000) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myMessageProcessor.receive(atomicInteger.incrementAndGet() + "");
                }
            }
        };
        thread.run();
        thread.join();*/
    }

    public static void schedulers() {
        StopWatch stopWatch1 = new StopWatch("A");
        StopWatch stopWatch2 = new StopWatch("B");
        stopWatch1.start();
        stopWatch2.start();

        Flux<Integer> flux = Flux.range(1, 5)
                .publishOn(Schedulers.parallel())
                .subscribeOn(Schedulers.elastic());
        flux.subscribe(p -> {
            System.out.println(Thread.currentThread().getName() + "-A," + p);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, System.out::println, () -> {
            stopWatch1.stop();
            System.out.println("A time:" + stopWatch1.getTotalTimeMillis());
        });
        flux.subscribe(p -> {
            System.out.println(Thread.currentThread().getName() + "-B," + p);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, System.out::println, () -> {
            stopWatch2.stop();
            System.out.println("B time:" + stopWatch2.getTotalTimeMillis());
        });


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //todo
    public static void create() throws InterruptedException {
        //        MyEventProcessor myEventProcessor = new MyEventProcessor();
        //        Flux<Integer> bridge = Flux.create(sink -> {
        //            myEventProcessor.register(
        //                    new MyEventListener<Integer>() {
        //                        public void onDataChunk(List<Integer> chunk) {
        //                            for (Integer s : chunk) {
        //                                sink.next(s);
        //                            }
        //                        }
        //                        public void processComplete() {
        //                            sink.complete();
        //                        }
        //                    });
        //        });

        Flux.range(1, 20).subscribe(new BaseSubscriber<Integer>() {
            public void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
                request(1);
            }

            public void hookOnNext(Integer value) {
                System.out.println("rcv: " + value);
                request(1);
                if (value > 10)
                    cancel();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        Thread.sleep(100000);

    }

    public static void coldAndHot() {
        //cold: defer
        //hot :just

        Flux<HttpHelper.HttpResult> flux = Flux
                .defer(() -> Mono.just(HttpHelper.httpGet("http://www.baidu.com")))
                .concatWith(Mono.just(HttpHelper.httpGet("http://www.sogou.com")))
                .subscribeOn(Schedulers.elastic());
        flux.subscribe(p -> {
            logger.debug("[SUBSCRIBE-(1)]-thread: {}, code: {}, url: {}", Thread.currentThread().getName(), p.getStatusCode(), DigestUtils.md5Hex(p.getBody()));
        });
        flux.subscribe(p -> {
            logger.debug("[SUBSCRIBE-(2)]-thread: {}, code: {}, url: {}", Thread.currentThread().getName(), p.getStatusCode(), DigestUtils.md5Hex(p.getBody()));
        });
    }

  /*  public static class MyEventProcessor {
        List<MyEventListener> list = new ArrayList<>();

        public <T> void register(MyEventListener<T> myEventListener) {
            this.list.add(myEventListener);
        }

        public void triggerEvent(String... event) {
            list.forEach(p -> {
                List<String> msgs = Arrays.asList(event);
                p.onDataChunk(msgs);
            });
        }

        public void stopEvent() {
            list.forEach(p -> {
                p.processComplete();
            });
        }
    }*/

    public static class MessageProcessor {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        List<MyMessageListener> messageListeners = new ArrayList<>();

        public void register(MyMessageListener listener) {
            messageListeners.add(listener);
        }

        public List<String> request(Long n) {
            List<String> messages = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                messages.add(String.valueOf(atomicInteger.getAndIncrement()));
            }
            return messages;
        }

        public void receive(String msg) {
            messageListeners.forEach(listener -> {
                List<String> msgs = Arrays.asList(msg);
                listener.onMessage(msgs);
            });
        }
    }

    public static void parallel() {
        Flux.range(1, 2000)
                .parallel().runOn(Schedulers.parallel())
                .subscribe(i ->
                        System.out.println(Thread.currentThread().getName() + " -> " + i));
    }

    interface MyMessageListener<T> {
        public void onMessage(List<String> messages);
    }

    interface MyEventListener<T> {
        void onDataChunk(List<T> chunk);

        void processComplete();
    }

    public static void generate() {
        SampleSubscriber<String> ss = new SampleSubscriber<>();
        Flux<String> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("The generator has terminated or the downstream cancelled. The last state is: " + state));

        flux.subscribe(ss);

    }

    public static void window() {
        //        Flux.range(0, 100)
        //                .window(10)
        //                .flatMap(window -> {
        //                    return window.reduce(new ArrayList<>(), (a, b) -> {
        //                        a.add(b);
        //                        return a;
        //                    });
        //                })
        //                .map((list) -> list.size())
        //                .reduce(0, (a, b) -> a + b)
        //                .doOnSuccess(System.out::println)
        //                .subscribe();


        Flux.range(1, 10).window(3)
                .subscribe(p -> p.doOnNext(j -> System.out.println(j)).subscribe());
    }

    public static void buffer() {
        //Flux.range(1, 10)
        Flux.fromStream(Stream.of(1, 2, 3, 4, 5))
                .buffer(3).subscribe(p -> System.out.println(p));
    }

    //todo
    public static void ss() throws InterruptedException {
        final Flux<Long> source = Flux.interval(Duration.ofMillis(100)).take(10).publish().autoConnect();
        source.subscribe();
        Thread.sleep(200);
        source.toStream().forEach(System.out::println);
    }

    //todo 有疑问
    public static void retryWhen3() throws InterruptedException {
        Flux.concat(Flux.error(new IllegalArgumentException(""))).retryWhen(companion -> companion.take(3))
                // .onErrorReturn(e -> e.getMessage().equals("boom10"),
                // "recovered10")
                // .publishOn(Schedulers.parallel())
                // .onErrorResume(e ->
                // Flux.just(100)).subscribeOn(Schedulers.elastic())
                .subscribe(p -> System.out.println(Thread.currentThread().getName() + "," + p), System.out::println,
                        () -> System.out.println("completion"));
        Thread.sleep(3000);
    }

    public static void retryWhen2() throws InterruptedException {
        Flux<String> flux = Flux.<String>error(new IllegalArgumentException())
                .retryWhen(companion -> companion.doOnNext(s -> System.out.println(s + " at " + LocalTime.now()))
                        .zipWith(Flux.range(1, 4), (error, index) -> {
                            if (index < 4)
                                return index;
                            else
                                throw Exceptions.propagate(error);
                        }).flatMap(index -> Mono.delay(Duration.ofMillis(index * 100)))
                        .doOnNext(s -> System.out.println("retried at " + LocalTime.now())));
        flux.subscribe(p -> System.out.println("s:" + p), System.out::println);
        Thread.sleep(1000);
    }

    public static void retryWhen() {
        Flux<String> flux = Flux.<String>error(new IllegalArgumentException())
                .retryWhen(companion -> companion.zipWith(Flux.range(1, 4), (error, index) -> {
                    if (index < 14)
                        return index;
                    else
                        throw Exceptions.propagate(error);
                }));
        flux.subscribe(p -> System.out.println("s:" + p), System.out::println);
    }

    public static void handle() {
        Flux.range(1, 10).handle((value, sink) -> {
            if (value % 2 == 0)
                sink.next(value);
            // if (a == 5)
            // throw new IllegalArgumentException();
        }).subscribeOn(Schedulers.immediate()).subscribe(System.out::println);
    }

    public static void pull1() {
        final AtomicInteger ai = new AtomicInteger(1);
        Flux<Integer> flux = Flux.generate(ArrayList::new, (list, sink) -> {
            int value = ai.getAndIncrement();
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        });
        flux.subscribe(s -> System.out.println("s1: " + s));
        flux.subscribe(s -> System.out.println("s2: " + s));
    }

    public static void pull() {
        Flux<String> flux = Flux.generate(sink -> {
            sink.next("Hello");
            // sink.complete();
        });
        flux.subscribe(p -> System.out.println("s1:" + p), System.out::println, () -> System.out.println("completion"),
                a -> {
                    int i = 10;
                    while (i-- > 0) {
                        a.request(1);
                    }
                    a.cancel();
                });

        flux.subscribe(p -> System.out.println("s2:" + p), System.out::println, () -> System.out.println("completion"),
                a -> {
                    int i = 10;
                    while (i-- > 0) {
                        a.request(1);
                    }
                    a.cancel();
                });
    }

    public static void subScribe() {
        Flux.range(5, 3).subscribe(System.out::println, System.out::println, () -> {
            System.out.println("done");
        }, a -> {
            a.request(2);
            // a.cancel();
            // System.out.println();
        });
    }

    public static void simpleDemo() throws InterruptedException {
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
    }

    public static void generateInInterval() throws InterruptedException {
        Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).subscribe((p) -> System.out.println(p));
        Thread.sleep(2100);
    }

    public static void generateKeyValuePair() throws InterruptedException {
        Flux<Long> flux = Flux.fromStream(Stream.generate(() -> System.currentTimeMillis()))
                .zipWith(Flux.interval(Duration.ofSeconds(1))).take(2).map(p -> p.getT1());

        flux.subscribe(System.out::println, System.out::println, () -> System.out.println("completed"));
        // then() only signals when completion or error
        // flux.then().subscribe(System.out::println, System.out::println, () ->
        // System.out.println("completed"));

        Thread.sleep(2100);

    }

    public static class SampleSubscriber<T> extends BaseSubscriber<T> {
        Random random = new Random();

        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribed");
            request(1);
        }

        public void hookOnNext(T value) {
            System.out.println("hookOnNext:" + value);
            request(1);
            try {

                int n = random.nextInt(1000);
                System.out.println("sleep:" + n / 1000.0);
                Thread.sleep(n);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Return current {@link Subscription}
         *
         * @return current {@link Subscription}
         */
        @Override
        protected Subscription upstream() {
            return super.upstream();
        }

        /**
         * Optional hook for completion processing. Defaults to doing nothing.
         */
        @Override
        protected void hookOnComplete() {
            super.hookOnComplete();
            System.out.println("hookOnComplete");
        }

        /**
         * Optional hook for error processing. Default is to call
         * {@link Exceptions#errorCallbackNotImplemented(Throwable)}.
         *
         * @param throwable the error to process
         */
        @Override
        protected void hookOnError(Throwable throwable) {
            super.hookOnError(throwable);
        }

        /**
         * Optional hook executed when the subscription is cancelled by calling this
         * Subscriber's {@link #cancel()} method. Defaults to doing nothing.
         */
        @Override
        protected void hookOnCancel() {
            super.hookOnCancel();
        }

        /**
         * Optional hook executed after any of the termination events (onError, onComplete,
         * cancel). The hook is executed in addition to and after {@link #hookOnError(Throwable)},
         * {@link #hookOnComplete()} and {@link #hookOnCancel()} hooks, even if these callbacks
         * fail. Defaults to doing nothing. A failure of the callback will be caught by
         * {@link Operators#onErrorDropped(Throwable, Context)}.
         *
         * @param type the type of termination event that triggered the hook
         *             ({@link SignalType#ON_ERROR}, {@link SignalType#ON_COMPLETE} or
         *             {@link SignalType#CANCEL})
         */
        @Override
        protected void hookFinally(SignalType type) {
            super.hookFinally(type);
        }
    }
}
