package com.yqz.console.tech.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Roylion
 * @Description: AES算法封装
 * @Date: Created in 9:46 2018/8/9
 */
public class AESUtil {

    /**
     * 加密算法
     */
    private static final String ENCRY_ALGORITHM_AES = "AES";

    /**
     * 加密算法/加密模式/填充类型
     * 本例采用AES加密，ECB加密模式，PKCS5Padding填充
     */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    /**
     * 设置iv偏移量
     * 本例采用ECB加密模式，不需要设置iv偏移量
     */
    private static final String IV_ = null;

    /**
     * 设置加密字符集
     * 本例采用 UTF-8 字符集
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * 密码处理方法
     * 如果加解密出问题，
     * 请先查看本方法，排除密码长度不足填充0字节,导致密码不一致
     *
     * @param password 待处理的密码
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] pwdHandler(String password) throws UnsupportedEncodingException {
        byte[] data = new byte[0];
        if (password != null) {
            byte[] bytes = password.getBytes(UTF_8);
            if (bytes.length > 32)
                throw new IllegalArgumentException("password length exceeds maximum supported length");

            int size = 0;
            int[] lengthArray = Arrays.stream(EnumPasswordSize.values()).mapToInt(p -> p.getValue()).sorted().toArray();
            for (int i = 0; i < lengthArray.length; i++) {
                if (bytes.length <= lengthArray[i]) {
                    size = lengthArray[i];
                    break;
                }
            }

            System.arraycopy(bytes, 0, data = new byte[size], 0, bytes.length);
        }
        return data;
    }

    //======================>原始加密<======================

    /**
     * 原始加密
     *
     * @param cipherMode 加密规则
     * @param textBytes 明文字节数组，待加密的字节数组
     * @param pwdBytes  加密密码字节数组
     * @return 返回加密后的密文字节数组，加密错误返回null
     */
    public static byte[] encrypt(String cipherMode, byte[] textBytes, byte[] pwdBytes) {
        try {
            // 1 获取加密密钥
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, ENCRY_ALGORITHM_AES);
            Security.addProvider(new BouncyCastleProvider());
            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(cipherMode);

            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            // 4 执行
            byte[] cipherTextBytes = cipher.doFinal(textBytes);

            // 5 返回密文字符集
            return cipherTextBytes;

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param cipherMode 加密规则
     * @param cipherTextBytes  待解密的字节数组
     * @param pwdBytes  解密密码字节数组
     * @return 返回解密后的明文字节数组，解密错误返回null
     */
    public static byte[] decrypt(String cipherMode, byte[] cipherTextBytes, byte[] pwdBytes) {

        try {
            // 1 获取解密密钥
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, ENCRY_ALGORITHM_AES);
            Security.addProvider(new BouncyCastleProvider());
            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(cipherMode);

            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            // 4 执行
            byte[] clearTextBytes = cipher.doFinal(cipherTextBytes);

            // 5 返回明文字符集
            return clearTextBytes;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 解密错误 返回null
        return null;
    }



    /**
     * 加密
     *
     * @param cipherMode 加密规则
     * @param clearText 明文，待加密的内容
     * @param password  密码，加密的密码
     * @return 返回密文字节数组的Hex形式。加密错误返回null
     */
    public static String encryptHex(String cipherMode, String clearText, String password) {
        try {
            // 1 获取加密密文字节数组
            byte[] cipherTextBytes = encrypt(cipherMode, clearText.getBytes(UTF_8), pwdHandler(password));

            // 2 对密文字节数组进行 转换为 HEX输出密文
            String cipherText = byte2hex(cipherTextBytes);

            // 3 返回 HEX输出密文
            return cipherText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 加密错误返回null
        return null;
    }

    /**
     * 加密
     *
     * @param cipherMode 加密规则
     * @param clearText 明文，待加密的内容
     * @param password  密码，加密的密码
     * @return 返回密文字节数组的base64形式。加密错误返回null
     */
    public static String encryptBase64(String cipherMode, String clearText, String password) {
        try {
            // 1 获取加密密文字节数组
            byte[] cipherTextBytes = encrypt(cipherMode, clearText.getBytes(UTF_8), pwdHandler(password));

            // 2 对密文字节数组进行 转换为 base64输出密文
            String cipherText = Base64Utils.encodeToString(cipherTextBytes);

            // 3 返回 base64输出密文
            return cipherText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 加密错误返回null
        return null;
    }

    /**
     * 解密Hex字符串
     *
    * @param cipherText 密文，带解密的内容
     * @param password   密码，解密的密码
     * @return 返回明文，解密后得到的内容。解密错误返回null
     */
    public static String decryptHex(String cipherMode, String cipherText, String password) {
        try {
            // 1 将HEX输出密文 转为密文字节数组
            byte[] cipherTextBytes = hex2byte(cipherText);

            // 2 将密文字节数组进行解密 得到明文字节数组
            byte[] clearTextBytes = decrypt(cipherMode, cipherTextBytes, pwdHandler(password));

            // 3 根据 CHARACTER 转码，返回明文字符串
            return new String(clearTextBytes, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 解密错误返回null
        return null;
    }
    /**
     * 解密base64字符串
     *
     * @param cipherText 密文，带解密的内容
     * @param password   密码，解密的密码
     * @return 返回明文，解密后得到的内容。解密错误返回null
     */
    public static String decryptBase64(String cipherMode, String cipherText, String password) {
        try {
            // 1 将HEX输出密文 转为密文字节数组
            byte[] cipherTextBytes = Base64Utils.decodeFromString(cipherText);

            // 2 将密文字节数组进行解密 得到明文字节数组
            byte[] clearTextBytes = decrypt(cipherMode, cipherTextBytes, pwdHandler(password));

            // 3 根据 CHARACTER 转码，返回明文字符串
            return new String(clearTextBytes, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 解密错误返回null
        return null;
    }

    /*字节数组转成16进制字符串  */
    public static String byte2hex(byte[] bytes) { // 一个字节的数，
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        String tmp = "";
        for (int n = 0; n < bytes.length; n++) {
            // 整数转成十六进制表示
            tmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase(); // 转成大写
    }

    /*将hex字符串转换成字节数组 */
    private static byte[] hex2byte(String str) {
        if (str == null || str.length() < 2) {
            return new byte[0];
        }
        str = str.toLowerCase();
        int l = str.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = str.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

    public enum EnumPasswordSize {
        Sixteen(16), TweentyFour(24), ThirtyTwo(32);
        int value;

        EnumPasswordSize(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static void main(String[] args) {
        Map<String,String> map=new HashMap<>();
        map.put("live.open.jkx.t", "riX0C07R");
        map.put("live.open.jkx.tz", "A06K0E3K");

     map.forEach((k,v)->{
         String result= AESUtil.encryptHex("AES/ECB/PKCS7Padding", v, "cZJ06L#ciPau^A!R");
         System.out.println(k+":"+result);
         System.out.println(AESUtil.decryptHex("AES/ECB/PKCS7Padding", result, "cZJ06L#ciPau^A!R"));
     });

    }


}