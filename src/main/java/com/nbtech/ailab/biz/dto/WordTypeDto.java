package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "文字类型题目回答数据汇总")
public class WordTypeDto {
    @ApiModelProperty(value = "文字名称")
    private String wordName;

    @ApiModelProperty("文字名称对应数量")
    private Long wordNum;
}
