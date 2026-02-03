package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("基础模型")
public class BasicDto {
    @ApiModelProperty(value = "基础模型名称")
    private String name;

    @ApiModelProperty(value = "概率")
    private BigDecimal probability;
}
