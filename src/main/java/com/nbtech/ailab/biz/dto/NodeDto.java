package com.nbtech.ailab.biz.dto;

import lombok.Data;

@Data
public class NodeDto {

    private String id;
    private String type;
    private NodeDataDto data;
    private PositionDto position;
    private PositionDto positionAbsolute;
    private int width;
    private int height;
    private boolean selected;
    private boolean dragging;

}
