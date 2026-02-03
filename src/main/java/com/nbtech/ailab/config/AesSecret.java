package com.nbtech.ailab.config;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.apache.ibatis.annotations.Param;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author nber
 */
public class AesSecret {

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String addSecret(String content, byte[] key) {
        AES aes = SecureUtil.aes(key);
        // 加密为16进制表示
        return aes.encryptHex(content);
    }

    /**
     * 解密
     *
     * @param content 加密秘钥
     */
    public static String cancel(String content, byte[] key) {
        AES aes = SecureUtil.aes(key);
        // 解密
        return aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
    }

}

