package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * @author nber
 */

@Getter
public enum ElementTypeEnum {
    /**
     * 模型
     */
    MODEL("dialogue"),

    /**
     *
     */
    CONSENT("consent"),

    /**
     * 指导语
     */
    INSTRUCTION("instruction"),

    /**
     * 展示结构
     */
    PLATE("plate"),

    /**
     * 干预
     */
    INTERVENE("intervene"),
    /**
     * 问卷
     */
    COLLECTION("collection"),

    /**
     * 问卷星问卷
     */
    QUESTION_STAR("question_star"),

    /**
     * 合作测评算子
     *
     */
    COOPERATIVE("cooperative"),

    /**
     * 身份验证算子
     *
     */
    AUTHENTICATION("authentication"),
    /**
     * 下一算子不存在
     */
    NULLELEMENT("000000");

    /**
     * 描述
     */
    private String desc;

    ElementTypeEnum(String desc) {
        this.desc = desc;

    }

    public static ElementTypeEnum fromString(String str) {
        for (ElementTypeEnum elementType : ElementTypeEnum.values()) {
            if (elementType.getDesc().equals(str)) {
                return elementType;
            }
        }
        return NULLELEMENT;
    }
}
