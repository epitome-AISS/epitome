package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("答案dto")
public class AnswerDto {
    @ApiModelProperty(value = "答案顺序")
    private Long answerSort;

    @ApiModelProperty(value = "答案内容")
    private String answerContext;

    @ApiModelProperty(value = "答案id")
    private Long id;

    @ApiModelProperty(value = "选项答案")
    private List<ChoiceDto> choices;

    @ApiModelProperty(value = "量表答案")
    private ScaleDto scale;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

}
