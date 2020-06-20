package com.yqz.console.tech;


import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Application2 {

    public static void main(String[] args) throws Exception {
//        Long[] ints = new Long[]{MSGID_MAX - 4, MSGID_MAX - 3, MSGID_MAX - 2, MSGID_MAX - 1, MSGID_MAX, 0L, 1L, 2L, 3L};
//        for (int j = 0; j < 10; j++) {
//            LAST_ID = MSGID_MAX - 5;
//            fasterMsgBuffer.clear();
//
//            Long[] copy = shuffle(ints);
//            System.out.println("第" + (j + 1) + "次:");
//            System.out.println(Arrays.stream(copy).map(p -> p.toString()).collect(Collectors.joining(",")));
//            for (int i = 0; i < ints.length; i++) {
//                order3(copy[i]);
//            }
//
//        }

        Integer[] a=new Integer[10];
        for (int i = 0; i <10 ; i++) {
            a[i]=i;
        }
        a=shuffle(a);
        Stream.of(a).forEach(p->System.out.print(p+","));

        System.out.println();

        a=shuffle(a);
        Stream.of(a).forEach(p->System.out.print(p+","));

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


    private static final AtomicInteger readCount = new AtomicInteger();
    private static final ConcurrentHashMap<Byte, Boolean> cards = new ConcurrentHashMap();

    static long LAST_ID = 0;
    private static long MSGID_MAX = 4294967295l;
    private static final Map<Long, String> fasterMsgBuffer = new ConcurrentHashMap<>();
    private static long MSGID_MIN = 0L;

    private static void order1(long _msgid) {
        if (_msgid == LAST_ID + 1) {
            LAST_ID = _msgid;
            System.out.println(_msgid);

            long nextMsgid;
            String next;
            while ((next = fasterMsgBuffer.remove(nextMsgid = LAST_ID + 1)) != null) {
                System.out.println(nextMsgid);
                LAST_ID = nextMsgid;
            }
        } else if (LAST_ID - _msgid > Integer.MAX_VALUE && LAST_ID == MSGID_MAX) {
            LAST_ID = _msgid == MSGID_MIN ? MSGID_MIN : MSGID_MIN - 1;
            if (_msgid == MSGID_MIN)
                System.out.println(_msgid);
            else
                fasterMsgBuffer.putIfAbsent(_msgid, String.valueOf(_msgid));

            long nextMsgid;
            String next;
            while ((next = fasterMsgBuffer.remove(nextMsgid = LAST_ID + 1)) != null) {
                System.out.println(nextMsgid);
                LAST_ID = nextMsgid;
            }
        } else {
            fasterMsgBuffer.putIfAbsent(_msgid, String.valueOf(_msgid));

        }

        if (LAST_ID == MSGID_MAX) {
            long start = MSGID_MIN - 1;
            long nextMsgid;
            String next;
            while ((next = fasterMsgBuffer.remove(nextMsgid = start + 1)) != null) {
                System.out.println(nextMsgid);
                start = nextMsgid;
            }
            if (start > 0)
                LAST_ID = start;
        }
    }

    private static void outputAndSetLastId(long msgId) {
        if (msgId == MSGID_MAX + 1) {
            msgId = 0;
        }

        System.out.println(msgId);
        LAST_ID = msgId;
    }


    private static void order2(long _msgid) {

        if (_msgid == MSGID_MIN)
            _msgid += MSGID_MAX + 1;

        if (_msgid == LAST_ID + 1) {
            outputAndSetLastId(_msgid);

            long nextMsgid;
            String next;
            while ((next = fasterMsgBuffer.remove(nextMsgid = LAST_ID + 1)) != null) {
                outputAndSetLastId(nextMsgid);
            }
        } else {
            fasterMsgBuffer.putIfAbsent(_msgid, String.valueOf(_msgid));
        }
    }

    private static void order3(long _msgid) {
        if (_msgid == LAST_ID + 1 || (_msgid == 0 && LAST_ID == MSGID_MAX)) {
            LAST_ID = _msgid;
            System.out.println(_msgid);

            long nextMsgid;
            String next;
            while ((next = fasterMsgBuffer.remove(nextMsgid = LAST_ID + 1 > MSGID_MAX ? 0 : LAST_ID + 1)) != null) {
                System.out.println(nextMsgid);
                LAST_ID = nextMsgid;
            }
        } else {
            fasterMsgBuffer.putIfAbsent(_msgid, String.valueOf(_msgid));
            System.out.println("faster: "+fasterMsgBuffer.keySet().stream().sorted().map(p -> p.toString()).collect(Collectors.joining(",")));
        }
    }


}
