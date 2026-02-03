package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * 权限角色类别
 */
@Getter
public enum AuthRoleEnum {

    /**
     * 本来是有好几种角色 但是目前角色就两类 一类实验者 一类管理者 非实验者都是管理者 所以才有这么个类
     */

    /**
     * 后台管理员角色
     */
    MANAGER("manager"),

    /**
     * 实验者角色
     */
    EXPERIMENTER("experimenter");

    /**
     * 描述
     */
    private String desc;

    AuthRoleEnum(String desc) {
        this.desc = desc;
    }
}
