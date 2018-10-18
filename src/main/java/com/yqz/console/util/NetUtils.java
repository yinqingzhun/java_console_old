package com.yqz.console.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by JIAOBAILONG on 2018/9/27 17:45 Autohome Co.,Ltd.
 */
public class NetUtils {

    public static List<InetAddress> getIPV4AddressList() {
        List<InetAddress> inetAddressList = new ArrayList<>();
        Enumeration allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();

                if (netInterface.isLoopback() || netInterface.isPointToPoint() || netInterface.isVirtual() || !netInterface.isUp())
                    continue;

                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        inetAddressList.add(ip);
                    }
                }
            }

        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }

        return inetAddressList;
    }
    
    public static Map<String, InetAddress> getIPV4AddressList(String displayNameRegexp){
        Map<String,InetAddress> inetAddressMap = new HashMap<>();
        Enumeration allNetInterfaces = null;

     Pattern pattern= Pattern.compile(displayNameRegexp);
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();

                if (netInterface.isLoopback() || netInterface.isPointToPoint() || netInterface.isVirtual() || !netInterface.isUp())
                    continue;

                
                if(!pattern.matcher(netInterface.getDisplayName()).matches())
                    continue;

                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        inetAddressMap.put(netInterface.getDisplayName(),ip);
                    }
                }
            }

        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }

        return inetAddressMap;
    }
}
