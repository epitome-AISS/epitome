package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class QnStartLoginParamVo {

    private String appId;

    private String ts;

    private String sign;

    private String subUser;
}
