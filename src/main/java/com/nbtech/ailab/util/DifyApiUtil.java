package com.nbtech.ailab.util;

import com.nbtech.ailab.common.RedisHeadEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DifyApiUtil {

    @Autowired
    private RedisService redisService;

    /**
     * 获取redis中当前用户的Token名称
     *
     * @param userId 当前用户id
     * @return
     */
    public String getRedisName(Long userId) {
        return RedisHeadEnum.DIFY_TOKEN_HEAD.getDesc() + userId;
    }

    /**
     * 获取当前用户的difyToken
     *
     * @return
     */
    public String getDifyToken() {
        Long userId = ShiroUtils.getUserId();
        String redisName = getRedisName(userId);
        if (!redisService.hasKey(redisName)){
            return null;
        }
        return redisService.get(redisName).toString();
    }

}
