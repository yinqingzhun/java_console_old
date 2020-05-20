package com.yqz.console.tech.slide;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SlotBaseCounter {
    private int slotSize;
    private AtomicInteger[] slotCounter;

    public SlotBaseCounter(int slotSize) {
        slotSize = slotSize < 1 ? 1 : slotSize;
        this.slotSize = slotSize;
        this.slotCounter = new AtomicInteger[slotSize];
        for (int i = 0; i < this.slotSize; i++) {
            slotCounter[i] = new AtomicInteger(0);
        }
    }

    public void increaseSlot(int slotIndex) {
        Preconditions.checkState(slotIndex < slotSize, "slot index is out of bound, size is "+slotSize);
        slotCounter[slotIndex].incrementAndGet();
    }

    /**
     * 擦除slot
     *
     * @param slotIndex
     */
    public void wipeSlot(int slotIndex) {
        Preconditions.checkState(slotIndex < slotSize, "slot index is out of bound, size is "+slotSize);
        slotCounter[slotIndex].set(0);
    }

    /**
     * 所有slot中值的总和
     *
     * @return
     */
    public int totalCount() {
        return Arrays.stream(slotCounter).mapToInt(slotCounter -> slotCounter.get()).sum();
    }

    @Override
    public String toString() {
        return Arrays.toString(slotCounter);
    }

    public static void main(String[] args) {
        SlotBaseCounter slotBaseCounter = new SlotBaseCounter(10);
        slotBaseCounter.increaseSlot(5);

        System.out.println(slotBaseCounter.totalCount());

    }

}  