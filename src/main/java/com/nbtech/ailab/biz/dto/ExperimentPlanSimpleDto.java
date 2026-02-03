package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实验计划简单信息DTO（仅包含id、名称、标题、创建时间）
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Data
@ApiModel(value = "实验计划简单信息DTO")
public class ExperimentPlanSimpleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "实验计划id")
    private Long id;

    @ApiModelProperty(value = "实验名称")
    private String experimentName;

    @ApiModelProperty(value = "实验标题")
    private String experimentTitle;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;
}

