package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class QnDataParamVo {

    private String appId;

    private String activity;

    private String ts;

    private String sign;

    private Integer pageIndex;

    private Integer pageSize;
}
