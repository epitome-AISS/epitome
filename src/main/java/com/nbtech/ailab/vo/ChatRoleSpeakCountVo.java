package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class ChatRoleSpeakCountVo {

    /**
     * 发言次数
     */
    private Integer speakerCount;

    /**
     * 角色类型
     */
    private String name;


    /**
     * 用户id
     */
    private Long userId;

    public ChatRoleSpeakCountVo(Integer speakerCount, String name, Long userId) {
        this.speakerCount = speakerCount;
        this.name = name;
        this.userId = userId;
    }

    public ChatRoleSpeakCountVo(Integer speakerCount, String name) {
        this.name = name;
        this.speakerCount = speakerCount;
    }
}
