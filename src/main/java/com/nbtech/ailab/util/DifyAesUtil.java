package com.nbtech.ailab.util;

import com.alibaba.fastjson.JSON;
import com.nbtech.ailab.config.AesSecret;
import com.nbtech.ailab.vo.AesKeyVo;

public class DifyAesUtil {

    private static String aesKey = "{\"keyArr\":\"jPv7c8jrfUhiiWvEsP9xhg==\"}";

    public static String getAesResult(String password) {
//            byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//            AesKeyVo aesKeyVo = new AesKeyVo();
//            aesKeyVo.setKeyArr(key);
//            aesKey = JSON.toJSONString(aesKeyVo);
        AesKeyVo aesKeyVo = JSON.parseObject(aesKey, AesKeyVo.class);
        return AesSecret.addSecret(password, aesKeyVo.getKeyArr());
    }

    public static String getPassword(String secret) {
        AesKeyVo aesKeyVo = JSON.parseObject(aesKey, AesKeyVo.class);
        return AesSecret.cancel(secret, aesKeyVo.getKeyArr());
    }
}
