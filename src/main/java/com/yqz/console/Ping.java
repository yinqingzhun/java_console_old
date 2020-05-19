package com.yqz.console;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Ping {

    public static void main(String[] args) {
        Stopwatch stopwatch=Stopwatch.createStarted();
        Arrays.stream(getLostRateAndAvgRtt("192.168.21.121", "140.143.248.243")).forEach(System.out::println);
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public static boolean ping(String ipAddress) {
        int timeOut = 3000;
        boolean status = false;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static boolean success(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    private static int getMtu(String localIp, String remoteIp, int min, int max) {
        //探测最大端是否可以
        if (pingWithBufferSizeLimit(localIp, remoteIp, max)) {
            return max;
        } else if (pingWithBufferSizeLimit(localIp, remoteIp, min)) {//探测最小端是否可以
            if (pingWithBufferSizeLimit(localIp, remoteIp, (min + max) / 2)) {//中间是否可行
                //后半段
                return getMtu(localIp, remoteIp, (min + max) / 2, max - 1);
            } else {
                //前半段
                return getMtu(localIp, remoteIp, min, (min + max) / 2 - 1);
            }

        }
        return -1;
    }

    public static int getMtu(String localIp, String remoteIp) {
        return getMtu(localIp, remoteIp, 1400, 1472);
    }


    public static boolean pingWithBufferSizeLimit(String localIp, String remoteIp, int bufferSize) {
        String[] localIps = new String[]{"127.0.0.1", "localhost", "0.0.0.0", "::1"};

        String pingCommand = "ping " + remoteIp + " -n 1 -w 1000 -f -l " + bufferSize;

        if (Arrays.stream(localIps).anyMatch(p -> p.equalsIgnoreCase(localIp)) == false) {
            pingCommand += " -S " + localIp;
        }

        String s = execPing(pingCommand);
        if (s == null || s.isEmpty())
            return false;
        String[] ss = s.split(System.lineSeparator());
        for (String line : ss) {
            if (success(line))
                return true;
        }
        return false;

    }


    public static String execPing(String pingCommand) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();

        try {
            log.info(pingCommand);

            Process p = r.exec(pingCommand);
            if (p == null) {
                return null;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));

            String line;

            StringBuilder stringBuilder = new StringBuilder();

            while ((line = in.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }


            return stringBuilder.toString();

        } catch (Exception e) {
            log.error(e.getMessage());

        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        return null;
    }


    public static int[] getLostRateAndAvgRtt(String localIp, String remoteIp) {
        String pingCommand = "ping " + remoteIp + " -n 3  " + " -S " + localIp;
        String s = execPing(pingCommand);
        System.out.println(s);
        if (s == null || s.isEmpty())
            return new int[0];

        int[] results = new int[2];
        String[] ss = s.split(System.lineSeparator());
        Pattern patternLostRate = Pattern.compile("(\\d+)% 丢失", Pattern.CASE_INSENSITIVE);
        Pattern patternRtt = Pattern.compile("平均 = (\\d+)ms", Pattern.CASE_INSENSITIVE);
        for (String line : ss) {
            //丢包率

            Matcher matcherLostRate = patternLostRate.matcher(line);
            while (matcherLostRate.find()) {
                results[0] =Integer.valueOf(matcherLostRate.group(1));
            }
            //average rtt

            Matcher matcherRtt = patternRtt.matcher(line);
            while (matcherRtt.find()) {
                results[1] = Integer.valueOf(matcherRtt.group(1));
            }

        }
        return results;

    }


}