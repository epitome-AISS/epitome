package com.nbtech.ailab.external.facade;

import com.nbtech.ailab.util.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisQueueFacade {

    @Autowired
    private RedisService redisService;

    /**
     * 将数据推送到队列
     *
     * @param data 要推送的数据
     */
    public void push(Object data,String key) {
        try {
            redisService.lSet(key, data);
            log.debug("Successfully pushed data to queue: {}", key);
        } catch (Exception e) {
            log.error("Failed to push data to queue: {}, error: {}", key, e.getMessage(), e);
            throw new RuntimeException("Failed to push data to queue", e);
        }
    }

    /**
     * 从队列中批量获取数据
     *
     * @param key 队列名称
     * @return 数据列表
     */
    public List<Object> pop(String key) {
        try {
            Long keySize = getKeySize(key);
            if (keySize != null && keySize > 0) {
                return redisService.lPop(key, keySize.intValue());
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("Failed to pop data from queue: {}, error: {}", key, e.getMessage(), e);
            throw new RuntimeException("Failed to pop data from queue", e);
        }
    }

    /**
     * 获取队列长度
     *
     * @param key 队列名称
     * @return 队列长度
     */
    public Long getKeySize(String key) {
        try {
            return redisService.lGetListSize(key);
        } catch (Exception e) {
            log.error("Failed to get queue size: {}, error: {}", key, e.getMessage(), e);
            throw new RuntimeException("Failed to get queue size", e);
        }
    }

}
