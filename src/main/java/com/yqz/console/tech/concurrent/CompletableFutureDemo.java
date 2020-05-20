package com.yqz.console.tech.concurrent;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {

    public static void main(String[] args) {
        CompletableFuture.completedFuture(1).thenAccept((s) ->
                System.out.println(s));

        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).thenComposeAsync((s) -> {
            return CompletableFuture.supplyAsync(() -> {
                return s + ",s2";
            });
        }).join();
        System.out.println(result);

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 10;
//        });
//        CompletableFuture<String> f = future.thenCombine(CompletableFuture.supplyAsync(() -> {
//            return 20;
//        }),(x,y) -> {return "计算结果："+x+y;});
//        System.out.println(f.get());



    }
}
