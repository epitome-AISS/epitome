package com.nbtech.ailab.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏类型枚举
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Getter
public enum FavoriteTypeEnum {

    /**
     * 实验计划
     */
    EXPERIMENT_PLAN("EXPERIMENT_PLAN", "实验计划"),

    /**
     * 素材
     */
    MATERIAL("MATERIAL", "素材"),

    /**
     * 智能体
     */
    AGENT("AGENT", "智能体"),

    /**
     * 问卷
     */
    QUESTIONNAIRE("QUESTIONNAIRE", "问卷");

    /**
     * 类型值
     */
    private String value;

    /**
     * 描述
     */
    private String desc;

    FavoriteTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 创建一个Map来存储value和枚举值的映射关系
     */
    private static final Map<String, FavoriteTypeEnum> VALUE_MAP = new HashMap<>();

    /**
     * 静态代码块，用于初始化MAP
     */
    static {
        for (FavoriteTypeEnum type : values()) {
            VALUE_MAP.put(type.getValue(), type);
        }
    }

    /**
     * 根据value获取枚举
     *
     * @param value 类型值
     * @return 枚举对象，如果不存在返回null
     */
    public static FavoriteTypeEnum getByValue(String value) {
        return VALUE_MAP.get(value);
    }

    /**
     * 根据value获取描述
     *
     * @param value 类型值
     * @return 描述，如果不存在返回null
     */
    public static String getDescByValue(String value) {
        FavoriteTypeEnum type = VALUE_MAP.get(value);
        return type != null ? type.getDesc() : null;
    }
}
