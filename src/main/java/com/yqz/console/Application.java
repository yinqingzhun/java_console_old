package com.yqz.console;

import com.yqz.console.model.Feed;
import com.yqz.console.util.ByteArrayHelper;
import com.yqz.console.util.NetUtils;
import com.yqz.console.utils.SimpleDateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {
    static Logger logger = LoggerFactory.getLogger(Application.class);
    
    
    private static void print(byte... b){
        if(b==null)
            return;
        for (int i = 0; i <b.length ; i++) {
            System.out.print(BigInteger.valueOf(b[i]).toString(16));
        }
        System.out.println();
    }

    private static Map<String, SocketAddress[]> deriveSocketAddress(String mobile, String union, String telcom) {
        int v=42062;
        byte[] bytes=BigInteger.valueOf(v).toByteArray();
        System.out.println(bytes.length);
        print(bytes);
        bytes=ByteArrayHelper.intToBytesBigEndian(v);
        print(bytes);


        byte[] bytes2=new byte[4];
        for (int i = 0; i <bytes.length ; i++) {
            bytes2[3-i]=bytes[i];
        }
        
        System.out.println(ByteArrayHelper.bytesToIntSmallEndian(bytes2,0));
        
        
                
        
        Map<String, SocketAddress[]> localAddressMap = new HashMap<>();
        String[] mobiles = Optional.of(mobile).orElse("").split(",");
        String[] unions = Optional.of(union).orElse("").split(",");
        String[] telcoms = Optional.of(telcom).orElse("").split(",");

        String displayNamePrefix ="Remote NDIS based Internet Sharing Device";
        Map<String, SocketAddress> inetSocketAddresses = NetUtils.getIPV4AddressList(displayNamePrefix + "(\\s*#\\s*\\d+\\s*)?")
                .entrySet().stream().collect(Collectors.toMap(p ->
                        p.getKey().replace(displayNamePrefix, "").trim().replace("#", "").trim(), p -> new InetSocketAddress(p.getValue(), 0)));

        SocketAddress[] addresses = inetSocketAddresses.entrySet().stream().filter(p -> Arrays.asList(mobiles).contains(p.getKey())).map(p -> p.getValue()).toArray(SocketAddress[]::new);
        if (addresses.length > 0) {
            localAddressMap.put("mobile", addresses);
        }
        addresses = inetSocketAddresses.entrySet().stream().filter(p -> Arrays.asList(unions).contains(p.getKey())).map(p -> p.getValue()).toArray(SocketAddress[]::new);
        if (addresses.length > 0) {
            localAddressMap.put("union", addresses);
        }
        addresses = inetSocketAddresses.entrySet().stream().filter(p -> Arrays.asList(telcoms).contains(p.getKey())).map(p -> p.getValue()).toArray(SocketAddress[]::new);
        if (addresses.length > 0) {
            localAddressMap.put("telcom", addresses);
        }
        return localAddressMap;

    }

    public static void main(String[] args) throws Exception {
        SortedMap<Integer,Integer> sortedMap=new TreeMap<>();
        sortedMap.put(1,1);
        sortedMap.put(2,1);
        System.out.println( ",1".split(",").length);
        
        
        //deriveSocketAddress("3,5", "7", "10");

        String s = "this is an apple";
        Matcher matcher = Pattern.compile("a").matcher(s);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }


        List<Feed> feeds = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 1100; i++) {
            Feed feed = new Feed();
            feed.setAuthorid(1);
            feed.setEnabled(true);
            feed.setObjid(i);
            feed.setObjtype(23);
            feed.setName("sadf");
            feed.setPublishtime(SimpleDateHelper.serialize(calendar.getTime(), SimpleDateHelper.DATEFORMAT_FULL));
            feeds.add(feed);
            calendar.add(Calendar.SECOND, 1);
        }
        feeds = feeds.stream().sorted(Comparator.comparing(Feed::getPublishtime).reversed()).limit(1000).collect(Collectors.toList());
        feeds = Stream.of(feeds.get(0)).collect(Collectors.toList());

        feeds.clear();
        Feed feed = new Feed();
        feed.setObjtype(1);
        feed.setObjid(1);
        feeds.add(feed);

        feed = new Feed();
        feed.setObjtype(3);
        feed.setObjid(1);
        feeds.add(feed);

        feed = new Feed();
        feed.setObjtype(2);
        feed.setObjid(2);
        feeds.add(feed);


    }

    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
