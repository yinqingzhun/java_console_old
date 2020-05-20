package com.yqz.console.tech;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MsgSeq implements Comparable<MsgSeq> {
    private static final AtomicLong ID = new AtomicLong();//全局（跨msgid,不入载荷）
    private final AtomicInteger readCount = new AtomicInteger();
    private final ConcurrentHashMap<Byte, Boolean> cards = new ConcurrentHashMap();

    private long id;
    public volatile long msgid;
    public volatile int seq;
    public volatile byte[] bytes;

    private MsgSeq() {
    }


    //工厂方法
    public static MsgSeq newObj(long msgid_, int seq_, byte[] bytes_) {
        MsgSeq ms = new MsgSeq();
        ms.id = ID.incrementAndGet();
        ms.msgid = msgid_;
        ms.seq = seq_;
        ms.bytes = bytes_;
        return ms;
    }

    public boolean isNoRead() {
        return readCount.get() == 0;
    }

    public void resetReadCount() {
        readCount.set(0);
    }

    //设置已读
    public boolean setRead(byte cardid) {
        final boolean[] success = {false};
        cards.computeIfAbsent(cardid, k -> {
            success[0] = readCount.incrementAndGet() < 256;
            return false;
        });
        return success[0];
    }

    /**
     * 记录卡读取了数据包
     *
     * @param cardid
     * @return 如果数据包第一次被读取，返回true
     */
    public boolean setFirstRead(byte cardid) {

        final boolean[] success = {false};
        cards.computeIfAbsent(cardid, k -> {
            success[0] = readCount.incrementAndGet() == 1;
            if (success[0]) {
                return false;
            } else {
                readCount.decrementAndGet();
                return null;
            }
        });
        return success[0];
    }

    //卡的 读状态

    /**
     * 卡是否已读取数据包
     *
     * @param cardid
     * @return
     */
    public boolean getRead(byte cardid) {
        return cards.get(cardid) != null;
    }

    public boolean isAck() {
        return readCount.get() >= 256;//考虑并发setAck & setRead
    }

    public void setAck() {
        readCount.set(256);
    }


    @Override
    public int compareTo(MsgSeq other) {
        if (other == null)
            return 1;

        if (this.readCount == null && other.readCount == null)
            return 0;
        if (this.readCount == null)
            return -1;
        if (other.readCount == null)
            return 1;

        int x = (this.readCount.get() < other.readCount.get()) ? -1 : ((this.readCount.get() == other.readCount.get()) ? 0 : 1);
        if (x == 0) {
            return (this.id < other.id) ? -1 : ((this.id == other.id) ? 0 : 1);
        }

        return x;
    }

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        List<Runnable> runnableList = new LinkedList<>();
        List<MsgSeq> list = new LinkedList<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 30; j++) {
                MsgSeq msgSeq = MsgSeq.newObj(i, j, new byte[]{0});
                list.add(msgSeq);
                for (int k = 0; k < 30; k++) {
                    runnableList.add(() -> {
                        msgSeq.readCount.addAndGet(random.nextInt(Byte.MAX_VALUE));
                    });
                }
            }
        }

        runnableList.parallelStream().forEach(p -> executorService.schedule(p, 0, TimeUnit.MILLISECONDS));


        list.stream().sorted().forEach(msgSeq -> {
            System.out.println(msgSeq.readCount.get() + ":" + msgSeq.id);
        });
    }
}