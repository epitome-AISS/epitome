package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "量表dto")
public class ScaleDto {
    @ApiModelProperty(value = "量表等级")
    private Long scaleGrade;

    @ApiModelProperty(value = "量表内容")
    private String scaleContext;

    @ApiModelProperty(value = "量表id")
    private Long id;
}
