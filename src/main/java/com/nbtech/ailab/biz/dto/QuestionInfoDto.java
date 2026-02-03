package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("问题信息dto")
public class QuestionInfoDto {
    @ApiModelProperty(value = "选项答案")
    private String choices;

    @ApiModelProperty(value = "量表答案")
    private String scales;

    @ApiModelProperty(value = "题目顺序")
    private Long questionSort;

    @ApiModelProperty(value = "题目名称")
    private String questionName;

    @ApiModelProperty(value = "题目类型")
    private String questionType;


}
