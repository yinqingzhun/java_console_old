package com.yqz.console.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ByteArrayHelper {
    public static long merge(ByteOrder byteOrder, int left, int right) {

        if (byteOrder == ByteOrder.BIG_ENDIAN){
            byte[] la = intToBytesBigEndian(left);
            byte[] ra = intToBytesBigEndian(right);
            return bytesToValue(byteOrder, merge(la, ra), 0, 8);
        }else{
            byte[] la = intToBytesLittleEndian(left);
            byte[] ra = intToBytesLittleEndian(right);
            return bytesToValue(ByteOrder.LITTLE_ENDIAN, merge(la, ra), 0, 8);
        }
    }

    public static byte[] intToBytesBigEndian(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToIntBigEndian(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }

    public static byte[] intToBytesLittleEndian(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToIntLittleEndian(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF))
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | (src[offset + 3] & 0xFF) << 24);
        return value;
    }

    public static byte[] merge(byte[]... bytes) {
        if (bytes == null || bytes.length == 0)
            return new byte[0];

        int len = Stream.of(bytes).map(p -> p.length).collect(Collectors.reducing((a, b) -> a + b)).orElse(0);
        byte[] all = new byte[len];
        int pos = 0;
        for (int i = 0; i < bytes.length; i++) {
            System.arraycopy(bytes[i], 0, all, pos, bytes[i].length);
            pos += bytes[i].length;
        }
        return all;
    }

    public static byte[] reverse(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return bytes;

        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - 1 - i];
            bytes[bytes.length - 1 - i] = temp;
        }

        return bytes;
    }

    public static byte[] valueToBytes(ByteOrder byteOrder, long value, int length) {
        if (length != 1 && length != 2 && length != 4 && length != 8) {
            throw new IllegalArgumentException("length is 1 or 2 or 4 or 8");
        }
        ByteBuffer byteBuf = ByteBuffer.allocate(length).order(byteOrder);

        switch (length) {
            case 1:
                byteBuf.put((byte) value);
                break;
            case 2:
                byteBuf.putShort((short) value);
                break;
            case 4:
                byteBuf.putInt((int) value);
                break;
            case 8:
                byteBuf.putLong(value);
                break;
        }


        byte[] bytes = byteBuf.array();

        return bytes;
    }

    public static long bytesToValue(ByteOrder byteOrder, byte[] bytes, int offset, int length) {
        if (length != 1 && length != 2 && length != 4 && length != 8) {
            throw new IllegalArgumentException("length is 1 or 2 or 4 or 8");
        }

        ByteBuffer byteBuf = ByteBuffer.allocate(length).order(byteOrder);
        byteBuf.put(bytes, offset, length);

        switch (length) {
            case 1:
                return byteBuf.get(0);
            case 2:
                return byteBuf.getShort(0);
            case 4:
                return byteBuf.getInt(0);
            case 8:
                return byteBuf.getLong(0);
        }
        return 0;

    }

    public static boolean contains(byte[] bytes, byte target) {
        for (byte b : bytes) {
            if (b == target)
                return true;
        }
        return false;
    }
}
