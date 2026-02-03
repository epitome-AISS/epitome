package com.nbtech.ailab.util;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class EhcacheUtil {

    private static volatile EhcacheUtil instance;
    private CacheManager cacheManager;
    private Cache<String, String> objectCache;

    // 私有构造方法（防止外部实例化）
    private EhcacheUtil() {
        initCache();
    }

    // 获取单例
    public static EhcacheUtil getInstance() {
        if (instance == null) {
            synchronized (EhcacheUtil.class) {
                if (instance == null) {
                    instance = new EhcacheUtil();
                }
            }
        }
        return instance;
    }

    // 初始化缓存
    private void initCache() {
        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        this.cacheManager.init();

        // 创建默认缓存（Key: String, Value: Object，可存储任意对象）
        this.objectCache = cacheManager.createCache(
                "defaultCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, String.class,
                        ResourcePoolsBuilder.heap(1000) // 堆内存存储1000条
                )
        );
    }

    /**
     * 存储对象
     * @param key   缓存键
     * @param value 缓存值（任意对象）
     */
    public void put(String key, String value) {
        objectCache.put(key, value);
    }

    /**
     * 获取对象
     * @param key 缓存键
     * @return 缓存值（需强制转换类型）
     */
    public String get(String key) {
        return objectCache.get(key);
    }

    /**
     * 删除对象
     * @param key 缓存键
     */
    public void remove(String key) {
        objectCache.remove(key);
    }

    /**
     * 清空缓存
     */
    public void clear() {
        objectCache.clear();
    }



}
