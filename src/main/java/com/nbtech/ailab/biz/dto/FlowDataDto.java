package com.nbtech.ailab.biz.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlowDataDto {

    private List<EdgeDto> edges;
    private List<NodeDto> nodes;
}