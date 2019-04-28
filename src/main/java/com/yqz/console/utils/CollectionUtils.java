package com.yqz.console.utils;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
    /**
     * 获取left和right两个集合的交集
     *
     * @param left
     * @param right
     * @param <T>
     * @return
     */
    public static <T> Collection<T> intersection(Collection<T> left, Collection<T> right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);

        Set<T> setIntersection = new HashSet<T>();
        Iterator<T> iterA = left.iterator();

        while (iterA.hasNext()) {
            T s = iterA.next();
            if (right.contains(s)) {
                setIntersection.add(s);
            }
        }
        return setIntersection;
    }

    /**
     * 获取left和right两个集合的差集，即left中包含但right中不包含的元素集合
     *
     * @param left
     * @param right
     * @param <T>
     * @return
     */
    public static <T> Collection<T> difference(Collection<T> left, Collection<T> right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        Set<T> setDifference = new HashSet<T>();

        Iterator<T> iterA = left.iterator();
        while (iterA.hasNext()) {
            T s = iterA.next();
            if (!right.contains(s)) {
                setDifference.add(s);
            }
        }
        return setDifference;
    }

    public static <T> Collection<T> distinct(Collection<T> collection, Function<? super T, Object> keyExtractor) {
        Preconditions.checkNotNull(collection);
        return collection.stream().filter(distinctByKey(keyExtractor)).distinct().collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
