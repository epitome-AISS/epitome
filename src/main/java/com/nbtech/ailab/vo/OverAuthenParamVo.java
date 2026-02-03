package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

@Data
public class OverAuthenParamVo {

    private Long experimentId;

    private Long groupId;

    private List<AuthenticationVo> authenticationVos;
}
