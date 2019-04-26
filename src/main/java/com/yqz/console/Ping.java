package com.yqz.console;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Ping {

    public static boolean ping(String ipAddress) throws Exception {
        int timeOut = 3000;
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
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
        if (pingBySizeLimit(localIp, remoteIp, max)) {
            return max;
        } else if (pingBySizeLimit(localIp, remoteIp, min)) {//探测最小端是否可以
            if (pingBySizeLimit(localIp, remoteIp, (min + max) / 2)) {//中间是否可行
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


    public static boolean pingBySizeLimit(String localIp, String remoteIp, int bufferSize) {
        String[] localIps = new String[]{"127.0.0.1", "localhost", "0.0.0.0", "::1"};
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();

        String pingCommand = "ping " + remoteIp + " -n 1 -w 1000 -f -l " + bufferSize;

        if (Arrays.stream(localIps).anyMatch(p -> p.equalsIgnoreCase(localIp)) == false) {
            pingCommand += " -S " + localIp;
        }


        try {
            log.info(pingCommand);

            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                if (success(line)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }


}