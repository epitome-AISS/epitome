package com.nbtech.ailab.biz.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcessDto {

    private String description;
    private String processType;
    private String processDag;
    private String processId;
    private List<ProcessSettingDto> processConfig;
    private Integer sort;
    private Integer round;
}
