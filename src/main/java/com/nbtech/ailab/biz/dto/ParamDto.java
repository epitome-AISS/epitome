package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("问卷算子是否被发布dto")
public class ParamDto {
    private Long experimentId;

    private Long elementId;

    private String createName;
}
