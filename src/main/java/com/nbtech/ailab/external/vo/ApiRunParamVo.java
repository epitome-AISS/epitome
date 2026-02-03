package com.nbtech.ailab.external.vo;

import lombok.Data;


import java.util.List;

@Data
public class ApiRunParamVo {

    private String elementId;

    private String input;

    private String flowId;

    // 运行工作流的场景
    private String runType;

    // 工作流的类型
    private String workflowType;

    private Integer round;

    private List<Object> files;

    // 创建工作流的用户id
    private Long userId;

    // 实验组的id
    private Long groupId;

    // 使用工作流的用户id
    private Long useUserId;

    private String workId;

    private String processId;

    private Boolean saveResult = true;

}
