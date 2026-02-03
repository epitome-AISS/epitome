package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class NodeVo {
    public String id;
    public DataVo data;
    public String type;
    public Object position;
    public Integer width;
    public Integer height;
    public Boolean selected;
    public Object positionAbsolute;
    public Boolean dragging;
}
