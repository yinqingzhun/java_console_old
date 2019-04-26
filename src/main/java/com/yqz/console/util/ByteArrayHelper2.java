package com.yqz.console.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ByteArrayHelper2 {
    public static byte[] intToBytesBigEndian(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    public static byte[] intToBytesBuffer( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    public static byte[] intToByteArray ( final int i ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(i);
        dos.flush();
        return bos.toByteArray();
    }

    public static byte[] bigIntToByteArray( final int i ) {
        BigInteger bigInt = BigInteger.valueOf(i);
        return bigInt.toByteArray();
    }
    

    public static int bytesToIntBigEndian(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }


    public static int bytesToIntSmallEndian(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) )
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | (src[offset + 3] & 0xFF)<< 24);
        return value;
    }

    public static byte[] intToBytes(final int data) {
        return new byte[] {
                (byte)((data >> 24) & 0xff),
                (byte)((data >> 16) & 0xff),
                (byte)((data >> 8) & 0xff),
                (byte)((data >> 0) & 0xff),
        };
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

     
}
