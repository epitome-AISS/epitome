package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class EdgeVo {
    public Object style;
    public String type;
    public Object markerEnd;
    public String source;
    public String sourceHandle;
    public String target;
    public String targetHandle;
    public String id;
    public Boolean selected;
}
