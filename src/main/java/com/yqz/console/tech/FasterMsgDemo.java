package com.yqz.console.tech;


import com.google.common.base.Joiner;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class FasterMsgDemo {
    /**
     * 重置LAST_ID时fasterMsg队列的最大长度
     */
    private int maxLengthWhenResetFasterMsg = 10;

    /**
     * fasterMsgBuffer为空以来，有快消息时的LAST_ID
     */
    private long lastIdWhenResetFasterMsg = 0;
    /**
     * 经过maxResetMs毫秒LAST_ID未变化，且fasterMsgBuffer长度大于maxLengthWhenResetFasterMsg，将重置LAST_ID为fasterMsgBuffer中最大的msgId，抛弃缺失的msgId片段
     */
    private long maxWaitMsWhenResetFasterMsg = 3 * 1000;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private AtomicBoolean allowResetLastId = new AtomicBoolean(true);
    public static volatile long LAST_ID = 0;
    private final Map<Long, Tuple3<Long, byte[], Integer>> fasterMsgBuffer = new ConcurrentHashMap<>();//更快到达的“未来消息”
    private final ConcurrentHashMap<InetSocketAddress, Long> clients = new ConcurrentHashMap<InetSocketAddress, Long>();
    private static final ConcurrentHashMap<Long, Boolean> testFlagMap = new ConcurrentHashMap<>();
//    public static final LinkedBlockingQueue<Tuple3<Long, byte[], Integer>> receivedMsgQueue = new LinkedBlockingQueue();//对外提供消费


    public FasterMsgDemo() {
        //客户端重启时，可能导致接收的消息ID不连续。假如，聚合端有1 2 3三个数据包，客户端重启前，发送了2，重启以后，继续发送1 3
        //检测fasterMsgBuffer是否阻塞，即由于收到的msgId不连续，始终无法满足下一个msgId=LAST_ID+1，导致LAST_ID长时间不能更新
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            synchronized (this) {
                if (lastIdWhenResetFasterMsg == LAST_ID && fasterMsgBuffer.size() > maxLengthWhenResetFasterMsg) {
                    //找到第一段连续的fasterMsg
                    do {
                        LAST_ID++;
                    } while (retrieveFasterMsg() == 0);

                    log.warn("after waiting for {} ms, faster msg buffer is cleared. LAST_ID is reset from {} to {}", maxWaitMsWhenResetFasterMsg, lastIdWhenResetFasterMsg, LAST_ID);
                }
                lastIdWhenResetFasterMsg = LAST_ID;
            }
        }, maxWaitMsWhenResetFasterMsg, maxWaitMsWhenResetFasterMsg, TimeUnit.MILLISECONDS);
    }

    /**
     * 从LAST_ID+1开始，检查fasterMsg队列中是否有连续的msgId序列，并提取出来
     *
     * @return 提取的连续消息个数
     */
    private int retrieveFasterMsg() {
        long _msgid = LAST_ID;
        long nextMsgid;
        Tuple3<Long, byte[], Integer> next;
        int count = 0;
        while ((next = fasterMsgBuffer.remove(nextMsgid = LAST_ID + 1 > 4294967295L ? 0 : LAST_ID + 1)) != null) {
            log.info("delete:{}", next._1);
            LAST_ID = nextMsgid;
            count++;
        }

        if (_msgid != LAST_ID && fasterMsgBuffer.size() == 0) {
            log.info("delete faster {} messages ( from {} to {} )"
                    , count, _msgid, LAST_ID);
        }

        return count;
    }

    public void addMsg(long _msgid) {
        synchronized (this) {
            byte[] originalPacket = new byte[0];
            int blocks = 0;

            if (_msgid == 0)
                _msgid += 4294967295L + 1;

            if (_msgid == LAST_ID + 1 || (_msgid == 0 && LAST_ID == 4294967295L)) {
                LAST_ID = _msgid;
                log.info("coming:{}", _msgid);

                retrieveFasterMsg();


            } else {
                addFasterMsg(_msgid, Tuple.of(_msgid, originalPacket, blocks));
            }
        }
    }

    public void printFasterMsg() {
        log.info("fasterMsg:{}", Joiner.on(",").join(fasterMsgBuffer.keySet()));
    }

    private void addFasterMsg(long _msgid, Tuple3 tuple) {
        int size = fasterMsgBuffer.size();
        Tuple3 tuple3 = fasterMsgBuffer.putIfAbsent(_msgid, tuple);


        if (size > 10 && size % 10 == 0)
            log.info("add faster messages (total {} ) {}/{} {}", size, LAST_ID, _msgid, tuple3 == null ? "" : "[重复]");


    }


    public static void main(String[] args) {
        LAST_ID = -1;
        FasterMsgDemo fasterMsgDemo = new FasterMsgDemo();

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                fasterMsgDemo.addMsg(i + 1);
            }
            fasterMsgDemo.printFasterMsg();
        });
        thread.start();


    }
}
