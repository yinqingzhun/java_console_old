package com.yqz.console.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yinqingzhun on 2017/6/1.
 */
@Slf4j
public class SimpleDateHelper {

    public final static String DATEFORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String DATEFORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";


    public static Date deserialize(String source, String dateFormat) {

        DateFormat df = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = df.parse(source);
            return date;
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        Preconditions.checkNotNull(date);
        return date;
    }

    public static String serialize(Date date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String str = df.format(date);
        return str;
    }


}
