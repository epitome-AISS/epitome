package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum RoleEnvEnum {

    // 角色名称环境变量
    EPITOME_ROLE("epitome_role"),

    // 角色描述环境变量
    EPITOME_ROLE_DESCRIPTION("epitome_role_description");

    private String desc;

    RoleEnvEnum(String desc) {
        this.desc = desc;
    }
}
