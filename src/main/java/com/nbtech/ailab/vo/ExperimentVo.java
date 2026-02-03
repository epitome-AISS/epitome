package com.nbtech.ailab.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Data
@ApiModel(value = "")
public class ExperimentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "实验id")
    private Long experimentIds;

    @ApiModelProperty(value = "实验组id")
    private Long groupIds;

}