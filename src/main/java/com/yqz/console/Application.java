package com.yqz.console;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

public class Application {
    static Logger logger = LoggerFactory.getLogger(Application.class);
    public final static ObjectMapper objMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException {

        // ExecutorService es = Executors.newFixedThreadPool(2);
        // es.submit(new Runnable() {
        //
        // @Override
        // public void run() {
        //
        // throw new IllegalArgumentException();
        //
        // }
        // });
        // es.submit(new Runnable() {
        //
        // @Override
        // public void run() {
        //
        // System.out.println("1");
        //
        // }
        // });
        //
        // Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) ->
        // {
        // System.out.println(e.toString());
        // });
        System.out.println("ok");
    }

}
