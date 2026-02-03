package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class RoleSpeakCountVo {

    /**
     * 发言次数
     */
    private Integer speakerCount;

    /**
     * 发言字数
     */
    private Integer wordCount;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 是否是主持人
     */
    private Boolean isHost;
}
