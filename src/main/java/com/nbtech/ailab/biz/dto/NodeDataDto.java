package com.nbtech.ailab.biz.dto;

import lombok.Data;

@Data
public class NodeDataDto {

    private String type;
    private String classify;
    private NodeConfigDto config;
}
