package com.yqz.console.tech.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

import java.time.Duration;

public class MonoSamples {
    static final Logger LOG = LoggerFactory.getLogger(MonoSamples.class);


    public static void main(String... args) throws Exception {
        // Deferred is the publisher, Promise the consumer
        MonoProcessor<String> promise = MonoProcessor.create();

        Mono<String> result = promise.doOnSuccess(p -> LOG.info("Promise completed {}", p))
                .doOnTerminate(( ) -> LOG.info("Got value: "))
                .doOnError(t -> LOG.error(t.getMessage(), t))
                ;

        promise.onNext("Hello World!");
        //promise.onError(new IllegalArgumentException("Hello Shmello! :P"));

        String s = result.block(Duration.ofMillis(500));
        LOG.info("s={}", s);
    }
}
