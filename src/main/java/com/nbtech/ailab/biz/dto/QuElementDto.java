package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuElementDto {
    @ApiModelProperty(value = "问卷算子id")
    private String id;

    @ApiModelProperty(value = "问卷id")
    private Long questionnaireId;
}
