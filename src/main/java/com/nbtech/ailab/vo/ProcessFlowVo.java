package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProcessFlowVo {

    private String processId;
    private String processType;
    private String description;
    private Integer round;
    private Integer sort;
}
