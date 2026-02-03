package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "审核dto")
public class ReviewTestDto {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "审核是否通过")
    private Integer isReview;
}
