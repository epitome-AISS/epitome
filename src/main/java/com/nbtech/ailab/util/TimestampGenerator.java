package com.nbtech.ailab.util;

import java.util.concurrent.TimeUnit;

public class TimestampGenerator {

    private long timestamp; // 存储生成的时间戳（秒）
    private long expirationTime; // 过期时间（毫秒）

    /**
     * 生成一个有效时间为60秒的时间戳
     */
    public void generateTimestamp() {
        // 获取当前时间戳（秒）
        long currentTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        this.timestamp = currentTimeInSeconds;
        // 设置过期时间（当前时间 + 60秒）
        this.expirationTime = System.currentTimeMillis() + 60000;
    }

    /**
     * 检查时间戳是否有效
     * @return true如果时间戳未过期，否则false
     */
    public boolean isValid() {
        return System.currentTimeMillis() <= expirationTime;
    }

    /**
     * 获取生成的时间戳
     * @return 时间戳（秒）
     */
    public long getTimestamp() {
        return timestamp;
    }


    // 使用方法
//    public static void main(String[] args) {
//        TimestampGenerator generator = new TimestampGenerator();
//
//        // 生成时间戳
//        generator.generateTimestamp();
//        System.out.println("生成的时间戳: " + generator.getTimestamp());
//        System.out.println("当前时间戳是否有效: " + generator.isValid());
//
//        // 模拟等待60秒后检查
//        // 注意：实际运行时需要等待60秒，这里仅作演示
//        try {
//            Thread.sleep(60000); // 等待60秒
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("60秒后时间戳是否有效: " + generator.isValid());
//    }
}
