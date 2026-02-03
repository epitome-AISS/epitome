package com.nbtech.ailab.biz.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcessSettingDto {

    private String workId;
    private String flowId;
    private String flowType;
    private List<String> roleSet;
    private List<String> needFront;
    private List<String> foreNodes;
    private List<String> backNodes;
    private String techType;
    private String userId;
    private String flowConfig;

}
