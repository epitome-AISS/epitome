package com.nbtech.ailab.external.vo;

import lombok.Data;

import java.util.List;

@Data
public class RunParamVo {
    private Object inputs;

    private List<Object> files;
}
