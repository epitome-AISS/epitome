package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class RollbackWorkflowVo {

    private String elementId;

    private String processId;

    private String workId;

    private String flowId;

    private String useUserId;

    private Long round;
}
