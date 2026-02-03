package com.nbtech.ailab.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nber
 * 问卷分析使用到的答案类型
 */
@Getter
public enum QuestionTypeEnum {

    /**
     * 填空
     */
    FILL( 0,"fill"),

    /**
     * 简答
     */
    SHORTANSWER(1,"shortAnswer"),

    /**
     * 单选
     */
    RADIO(2,"radio"),

    /**
     * 多选
     */
    MULTIPLE(3,"multiple"),

    /**
     * 量表
     */
    SCALES(4,"scales"),

    /**
     * 排序
     */
    SORT(5,"sort"),
    /**
     * 文件上传
     */
    FILE(6,"file");

    private final Integer code;

    private final String desc;

    QuestionTypeEnum(Integer code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 创建一个Map来存储code和枚举值的映射关系
    private static final Map<Integer, QuestionTypeEnum> CODE_MAP = new HashMap<>();

    // 静态代码块，用于初始化MAP
    static {
        for (QuestionTypeEnum type : values()) {
            CODE_MAP.put(type.getCode(), type);
        }
    }

    // 通过code获取desc的方法
    public static String getDescByCode(Integer code) {
        QuestionTypeEnum type = CODE_MAP.get(code);
        return type != null ? type.getDesc() : null;
    }




}
