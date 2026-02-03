package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * 完成实验状态
 * @author nber
 */
@Getter
public enum CompletedStatusEnum {

    /**
     * 待完成
     */
    BEEND("beEnd"),

    /**
     * 完成
     */
    END("end");

    /**
     * 描述
     */
    private String desc;

    CompletedStatusEnum(String desc) {
        this.desc = desc;

    }
}
