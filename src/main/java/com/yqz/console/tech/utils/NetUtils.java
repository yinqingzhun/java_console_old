package com.yqz.console.tech.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.stream.Stream;

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

    public static Map<String, InetAddress> getIPV4AddressListByGateway(String... gateway) {

        if (gateway == null || gateway.length == 0)
            return Collections.emptyMap();

        Map<String, InetAddress> inetAddressMap = new HashMap<>();

        List<InetAddress> list = getIPV4AddressList();
        Stream.of(gateway).forEach(p -> {
            for (InetAddress address : list) {
                if (address.getHostAddress().substring(0, address.getHostAddress().lastIndexOf(".")).equalsIgnoreCase(p.substring(0, p.lastIndexOf(".")))) {
                    inetAddressMap.putIfAbsent(p, address);
                    break;
                }
            }
            inetAddressMap.putIfAbsent(p, null);
        });
        return inetAddressMap;
    }

}
