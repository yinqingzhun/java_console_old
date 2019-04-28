package com.yqz.console.utils;

import java.security.MessageDigest;

public class HashHelper {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String sha1(String srcStr){
        return hash("SHA-1", srcStr);
    }
    public static String md5(String srcStr){
        return hash("MD5", srcStr);
    }
    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }


}