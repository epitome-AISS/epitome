package com.nbtech.ailab.biz.dto;

import lombok.Data;

@Data
public class EdgeDto {

    private String id;
    private String type;
    private String source;
    private String target;
    private EdgeStyleDto style;
    private MarkerEndDto markerEnd;
    private String sourceHandle;
    private String targetHandle;
    private EdgeDataDto data;
    private boolean selected;

}
