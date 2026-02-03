package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("单选 多选 量表类型题目中选项数据汇总")
public class WordQuestionnaireDto {
    @ApiModelProperty(value = "选项名称")
    private String optionName;

    @ApiModelProperty(value = "各个选项对应小计")
    private Long countNum;

    @ApiModelProperty(value = "选项顺序")
    private Long optionSort;

}
