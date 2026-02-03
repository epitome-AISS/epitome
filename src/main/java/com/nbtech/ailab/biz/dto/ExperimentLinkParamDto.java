package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生成实验链接参数
 */
@Data
public class ExperimentLinkParamDto {
    @ApiModelProperty(value = "实验组id")
    private Long groupsId;

    @ApiModelProperty(value = "生成个数")
    private Integer produceNum;
}
