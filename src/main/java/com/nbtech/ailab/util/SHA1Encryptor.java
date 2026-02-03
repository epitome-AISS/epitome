package com.nbtech.ailab.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha1 加密对象
 */
public class SHA1Encryptor {

    /**
     * 使用SHA-1算法对字符串进行加密
     * @param input 需要加密的字符串
     * @return 加密后的十六进制字符串
     */
    public static String encryptSHA1(String input) {
        try {
            // 创建一个SHA-1的MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // 对输入字符串进行加密
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
}
