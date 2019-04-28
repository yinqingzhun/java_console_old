package com.yqz.console.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringHelper {
    public static <T> String join(T[] array, String delimiter) {
        return Stream.of(array).map(p -> p.toString()).collect(Collectors.joining(delimiter));
    }
}
