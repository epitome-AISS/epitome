package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * @author nber
 */

@Getter
public enum UserStatusEnum {
    /**
     * 启用
     */
    ENABLE( 1),

    /**
     * 禁用
     */
    FORBID(0);

    private Integer desc;
    UserStatusEnum(Integer desc) {
        this.desc = desc;
    }
}
