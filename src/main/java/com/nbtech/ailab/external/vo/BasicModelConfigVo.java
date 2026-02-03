package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class BasicModelConfigVo {

    private String model;

    private String model_type;

    private CredentialsVo credentials;

    private LoadBalancingVo load_balancing;

}
