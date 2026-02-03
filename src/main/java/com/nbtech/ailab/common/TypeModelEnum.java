package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum TypeModelEnum {

    /**
     * 单一模型对话
     */
    SINGLE(1),

    /**
     * 群聊对话
     */
    GROUP(2),

    /**
     * 智能教育
     */
    EDUCATION(3);

    private final Integer value;

    TypeModelEnum(Integer value) {
        this.value = value;
    }
}
