package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * @author nber
 */
@Getter
public enum ModelRoleEnum {
    /**
     * 模型角色
     */
    ASSISTANT( "assistant"),

    /**
     * 用户角色
     */
    USER("user");


    private String desc;
    ModelRoleEnum(String desc) {
        this.desc = desc;
    }
}
