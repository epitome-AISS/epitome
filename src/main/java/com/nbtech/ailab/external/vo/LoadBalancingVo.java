package com.nbtech.ailab.external.vo;

import lombok.Data;

import java.util.List;

@Data
public class LoadBalancingVo {
    private boolean enabled;
    private List<Object> configs;
}
