package com.yqz.console;


import com.google.common.base.Preconditions;
import com.yqz.console.model.FeedBase;
import com.yqz.console.util.DateHelper;
import com.yqz.console.util.HashHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Application {
    static Logger logger = LoggerFactory.getLogger(Application.class);
    static Integer[] GAME_TASK_LIST = {1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7};
    static String[] words = {"a", "b", "c", "d", "e", "f", "g"};

    private static String encodeParameters(Map<String, Object> dict, String appkey) {
        SortedMap<String, Object> sortedMap = new TreeMap<>(dict);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appkey);
        for (String key : sortedMap.keySet()) {
            if (key != "_sign")
                stringBuilder.append(key).append(sortedMap.get(key));
        }
        stringBuilder.append(appkey);


        return HashHelper.md5(stringBuilder.toString());


    }

    private static <T> T[] shuffle(T[] array) {
        T[] copy = Arrays.copyOf(array, array.length);
        Random random = new Random();
        int i = copy.length;
        while (--i > 0) {
            int j = random.nextInt(i);
            T temp = copy[i];
            copy[i] = copy[j];
            copy[j] = temp;
        }
        return copy;
    }

    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        // 这里参考java8 hashmap的算法，使推算的过程固定
        int n = ticksPerWheel - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        // 这里1073741824 = 2^30,防止溢出
        return (n < 0) ? 1 : (n >= 1073741824) ? 1073741824 : n + 1;
    }

    public static String toString(int[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]).append(",");
        }
        return stringBuilder.toString();
    }

    private static String getFeedKey(FeedBase feed) {
        String key = feed.getObjid() + "," + feed.getObjtype();
        return key;
    }

    private static List<FeedBase> mergeFeedList(List<FeedBase> feedList, FeedBase... feed) {
        Set<String> set = new HashSet<>();
        final List<FeedBase> allList = new ArrayList<>();
        if (feedList != null) {
            feedList.forEach(p -> {
                String key = getFeedKey(p);
                if (!set.contains(key)) {
                    set.add(key);
                    allList.add(p);
                }
            });
        }

        if (feed != null && feed.length > 0) {
            Arrays.asList(feed).forEach(p -> {
                String key = getFeedKey(p);
                if (!set.contains(key)) {
                    set.add(key);
                    allList.add(p);
                }
            });

        }


        List<FeedBase> resultList = allList.stream()
                .sorted(Comparator.comparing(FeedBase::getPublishtime).reversed())
                .limit(1000).collect(Collectors.toList());

        return resultList;
    }



    public static void main(String[] args) throws Exception {
        //List<FeedBase> feedBaseList = new LinkedList<>();
        //feedBaseList.add(new FeedBase(1, 1, "2000-01-01 12:32:33"));
        //feedBaseList.add(new FeedBase(2, 2, "2002-01-01 12:32:33"));
        //feedBaseList.add(new FeedBase(1, 1, "2001-01-01 12:32:33"));
        //feedBaseList = mergeFeedList(feedBaseList, new FeedBase(1, 1, "2000-01-01 14:32:33"), new FeedBase(3, 3, "2003-01-01 12:32:33"));
        //
        //feedBaseList.forEach(p -> System.out.println(JsonHelper.serialize(p)));
        

        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(10);
        System.out.println(bCryptPasswordEncoder.encode("admin"));

      /*  Enumeration<Integer> enumeration = Collections.enumeration(Arrays.asList(1, 3, 5, 7));
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }*/


        //long n = Integer.MAX_VALUE + 1;
        //byte[] bytes = ByteArrayHelper.valueToBytes(ByteOrder.BIG_ENDIAN, n, 4);
        //System.out.println(ByteArrayHelper.bytesToValue(ByteOrder.BIG_ENDIAN, bytes, 0, 4));
        //
        //System.out.println(Arrays.copyOfRange(new byte[6], 6, 6).length);

        //System.out.println(Ping.getMtu("127.0.0.1", "212.64.81.230"));
         
       /* System.out.println(normalizeTicksPerWheel(13));
     
        HashedWheelTimer wheelTimer = new HashedWheelTimer();
        wheelTimer.newTimeout(new io.netty.util.TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println(Thread.currentThread().getId() + " is out in  "+System.nanoTime()/1000000);
            }
        }, 3, TimeUnit.SECONDS);

        wheelTimer.newTimeout(new io.netty.util.TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println(Thread.currentThread().getId() + " is out in  "+System.nanoTime()/1000000);
            }
        }, 1, TimeUnit.SECONDS);

        wheelTimer.newTimeout(new io.netty.util.TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println(Thread.currentThread().getId() + " is out in  "+System.nanoTime()/1000000);
            }
        }, 10, TimeUnit.MILLISECONDS);

        Thread.sleep(5000);
        wheelTimer.stop();*/


        // int[] ints=  generateRedPacket(327, 1 * 100, 10 * 100, 3);
        //System.out.println( BigDecimal.valueOf(0).compareTo(BigDecimal.ZERO)<=0);
        //
        //System.out.println(getUserTaskIdListTodayKey(1000,LocalDate.now()));
        //int[] ints = generateRedPacket(5000 * 100, 50, 2000, 2000);

        //System.out.println(BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(879)) );
    }

    private static String getUserTaskIdListTodayKey(int userId, LocalDate localDate) {
        String userTaskIdListTodayKey = String.format("follow_userTaskIdListKey_%s_%s", userId
                , DateHelper.serialize(DateHelper.localDateToDate(localDate), DateHelper.DATEFORMAT_ONLY_DATE));
        return userTaskIdListTodayKey;
    }

    public static int[] generateRedPacket(int moneyTotal, int moneyMin, int moneyMax, int splitNumber) {
        Preconditions.checkState(moneyMax > 0);
        Preconditions.checkState(moneyMin > 0);
        Preconditions.checkState(moneyTotal > 0);
        Preconditions.checkState(splitNumber > 0);

        //红包数
        int number = splitNumber;
        //红包总额
        int totalMoney = moneyTotal;
        //最小红包
        int minMoney = moneyMin;
        //最小红包
        int maxMoney = moneyMax;

        int agv = totalMoney / number;

        if (minMoney > agv) {
            minMoney = agv;
        }

        if (maxMoney * number < totalMoney) {
            maxMoney = totalMoney / number + 1;
        }

        if (maxMoney < agv) {
            maxMoney = agv + 1;
        }


        int[] ps = new int[number];

        for (int i = 0; i < number; i++) {
            ps[i] = minMoney;
        }


        Random random = new Random();
        int left = (totalMoney - minMoney * number);
        while (left > 0) {
            int dis = Math.min(left, random.nextInt((maxMoney - minMoney) / 10));
            int randomIndex = random.nextInt(number);
            if (ps[randomIndex] + dis <= maxMoney) {
                ps[randomIndex] += dis;
                left -= dis;
            }
        }

        System.out.println(Arrays.stream(ps).sum() + ": " + Arrays.stream(ps).sorted().mapToObj(p -> String.valueOf(p)).collect(Collectors.joining(",")));
        Preconditions.checkState(Arrays.stream(ps).allMatch(p -> p > 0));
        return ps;

    }


    public static int[] generateRedPacketWrong(int moneyTotal, int moneyMin, int moneyMax, int splitNumber) {

        Preconditions.checkState(moneyMax > 0);
        Preconditions.checkState(moneyMin > 0);
        Preconditions.checkState(moneyTotal > 0);
        Preconditions.checkState(splitNumber > 0);

        //红包数
        int number = splitNumber;
        //红包总额
        int totalMoney = moneyTotal;
        //最小红包
        int minMoney = moneyMin;
        //最小红包
        int maxMoney = moneyMax;

        int agv = totalMoney / number;

        if (minMoney > agv) {
            minMoney = agv;
        }

        if (minMoney * (number - 1) + maxMoney < totalMoney) {
            maxMoney = totalMoney - minMoney * (number - 1);
        }

        if (maxMoney < agv) {
            maxMoney = agv;
        }


        int[] ps = new int[number];

        for (int i = 0; i < number; i++) {
            ps[i] = minMoney;
        }

        Random random = new Random();
        int left = (totalMoney - minMoney * number);
        int gap = Math.min(left, maxMoney - minMoney);
        int[] splits = new int[number];
        for (int i = 0; i < number; i++) {
            if (i < number - 1) {
                splits[i] = random.nextInt(gap);
            } else {
                splits[i] = 0;
            }
        }
        Arrays.sort(splits);
        int splitSum = Arrays.stream(splits).sum();
        if (splitSum > left) {
            int more = splitSum - left;
            for (int i = number - 1; i >= 0; i--) {
                if (splits[i] - gap > more) {
                    more = more - (splits[i] - gap);
                    splits[i] = gap;
                } else {
                    splits[i] -= more;
                    break;
                }

            }
        } else {
            int less = left - splitSum;
            for (int i = 0; i < number; i++) {
                if (splits[i] < gap) {
                    if (gap - splits[i] > less) {
                        splits[i] += less;
                        break;
                    } else {
                        less = less - (gap - splits[i]);
                        splits[i] = gap;
                    }

                }
            }
        }


        for (int i = 0; i < number; i++) {
            long src = ps[i];
            ps[i] = ps[i] + splits[i];

        }

        System.out.println(Arrays.stream(ps).sum() + ": " + Arrays.stream(ps).mapToObj(p -> String.valueOf(p)).collect(Collectors.joining(",")));
        Preconditions.checkState(Arrays.stream(ps).allMatch(p -> p > 0));
        return ps;

    }

    public static void generateRedPacket2() {
        //红包数
        int number = 100;
        //红包总额
        int totalMoney = 8000;
        //最小红包
        int minMoney = 70;
        //最小红包
        int maxMoney = 80;
        int agv = totalMoney / number;

        if (minMoney > agv)
            minMoney = agv;

        if (minMoney * (number - 1) + maxMoney > totalMoney) {
            maxMoney = totalMoney - minMoney * (number - 1);
        }

        long[] ps = new long[number];

        for (int i = 0; i < number; i++) {
            ps[i] = minMoney;
        }


        Random random = new Random();
        int left = (totalMoney - minMoney * number) * 100;
        int[] splits = new int[number];
        for (int i = 0; i < number; i++) {
            if (i < number - 1) {
                splits[i] = random.nextInt(left);
            } else {
                splits[i] = left;
            }
        }
        Arrays.sort(splits);

        for (int i = 0; i < number; i++) {
            int step = splits[i] - (i > 0 ? splits[i - 1] : 0);
            long src = ps[i];
            int inc = step;
            ps[i] = ps[i] * 100 + inc;
            //System.out.printf("No.%s from %s to %s, step= %s, split=%s\r\n", i, src, ps[i], inc,splits[i]);
        }
        System.out.println(Arrays.stream(ps).boxed().map(p -> BigDecimal.valueOf(p / 100).toString()).collect(Collectors.joining(",")));
        System.out.println(Arrays.stream(ps).sum() * 0.01);


    }

    public static void generateRedPacket() {
        //红包数
        int number = 100;
        //红包总额
        int totalMoney = 7800 * 100;
        //最小红包
        int minMoney = 70 * 100;
        //最小红包
        int maxMoney = 80 * 100;
        int agv = totalMoney / number;

        if (minMoney > agv)
            minMoney = agv;

        if (minMoney * (number - 1) + maxMoney > totalMoney) {
            maxMoney = totalMoney - minMoney * (number - 1);
        }

        int[] ps = new int[number];

        for (int i = 0; i < number; i++) {
            ps[i] = minMoney;
        }

        int gap = maxMoney - minMoney;

        Random random = new Random();
        int left = (totalMoney - minMoney * number);
        int[] splits = new int[number];
        for (int i = 0; i < number; i++) {
            if (i < number - 1) {
                splits[i] = random.nextInt(gap);
            } else {
                splits[i] = 0;
            }
        }
        Arrays.sort(splits);
        int splitSum = Arrays.stream(splits).sum();
        if (splitSum > left) {
            int more = splitSum - left;
            for (int i = number - 1; i >= 0; i--) {
                if (splits[i] - gap > more) {
                    more = more - (splits[i] - gap);
                    splits[i] = gap;
                } else {
                    splits[i] -= more;
                    break;
                }

            }
        } else {
            int less = left - splitSum;
            for (int i = 0; i < number; i++) {
                if (splits[i] < gap) {
                    if (gap - splits[i] > less) {
                        splits[i] += less;
                        break;
                    } else {
                        less = less - (gap - splits[i]);
                        splits[i] = gap;
                    }

                }
            }
        }


        for (int i = 0; i < number; i++) {
            long src = ps[i];
            ps[i] = ps[i] + splits[i];
            System.out.printf("No.%s from %s to %s, step=%s\r\n", i, src, ps[i], splits[i]);
        }
        System.out.println(Arrays.stream(ps).boxed().map(p -> BigDecimal.valueOf(p).toString()).collect(Collectors.joining(",")));
        System.out.println(Arrays.stream(ps).sum());


    }

    public static void grp() {
        //红包数
        int number = 10;
        //红包总额
        float total = 1000;
        float money;
        //最小红包
        double min = 10;
        double max = 150;
        int i = 1;
        List math = new ArrayList();
        DecimalFormat df = new DecimalFormat("###.##");
        while (i < number) {
            //保证即使一个红包是最大的了,后面剩下的红包,每个红包也不会小于最小值
            max = total - min * (number - i);
            int k = (int) (number - i) / 2;
            //保证最后两个人拿的红包不超出剩余红包
            if (number - i <= 2) {
                k = number - i;
            }
            //最大的红包限定的平均线上下
            max = max / k;
            //保证每个红包大于最小值,又不会大于最大值
            money = (int) (min * 100 + Math.random() * (max * 100 - min * 100 + 1));
            money = (float) money / 100;
            //保留两位小数
            money = Float.parseFloat(df.format(money));
            total = (int) (total * 100 - money * 100);
            total = total / 100;
            math.add(money);
            System.out.println("第" + i + "个人拿到" + money + "剩下" + total);
            i++;
            //最后一个人拿走剩下的红包
            if (i == number) {
                math.add(total);
                System.out.println("第" + i + "个人拿到" + total + "剩下0");
            }
        }
        //取数组中最大的一个值的索引
        System.out.println("本轮发红包中第" + (math.indexOf(Collections.max(math)) + 1) + "个人手气最佳");
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

    static class ShortSequencer {

        private int current = 0;

        public ShortSequencer(int init) {
        }

        public int next() {
            synchronized (ShortSequencer.class) {
                current = current + 1 > 10 ? 1 : current + 1;
                System.out.println(current);
                return current;
            }


        }
    }

    static class TimeSequencer {
        private int lastTimestamp = 0;
        private int lastSequence = 0;

        public synchronized int[] next() {
            int time = (int) (System.currentTimeMillis() / 1000);
            if (time == lastTimestamp) {
                lastSequence++;
            } else {
                lastTimestamp = time;
                lastSequence = 1;
            }
            //System.out.println("next:"+lastTimestamp+"-"+lastSequence);
            return new int[]{lastTimestamp, lastSequence};
        }


    }
}
