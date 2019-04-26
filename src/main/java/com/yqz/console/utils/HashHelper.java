package com.yqz.console.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class HashHelper {
    static Logger logger = LoggerFactory.getLogger(HashHelper.class);

    public static String getHash(File f, String hashType) {

        InputStream ins = null;
        try {
            ins = new FileInputStream(f);
            byte[] buffer = new byte[8192];
            MessageDigest md5 = MessageDigest.getInstance(hashType);

            int len;
            while ((len = ins.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            return  DatatypeConverter.printHexBinary(md5.digest());
            //return DigestUtils.md5Hex(md5.digest());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            try {
                if (ins != null)
                    ins.close();
            } catch (IOException e1) {
                logger.warn(e1.getMessage());
            }
        }
        return null;
    }
}
