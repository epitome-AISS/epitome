package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("回答jsonDto")
public class AnswerJsonDto {
    @ApiModelProperty(value = "答案顺序")
    private Long answerSort;

    @ApiModelProperty(value = "答案内容")
    private String answerContext;

    @ApiModelProperty(value = "选项答案")
    private String choiceContext;

    @ApiModelProperty(value = "选项内容")
    private String choices;

    @ApiModelProperty(value = "选项顺序")
    private Long choiceSort;

    @ApiModelProperty(value = "量表答案")
    private String scale;

    @ApiModelProperty(value = "量表答案")
    private String scaleContext;

    @ApiModelProperty(value = "量表等级")
    private Long scaleGrade;

}
