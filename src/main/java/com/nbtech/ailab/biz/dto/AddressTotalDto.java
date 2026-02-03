package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("地域分布dto")
public class AddressTotalDto {
    @ApiModelProperty(value = "地域名称")
    private String name;

    @ApiModelProperty(value = "地域百分比")
    private Double perCent;

    @ApiModelProperty(value = "各个地域数量")
    private Long number;
}
