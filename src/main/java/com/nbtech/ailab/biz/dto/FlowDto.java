package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "操作流程传入dto")
public class FlowDto {
    @ApiModelProperty(value = "问卷/素材/模型id")
    private Long id;

    @ApiModelProperty(value = "操作流程")
    private String workFlow;

    @ApiModelProperty(value = "实验计划id")
    private Long experimentPlanId;
}
