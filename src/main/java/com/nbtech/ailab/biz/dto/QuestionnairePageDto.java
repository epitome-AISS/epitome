package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "问卷管理分页dto")
public class QuestionnairePageDto {
    @ApiModelProperty(value = "问卷id")
    private Long id;

    @ApiModelProperty(value = "问卷名称")
    private String questionnaireName;

    @ApiModelProperty(value = "问卷描述")
    private String questionnaireDesc;

    @ApiModelProperty(value = "问卷状态")
    private String status;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "实验计划名称")
    private String experimentPlanName;

    @ApiModelProperty(value = "实验计划标题")
    private String experimentPlanTitle;

    @ApiModelProperty(value = "实验计划状态")
    private String experimentStatus;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;
}
