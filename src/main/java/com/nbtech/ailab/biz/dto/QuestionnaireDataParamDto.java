package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("问卷数据分析传参dto")
public class QuestionnaireDataParamDto {
    @ApiModelProperty(value = "实验组id")
    private Long groupsId;

    @ApiModelProperty(value = "问卷id")
    private Long questionnaireId;

    @ApiModelProperty(value = "实验计划id")
    private Long experimentId;

    @ApiModelProperty(value = "算子id")
    private String elementId;

}
