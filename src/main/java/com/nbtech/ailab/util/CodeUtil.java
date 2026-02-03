package com.nbtech.ailab.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CodeUtil {

    /**
     * 生成  AD20240401001 这种格式
     *
     * @param frist   前面的英文
     * @param oldCode 之前最新的代码
     */
    public static String getPlanCode(String frist, String oldCode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
        String yearDay = dateFormat.format(new Date());
        if (StringUtils.isNotBlank(oldCode) && oldCode.contains(yearDay)) {
            int lastNum = Integer.parseInt(oldCode.replace(frist + yearDay, "")) + 1;
            return frist + yearDay + getStringNum(lastNum);
        }
        return frist + yearDay + "001";
    }

    public static String getStringNum(int num) {
        if (num >= 100) {
            return String.valueOf(num);
        }
        if (num >= 10) {
            return "0" + num;
        }
        return "00" + num;
    }

    /**
     * MD5 计算出账号密码
     *
     * @param originalString 初始号码
     */
    public static String fixCode(int getNum, String originalString) throws NoSuchAlgorithmException {
        // 获取MD5摘要算法的 MessageDigest 对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        md.update(originalString.getBytes());
        // 完成哈希计算，得到结果
        byte[] digest = md.digest();
        // 将结果转换为16进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().substring(0, getNum);
    }

    /**
     * 统计这个数从零到最大整数的结果
     * 例 101
     * [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110]
     * 100
     * [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
     *
     * @param target
     * @return
     */
    public static List<Integer> generateMultiplesOfTen(int target) {
        if (target % 10 >= 1) {
            target = (target / 10 + 1) * 10;
        } else {
            target = (target / 10) * 10;
        }
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i <= target; i += 10) {
            resultList.add(i);
        }
        System.out.println(resultList);
        return resultList;
    }


    /**
     * 把token包装成需要的形式
     */
    public static List<String> packingToken(String token) {
        String newToken = String.format("Bearer %s", token);
        return new ArrayList<String>() {{
            add(newToken);
        }};
    }

    public static String getTenCode(){
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            codeBuilder.append(chars.charAt(index));
        }

        return codeBuilder.toString();
    }
}
