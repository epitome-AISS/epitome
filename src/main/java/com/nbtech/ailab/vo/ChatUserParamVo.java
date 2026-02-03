package com.nbtech.ailab.vo;

import lombok.Getter;

@Getter
public class ChatUserParamVo {

    /**
     * 算子 id
     */
    private String elementId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户状态
     */
    private String userStatus;
}
