package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class CooperativeElementVo {

    /**
     * 标题
     */
    private String title;

    /**
     * 场景
     */
    private String scene;

    /**
     * 用户数量
     */
    private Integer userCount;

    /**
     * 角色
     */
    private String role;

    /**
     * 对话ID
     */
    private Long dialogueId;
}
