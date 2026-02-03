package com.nbtech.ailab.common;

import lombok.Data;
import lombok.Getter;

/**
 * 实验持有状态
 * @author nber
 */
@Getter
public enum HoldStatusEnum {

    /**
     * 私有
     */
    PRIVATE("private"),

    /**
     * 开源
     */
    PUBLIC("public");

    /**
     * 描述
     */
    private String desc;

    HoldStatusEnum(String desc) {
        this.desc = desc;

    }
}
