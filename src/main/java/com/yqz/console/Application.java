package com.yqz.console;

import java.io.*;
import java.net.URI;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;
import com.yqz.console.utils.HashHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.yqz.console.HttpHelper.HttpResult;
import com.yqz.console.json.JsonUtils;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import sun.awt.SunHints;

public class Application {
    static Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) throws IOException, InterruptedException {
        PeriodicAtomicInteger periodicAtomicInteger = new PeriodicAtomicInteger(3);
        int i = 20;
        while (i-- > 0) {
            AtomicInteger atomicInteger = periodicAtomicInteger.getAtomicInteger();
            System.out.println(atomicInteger.getAndIncrement());
            atomicInteger = null;
            Thread.sleep(500);
        }
        System.out.println("finished: ");
        //paths.forEach(System.out::println);
//        Flux.range(1, 1000).map(p -> p.longValue()).parallel().runOn(Schedulers.parallel()).subscribe(p -> {
//        }, System.out::println, () -> System.out.println("completion"));

        //ExecutorService es = Executors.newFixedThreadPool(2);
        //List<Callable<String>> lst = new ArrayList<>();
        //for (int i = 0; i < 10000; i++) {
        //    final int n = i;
        //    lst.add(new Callable<String>() {
        //        @Override
        //        public String call() throws InterruptedException {
        //            final String name = Thread.currentThread().getName();
        //            Object[] objects = new Object[2];
        //
        //
        //
        //
        //            machineTicks.compute(name, (key, value) -> {
        //                Long tick = System.currentTimeMillis();
        //                if (value == null) {
        //                    objects[0] = true;
        //                    objects[1] = tick;
        //                }else{
        //                    objects[0] = tick - value > 10;
        //                    objects[1] = (boolean) objects[0] ? tick : value;
        //                }
        //
        //                return (long) objects[1];
        //            });
        //
        //            if ((boolean) objects[0])
        //                System.out.println(name + "," + (long) objects[1]);
        //
        //            return null;
        //        }
        //    });
        //}
        //
        //es.invokeAll(lst);
        //
        //es.shutdown();
        System.out.println("");
    }

    private static String encodePath(String path) {
        if (!StringUtils.hasText(path))
            return path;

        try {
            path = UriUtils.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error encoding parameter {}", e.getMessage(), e);
        }
        return path;
    }
}
