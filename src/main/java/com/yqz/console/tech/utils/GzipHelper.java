package com.yqz.console.tech.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class GzipHelper {
    public static byte[] uncompress(byte[] data) {
        if (data == null || data.length == 0) {
            return data;
        }

        ByteArrayOutputStream out = null;
        GZIPInputStream gzip = null;
        try {
            out = new ByteArrayOutputStream();
            gzip = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }


        } catch (IOException e) {
            log.warn(e.getMessage());
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }


        }

        return out.toByteArray();
    }

    public static byte[] compress(byte[] data)   {
        if (data == null || data.length == 0) {
            return new byte[0];
        }
        ByteArrayOutputStream out = null;
        GZIPOutputStream gzip = null;
        try {
            out = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(out);
            gzip.write(data);
        } catch (IOException e) {
            log.warn(e.getMessage());
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }
        }
         
        return out.toByteArray();
    }

    public static byte[] compress(String str) {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        try {
            return compress(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
        }
        return new byte[0];
    }
    public static String uncompress(String str)   {
        if (str == null || str.length() == 0) {
            return str;
        }
        byte[] data = new byte[0]; // ISO-8859-1  
        try {
            data = uncompress(str.getBytes("utf-8"));
            return new String(data);
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
        }
        return null;
    }
}
