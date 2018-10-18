package com.yqz.console.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonHelper {
    final static Gson gson = new Gson();


    public static <T> T deserialize(String s, Class<T> c) {
        return gson.fromJson(s, c);
    }

    public static <T> T deserialize(String s, Type c) {
        return gson.fromJson(s, c);
    }

    public static String serialize(Object o) {
        return gson.toJson(o);
    }
}
