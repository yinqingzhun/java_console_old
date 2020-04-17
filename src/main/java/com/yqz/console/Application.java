package com.yqz.console;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.yqz.console.http.HttpHelper;
import com.yqz.console.model.FeedBase;
import com.yqz.console.utils.DateHelper;
import com.yqz.console.utils.HashHelper;
import com.yqz.console.utils.JsonHelper;
import com.yqz.console.utils.RequestSignHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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

    public static String mostCommonWord() {
        String paragraph = "Bob hit a ball, the hit BALL flew far after it was hit.";
        String[] banned = new String[]{"hit"};

        paragraph = paragraph.replaceAll("[^\\w\\s]", "");
        String[] ss = paragraph.split("\\s");


        Map<String, Integer> map = new HashMap();


        for (String s : ss) {
            boolean h = false;
            for (String b : banned) {
                if (s.toLowerCase().equals(b) || s.trim().equals("")) {
                    h = true;
                    break;
                }
            }

            if (h)
                continue;

            Integer count = map.get(s.toLowerCase());
            map.put(s.toLowerCase(), count == null ? 1 : count + 1);
        }

        Integer value = 0;
        String key = "";

        for (String k : map.keySet()) {
            Integer v = map.get(k);
            if (v > value) {
                value = v;
                key = k;
            }


        }

        return key;


    }

    private static void product(int input) {
        List<Integer> list = new LinkedList<>();
        int i = 2;
        int a = input;
        while (i <= a) {
            if (a % i == 0) {
                list.add(i);
                a /= i;
                i = 2;
            } else {
                i++;
            }
        }
        System.out.println(input + "=" + list.stream().map(p -> p.toString()).collect(Collectors.joining("*")));

    }

    private static void prime(int input) {
        List<Integer> list = new LinkedList<>();

        for (int i = 2; i <= input; i++) {
            int j = 2;
            for (; j < i; j++) {
                if (i % j == 0)
                    break;
            }

            if (j == i)
                list.add(i);
        }

        System.out.println(list.stream().map(p -> p.toString()).collect(Collectors.joining(",")));
    }

    public static String sign(long uid, String deviceId, long tsInSeconds) {
        //签名 ，规则：md5( 用户id + 设备号 + Unix时间戳 + 秘钥 )

        long lag = System.currentTimeMillis() - tsInSeconds * 1000;
        if (Math.abs(lag) > 30000) {
            return "";
        }

        String src = uid + deviceId + tsInSeconds + "692c114598d64c29b004f7e59ce4c4a9";
        return DigestUtils.md5DigestAsHex(src.getBytes(Charset.forName("utf-8")));
    }


    public static void run() throws Exception {
//        ConcurrentLinkedQueue<Integer> queue=new ConcurrentLinkedQueue<>();
//        queue.offer(1);
//        queue.offer(0);
//        queue.remove(1);
//        queue.offer(2);





       /* ConcurrentSkipListSet<Integer> blackUserList = new ConcurrentSkipListSet();
        Thread t1=new Thread(()->{
            int i = 0;
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                blackUserList.add(i++);
            }

        });
        t1.start();

        for (int i = 0; i <10 ; i++) {
            Thread t2=new Thread(()->{
                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int index = Math.max(blackUserList.size() - 10, 0);
                    int limit = 10;
                    System.out.println(blackUserList.stream().skip(index).limit(limit).map(p -> p.toString()).collect(Collectors.joining(",")));
                }

            });
            t2.start();
        }

*/


        //System.out.println( String.format("%,d",10000234));

        //run2();
//        addBlacklist();
//        addToken(79159749, "644954bc82e7471783e81c2b8a9320a504b7e1c5");
    }

    public static void addToken(int uid, String token) {

        String salt = "c90459faaad911e9834080e82c9531bf";

        String md5 = DigestUtils.md5DigestAsHex((uid + token + salt).getBytes(Charset.forName("utf-8"))).replace("-", "");


        String url = String.format("http://localhost/add_token?uid=%s&token=%s&_s=%s&ttl=1000000", uid, token, md5);
        System.out.println(url);
//        HttpHelper.Tuple2<Integer, String> result = HttpHelper.httpGet(url);
//        System.out.println(String.format("{%s}:ut:%s", uid, token) + "---" + result.getT2());

    }


    public static void addBlacklist() {
        String uids = "29652265,79159749";

        String guid = UUID.randomUUID().toString();
        long ts = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        sb.append(uids);
        sb.append(guid);
        sb.append(ts);
        sb.append("1f7cca96a30611e9afff80e82c9531bf");
        String md5 = DigestUtils.md5DigestAsHex(sb.toString().getBytes(Charset.forName("utf-8"))).replace("-", "");


        String url = String.format("http://localhost:8087/api/blacklist/add.do?uid=%s&guid=%s&_t=%s&_s=%s", uids, guid, ts, md5);
        System.out.println(url);
        HttpHelper.Tuple2<Integer, String> result = HttpHelper.httpGet(url);
        System.out.println(result.getT2());

    }

    public static void run2() throws Exception {
        ConcurrentLinkedQueue<Integer> extraAwardQueue = new ConcurrentLinkedQueue<>();
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
        String s = "/luckydraw?_c=110100&_m=b11498d2a71f224af7d4a41d4f6656462a0213a7&_tk=fa8b1446caa9447fb8c36e8de33d68b90217bb12&_g=ds90sadf89";
        ConcurrentHashMap<Integer, String> urlMap = new ConcurrentHashMap<>();
        Stopwatch stopwatch = Stopwatch.createStarted();
        int count = 1;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(count);
        Random random = new Random();
        String[] hosts = new String[]{"http://localhost:80", "http://localhost:80"};
        for (int i = 0; i < count; ++i) // create and start threads
        {
            extraAwardQueue.add(i);
            final int n = i;
            new Thread(() -> {
                try {
                    startSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int userCount = 2;
                for (int j = 0; j < userCount; j++) {
                    try {
                        Thread.sleep(2600);
                    } catch (InterruptedException e) {
                    }
                    int uid = (4289227);
                    long ts = System.currentTimeMillis() / 1000;
                    String sign = sign(uid, "b11498d2a71f224af7d4a41d4f6656462a0213a7", ts);

                    String url = hosts[(random.nextInt(uid)) % 2] + s + "&_u=" + uid + "&_d=" + urlMap.getOrDefault(uid, "")
                            + "&_t=" + ts + "&_s=" + sign;
//                    log.info(url);
                    HttpHelper.Tuple2<Integer, String> result = HttpHelper.httpGet(url);
                    System.out.println(DateHelper.getNowString(DateHelper.DATEFORMAT_FULL) + "/t" + result.getT2());
                    if (result.getT1() == 200) {
                        JsonNode jsonNode = JsonHelper.deserialize(result.getT2(), JsonNode.class);
                        if (jsonNode.at("/c").asText().equals("0")) {

                            ConcurrentLinkedQueue<String> queue = map.compute(uid, (k, v) ->
                                    v == null ? new ConcurrentLinkedQueue() : v
                            );
                            String d = jsonNode.at("/o/d").asText();
                            queue.add(d);
                            urlMap.put(uid, d);


                        }
                    }
                   /* try {
                        Thread.sleep(random.nextInt(1000) + 2900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }

                doneSignal.countDown();
            }).start();

        }


        // don't let run yet
        startSignal.countDown();      // let all threads proceed

        doneSignal.await();           // wait for all to finish
        stopwatch.stop();
        Map<Integer, Set<String>> ids = new ConcurrentHashMap<>();

        map.forEach((k, v) -> {
            Set<String> set = new HashSet<>();

            v.stream().forEach(p -> {
                String[] ary = p.split(",");
                if (!StringUtils.isEmpty(ary[ary.length - 2]))
                    set.addAll(Arrays.asList(ary[ary.length - 2].split("[|]")).stream().filter(m -> StringUtils.hasText(m)).collect(Collectors.toList()));
//                    System.out.println(ary[ary.length - 2] + "--->" + p);
            });
            if (set.size() > 3) {
                System.out.println(k + "[" + set.size() + "]:" + Joiner.on(",").join(set));
                System.out.println();
            }
            ids.put(k, set);
        });
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));


        System.out.printf("count: %s/%s, list: %s"
                , ids.values().stream().flatMap(v -> v.stream()).distinct().count()
                , ids.values().stream().flatMap(v -> v.stream()).count()
                , ids.values().stream().flatMap(v -> v.stream()).sorted().collect(Collectors.toList()));


    }

    public static String toBinaryString(Inet4Address inet4Address) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] ipArray = inet4Address.getAddress();
        for (int i = 0; i < ipArray.length; i++) {

            String bs = Integer.toBinaryString(ipArray[i] & 0xff);
            if (bs.length() < 8) {
                stringBuilder.append(String.join("", Collections.nCopies(8 - bs.length(), "0")));
            }
            stringBuilder.append(bs);
            if (i < ipArray.length - 1) {
                stringBuilder.append(".");
            }
        }
        return stringBuilder.toString();
    }


    public static boolean isSameSubnet(Inet4Address ip, Inet4Address subnet) {
        /*Preconditions.checkState(subnetLength > 0);
        Preconditions.checkState(subnetLength < 32);

        byte[] maskArray = new byte[4];
        for (int i = 0; i < subnetLength / 8; i++) {
            maskArray[i] = (byte) 255;
        }

        if (subnetLength % 8 != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.join("", Collections.nCopies(subnetLength % 8, "1")));
            stringBuilder.append(String.join("", Collections.nCopies(8 - subnetLength % 8, "0")));

            Integer mask = Integer.valueOf(stringBuilder.toString(), 2);
            maskArray[subnetLength / 8] = (byte) (mask & 0xff);
        }
*/

        byte[] ipArray = ip.getAddress();
        byte[] subnetArray = subnet.getAddress();


        int end = 4;
        boolean foundEnd = false;
        for (int i = 0; i < end; i++) {
            if (!foundEnd && subnetArray[4 - i - 1] != 0) {
                end = 4 - i;
                foundEnd = true;
            }


            System.out.printf("No.%s" + System.lineSeparator(), i);
            System.out.println(Integer.toBinaryString(ipArray[i] & 0xff) + "---" + Integer.toBinaryString(subnetArray[i] & 0xff));
            System.out.println();

            if (((subnetArray[i] & 0xff) != (ipArray[i] & 0xff))) {
                return false;
            }
        }
        return true;
    }

    public static <T> T[] of(T... ts) {
        return ts;
    }

    public static void test(String appId, String appkey, long ts, Map<String, Object> fields) throws NoSuchAlgorithmException {

        SortedMap<String, Object> p = new TreeMap(fields);
        p.put("_appid",appId);
        p.put("_timestamp", String.valueOf(ts));


        StringBuilder sb = new StringBuilder();
        sb.append(appkey);
        for (Map.Entry<String, Object> me : p.entrySet())
            sb.append(me.getKey() + me.getValue());
        sb.append(appkey);

        System.out.println("before:"+sb);
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(sb.toString().getBytes());
//        byte b[] = md.digest();
//        int i;
//        StringBuffer buf = new StringBuffer("");
//        for (int offset = 0; offset < b.length; offset++) {
//            i = b[offset];
//            if (i < 0) i += 256;
//            if (i < 16) buf.append("0");
//            buf.append(Integer.toHexString(i));
//        }
//        //debug

        try {
            String  sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes("utf-8"));
            System.out.println("_sign=" + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "a");
        map.put("roomid", "2");
        map.put("type", "1");

        String appId = "live.open.jkx.i";
        String secret = "7jC2^t%^36"; //"riX0C07R";

        appId = "testid";
        secret = "testkey";

        long ts=new Date().getTime()/1000;

        String ss = RequestSignHelper.sign(map,appId, secret, ts);

        System.out.println("online: " + ss);

        test(appId, secret, ts, map);


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
