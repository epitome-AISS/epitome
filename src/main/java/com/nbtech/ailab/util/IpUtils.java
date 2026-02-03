package com.nbtech.ailab.util;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.File;
import java.io.IOException;

/**
 * 通过ip找到地址路径
 * @author nber
 */
@Slf4j
public class IpUtils {

    public static String getAddress(String ip) {
        String address = "未知";
        try {
            // jar路径
            String projectPath = System.getProperty("user.dir");
            // 文件在线上环境的地址
            String dbPath = projectPath + "/util/ip2region.xdb";
            // 1、创建 searcher 对象
            Searcher searcher = Searcher.newWithFileOnly(dbPath);
            // 2、查询
            String region = searcher.search(ip);
            log.info("获取到的地址" + region);
            String[] addressArr = region.split("\\|");
            address = addressArr[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

}



