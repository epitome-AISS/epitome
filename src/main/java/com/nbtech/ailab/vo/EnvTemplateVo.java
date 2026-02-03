package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class EnvTemplateVo {

    private String envName;

    private String envDes;

    private String envScope;

    private String envType;
    /**
     * 更新方式
     */
    private String updateMethod;

    // 角色名称
    private String roleName;

    // 是否展示
    private String reveal;

    private String envConfig;

    // 用来保存更新数据的字段
    private String updateStr;

    private Long roomId;

    private String elementId;

    // 回合数
    private Integer round;

}
