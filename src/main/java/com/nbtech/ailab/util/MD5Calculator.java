package com.nbtech.ailab.util;

import com.nbtech.ailab.biz.dto.SysUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 根据文件 生成Md5唯一值
 *
 */
public class MD5Calculator {

    public static String calculateMD5(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[4096];
            int numRead;
            while ((numRead = inputStream.read(buffer)) > 0) {
                md.update(buffer, 0, numRead);
            }
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }


    /**
     * 根据用户名和id生成唯一的md5值
     * @param userDto
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getUserMD5(SysUserDto userDto) throws NoSuchAlgorithmException {
        return getMD5(userDto.getId() + userDto.getUsername());
    }

    /**
     * 字符串生成唯一md5值
     * @param input 输入字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String input) throws NoSuchAlgorithmException {
        // 获取MD5摘要算法的 MessageDigest 对象
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 使用指定的字节更新摘要信息
        md.update(input.getBytes());

        // 得到密文（即：MD5哈希值）
        byte[] mdBytes = md.digest();

        // 将二进制转换成16进制字符串形式
        StringBuilder hexString = new StringBuilder();
        for (byte b : mdBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        // 返回MD5哈希值的16进制字符串表示形式
        return hexString.toString();
    }

    /**
     * 获取字符串的MD5值
     *
     * @param input 输入的字符串
     * @return 输入字符串的MD5值
     */
    public static String get10LengthMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) { // 只取前10个字符
                sb.append(String.format("%02x", digest[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }


    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
