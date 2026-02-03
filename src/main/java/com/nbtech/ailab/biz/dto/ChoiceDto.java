package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "选项dto")
public class ChoiceDto {
    @ApiModelProperty(value = "选项排序")
    private Long choiceSort;

    @ApiModelProperty(value = "选项内容")
    private String choiceContext;

    @ApiModelProperty(value = "验证内容")
    public String verificationContext;

    @ApiModelProperty(value = "选项id")
    private Long id;

    @ApiModelProperty(value = "是否可修改")
    private Boolean isChange;
}
