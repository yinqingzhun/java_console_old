package com.yqz.console.tech.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Node<E> {
    volatile E item;
    volatile Node<E> next;

    /**
     * Constructs a new node.  Uses relaxed write because item can
     * only be seen after publication via casNext.
     */
    Node(E item) {
        UNSAFE.putObject(this, itemOffset, item);
    }

    boolean casItem(E cmp, E val) {
        return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
    }

    void lazySetNext(Node<E> val) {
        UNSAFE.putOrderedObject(this, nextOffset, val);
    }

    boolean casNext(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
    }

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE;
    private static final long itemOffset;
    private static final long nextOffset;

    public static Unsafe getUnsafe() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    static {
        try {
            UNSAFE =  getUnsafe();
            Class<?> k = Node.class;
            itemOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("item"));
            nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }


    public static void main(String[] args) {
        Node<Integer> root = new Node<>(null);
        Node<Integer> node1 = new Node<>(1);
        root.casNext(null, node1);

        System.out.println();
    }
}