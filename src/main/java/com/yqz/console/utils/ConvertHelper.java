package com.yqz.console.utils;

import java.util.function.Function;

public class ConvertHelper {

    public static <T, R> R convert(Function<T, R> function, T source, R defaultValue) {
        try {
            return function.apply(source);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}