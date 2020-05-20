package com.yqz.console.tech.utils;

public class ByteUtils {
    // encode 8 bits unsigned int
    public static void encode8u(byte[] p, int offset, byte c) {
        p[0 + offset] = c;
    }

    // decode 8 bits unsigned int
    public static byte decode8u(byte[] p, int offset) {
        return p[0 + offset];
    }

    /* encode 16 bits unsigned int (msb) */
    public static void encode16u(byte[] p, int offset, int w) {
        p[offset + 0] = (byte) (w >> 8);
        p[offset + 1] = (byte) (w >> 0);
    }

    /* decode 16 bits unsigned int (msb) */
    public static int decode16u(byte[] p, int offset) {
        int ret = (p[offset + 0] & 0xFF) << 8
                | (p[offset + 1] & 0xFF);
        return ret;
    }

    public static void encode16(byte[] p, int offset, int value) {
        p[offset] = (byte) (value >>> 8);
        p[offset + 1] = (byte) value;
    }

    public static byte[] encode16(int value) {
        byte[] b = new byte[2];
        ByteUtils.encode16(b,0,value);
        return b;
    }

    public static short decode16(byte[] p, int offset) {
        return (short) (p[offset] << 8 | p[offset + 1] & 255);
    }


    /* encode 32 bits unsigned int (msb) */
    public static void encode32u(byte[] p, int offset, long l) {
        p[offset + 0] = (byte) (l >> 24);
        p[offset + 1] = (byte) (l >> 16);
        p[offset + 2] = (byte) (l >> 8);
        p[offset + 3] = (byte) (l >> 0);
    }

    /* decode 32 bits unsigned int (msb) */
    public static long decode32u(byte[] p, int offset) {
        long ret = (p[offset + 0] & 0xFFL) << 24
                | (p[offset + 1] & 0xFFL) << 16
                | (p[offset + 2] & 0xFFL) << 8
                | p[offset + 3] & 0xFFL;
        return ret;
    }

    public static byte[] encode16u(int n) {
        byte[] b = new byte[2];
        ByteUtils.encode16u(b, 0, n);
        return b;
    }

    public static byte[] encode32u(long n) {
        byte[] b = new byte[4];
        ByteUtils.encode32u(b, 0, n);
        return b;
    }




    public static void encode32(byte[] p, int offset, int value) {
        p[offset + 0] = (byte) (value >>> 24);
        p[offset + 1] = (byte) (value >>> 16);
        p[offset + 2] = (byte) (value >>> 8);
        p[offset + 3] = (byte) value;
    }
    public static byte[] encode32(int value) {
        byte[] b=new byte[4];
        ByteUtils.encode32(b,0,value);
        return b;
    }
    public static int decode32(byte[] p, int offset) {
        return (p[offset + 0] & 255) << 24
                | (p[offset + 1] & 255) << 16
                | (p[offset + 2] & 255) << 8
                | p[offset + 3] & 255;
    }


}