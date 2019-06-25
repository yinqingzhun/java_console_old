package com.yqz.console.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class JacksonHelper {
    private static Logger logger = LoggerFactory.getLogger(JacksonHelper.class);
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static String serialize(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static String serialize(Object o, boolean prettyPrinter) {
        try {
            if (prettyPrinter)
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, Class<T> clazz) {
        if (s == null || s.length() == 0)
            return null;

        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (Exception e) {
            logger.error("failed to deserialize msg:{} ,error: {}", s, e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, JavaType javaType) {
        if (s == null || s.length() == 0)
            return null;

        try {
            T t = mapper.readValue(s, javaType);
            return t;
        } catch (Exception e) {
            logger.error("failed to deserialize msg:{} ,error: {}", s, e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, TypeReference clazz) {

        if (s == null || s.length() == 0)
            return null;
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (Exception e) {
            logger.error("failed to deserialize msg:{} ,error: {}", s, e.getMessage());
        }
        return null;
    }

    public static JsonNode deserialize(String s) {
        if (s == null || s.length() == 0)
            return null;
        
        try {
            JsonNode t = mapper.readTree(s);
            return t;
        } catch (Exception e) {
            logger.error("failed to deserialize msg:{} ,error: {}", s, e.getMessage());
        }
        return null;
    }

    public static TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }
}
