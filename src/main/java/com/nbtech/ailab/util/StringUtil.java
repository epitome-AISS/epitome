package com.nbtech.ailab.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtil {

    private static final Random random = new Random();


    /**
     * 转换旧的字符串id为新的字符串id 并放入map
     * @param idMap
     * @param oldId
     * @return
     */
    public static String changeNewId(Map<String,String> idMap, String oldId) throws InterruptedException {
        if (idMap.containsKey(oldId)){
            return idMap.get(oldId);
        }
        int sleepTime = 50 + random.nextInt(50); // 50ms基础 + 0~50ms随机
        try {
            // 线程休眠50毫秒
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("线程休眠被中断", e);
        }

        // 获取当前时间戳（毫秒级）
        long newIdLong = System.currentTimeMillis();
        String newId = Long.toString(newIdLong);
        idMap.put(oldId, newId);
        return newId;
    }

    /**
     * 转换旧的字符串id集合为新的字符串id集合
     * @param oldIds
     * @param idMap
     * @return
     */
    public static List<String> changeListId(List<String> oldIds, Map<String, String> idMap) throws InterruptedException {
        List<String> newIds = new ArrayList<String>();
        for (String id : oldIds) {
            newIds.add(changeNewId(idMap, id));
        }
        return newIds;
    }
}
