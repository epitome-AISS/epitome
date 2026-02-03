package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class ApiParamVo {
    private Object inputs;

    private String response_mode="streaming";

    private String user ="abc-123";
}
