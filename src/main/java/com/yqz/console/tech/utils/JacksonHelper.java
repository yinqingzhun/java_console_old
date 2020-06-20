package com.yqz.console.tech.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class JacksonHelper {


    private static ObjectMapper mapper = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(Date.class, new CustomDateDeserializer());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new ParameterNamesModule())
                .registerModule(javaTimeModule);


    }

    public static String serialize(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String serialize(Object o, boolean prettyPrinter) {
        try {
            if (prettyPrinter)
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, Class<T> clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserialize(String s, TypeReference<T> clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserialize(String s, JavaType clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            log.error(s+", "+e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            T t = mapper.readValue(s, TypeFactory.defaultInstance().constructParametricType(parametrized,parameterClasses));
            return t;
        } catch (IOException e) {
            log.error(s+", "+e.getMessage());
        }
        return null;
    }

    static class CustomDateDeserializer extends DateDeserializers.DateDeserializer {


        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            Date date = null;

            try {
                date = super.deserialize(p, ctxt);
            } catch (IOException e) {
            }


            if (date == null) {
                date = DateHelper.deserialize(p.getText());
            }

            return date;
        }
    }
}
