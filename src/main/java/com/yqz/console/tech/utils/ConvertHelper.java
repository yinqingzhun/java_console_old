package com.yqz.console.tech.utils;

import java.util.function.Function;

public class ConvertHelper {

    public static <T, R> R convert(Function<T, R> function, T source, R defaultValue) {
        try {
            return function.apply(source);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> T defaultValue(T source, T defaultValue) {
        return source == null ? defaultValue : source;
    }
}