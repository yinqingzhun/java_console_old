package com.yqz.console.util;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yinqingzhun on 2017/6/1.
 */
@Slf4j
public class DateHelper {

    public final static String DATEFORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String DATEFORMAT_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public final static String DATEFORMAT_DATE = "yyyy-MM-dd";

    public static void main(String[] args) {

        String str = "2016-08-09T10:01:54.123Z";
        Preconditions.checkState(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-09 18:01:54.123"));

        str = "2016-08-09T10:01:54.123+8:00";
        Preconditions.checkState(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-09 10:01:54.123"));

        str = "2016-08-09T10:01:54.123+08:00";
        Preconditions.checkState(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-09 10:01:54.123"));

        str = "2016-08-09T10:01:54.123-8:00";
        Preconditions.checkState(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-10 02:01:54.123"));

        str = "2016-08-09T10:01:54.123-08:00";
        Preconditions.checkState(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-10 02:01:54.123"));

        str = "2016-08-09 10:01:54.123";
        Preconditions.checkState(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-09 10:01:54.123"));

        str = "2016-08-09T10:01:54.123";
        System.out.println(serialize(deserialize(str), DATEFORMAT_FULL).equals("2016-08-09 10:01:54.123"));

        str = "2016-08-09";
        System.out.println(serialize(deserialize(str), DATEFORMAT_DATE).equals("2016-08-09"));
    }

    public static Date deserialize(String source, String... dateFormat) {
        Date date = null;
        if (dateFormat != null && dateFormat.length > 0) {
            for (String df : dateFormat) {
                try {
                    date = new SimpleDateFormat(df).parse(source);
                    break;
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }

        } else {
            try {
                date = parse(source);
            } catch (ParseException e) {
                log.debug(e.getMessage());
                //  /Date(1528805593000)/
                Matcher matcher = Pattern.compile("\\/Date\\((-?\\d+)\\)\\/").matcher(source);
                if (matcher.matches()) {
                    date = new Date(Long.valueOf(matcher.group(1)));
                }
            }
        }
        Preconditions.checkNotNull(date, String.format("date %s can't be resolved", source));
        return date;

    }

    public static String serialize(Date date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String str = df.format(date);
        return str;
    }

    public static String serialize(Date date) {
        DateFormat df = new SimpleDateFormat(DateHelper.DATEFORMAT_FULL);
        String str = df.format(date);
        return str;
    }

    public static String getCurrentDateTime() {
        return serialize(new Date(), DATEFORMAT_STANDARD);
    }

    /**
     * 获取指定的Date
     *
     * @param timeUnit 时间单位
     * @param account  添加到calendarTimeField的时间或日期数量
     * @return
     */
    public static Date getDate(TimeUnit timeUnit, int account) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit.getValue(), account);
        return calendar.getTime();
    }

    /**
     * 获取指定的timestamp
     *
     * @param timeUnit 时间单位
     * @param account  添加到calendarTimeField的时间或日期数量
     * @return
     */
    public static long getTimeInMillis(TimeUnit timeUnit, int account) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit.getValue(), account);
        return calendar.getTimeInMillis();
    }

    public enum TimeUnit {
        YEAR(Calendar.YEAR),
        MONTH(Calendar.MONTH),
        DAY(Calendar.DAY_OF_YEAR),
        HOUR(Calendar.HOUR_OF_DAY),
        MINUTE(Calendar.MINUTE),
        SECOND(Calendar.SECOND),
        MILLISECOND(Calendar.MILLISECOND);

        private int v;

        TimeUnit(int value) {
            this.v = value;
        }

        int getValue() {
            return this.v;
        }

    }
    /**
     * 返回代表当前时间格式化的字符串
     *
     * @return
     */
    public static String getNowString(String dateFormat) {
        return serialize(getNow(), dateFormat);
    }

    /**
     * 返回一个代表当前时间的Date对象
     *
     * @return
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }
    /**
     * 函数功能描述:UTC时间转本地时间格式
     *
     * @param datetime 日期字符串
     * @return 本地日期
     */
    public static Date parse(String datetime) throws ParseException {
        boolean isUTC = false;
        String utcTimePattern = "yyyy-MM-dd";
        String subTime = datetime.substring(10);//UTC时间格式以 yyyy-MM-dd 开头,将utc时间的前10位截取掉,之后是含有多时区时间格式信息的数据

        //处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
        if (subTime.indexOf("+") != -1) {
            subTime = changeUtcSuffix(subTime, "+");
        } else if (subTime.indexOf("-") != -1) {
            subTime = changeUtcSuffix(subTime, "-");
        }
        datetime = datetime.substring(0, 10) + subTime;

        //依据传入函数的utc时间,得到对应的utc时间格式
        //步骤一:处理 T
        if (datetime.indexOf("T") != -1) {
            utcTimePattern = utcTimePattern + "'T'";
        }

        //步骤二:处理毫秒SSS
        if (StringUtils.hasText(subTime)) {
            if (datetime.indexOf(".") != -1) {
                utcTimePattern = utcTimePattern + "HH:mm:ss.SSS";
            } else {
                utcTimePattern = utcTimePattern + "HH:mm:ss";
            }
        }

        //步骤三:处理时区问题
        if (subTime.indexOf("+") != -1 || subTime.indexOf("-") != -1) {
            utcTimePattern = utcTimePattern + "XXX";
            isUTC = true;
        } else if (subTime.indexOf("Z") != -1) {
            utcTimePattern = utcTimePattern + "'Z'";
            isUTC = true;
        }


        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePattern);
        if (isUTC)
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = utcFormater.parse(datetime);
        return date;

    }

    /**
     * 函数功能描述:修改时间格式后缀
     * 函数使用场景:处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
     *
     * @param subTime
     * @param sign
     * @return
     */
    private static String changeUtcSuffix(String subTime, String sign) {
        String timeSuffix = null;
        String[] splitTimeArrayOne = subTime.split("[" + sign + "]");
        String[] splitTimeArrayTwo = splitTimeArrayOne[1].split(":");
        if (splitTimeArrayTwo[0].length() < 2) {
            timeSuffix = sign + "0" + splitTimeArrayTwo[0] + ":" + splitTimeArrayTwo[1];
            subTime = splitTimeArrayOne[0] + timeSuffix;
            return subTime;
        }
        return subTime;
    }
}
