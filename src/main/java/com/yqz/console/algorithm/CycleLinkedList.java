package com.yqz.console.algorithm;

import com.google.common.base.Preconditions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CycleLinkedList<T> {
    private int capacity = 10;
    private T[] array = null;
    private int head = -1;

    public CycleLinkedList(Class<T> clazz, int initCapacity) {
        Preconditions.checkState(initCapacity > 0);
        this.capacity = initCapacity;
        array = (T[]) Array.newInstance(clazz, initCapacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public void add(T ele) {
        synchronized (this) {

            if (++head >= capacity) {
                head = 0;
            }

            array[head] = ele;

        }
    }

    public T[] toArray() {
        return Arrays.copyOfRange(array, 0, array.length);
    }

    
    public static void main(String[] args){
        CycleLinkedList<Integer> linkedList=new CycleLinkedList(Integer.class,3);
        for (int i = 0; i < 10; i++) {
            linkedList.add(i+1);
        }
        
        System.out.println(Arrays.stream(linkedList.toArray()).map(p->p.toString()).collect(Collectors.joining(",")) );
    }

}
