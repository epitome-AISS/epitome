package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@Data
@ApiOperation("题目")
public class QuestionDto {
    @ApiModelProperty(value = "题目顺序")
    private Long questionSort;

    @ApiModelProperty(value = "题目名称")
    private String questionName;

    @ApiModelProperty(value = "题目id")
    private Long id;

    @ApiModelProperty(value = "题目类型")
    private String questionType;

    @ApiModelProperty(value = "选项集合")
    private List<ChoiceDto> choices;

    @ApiModelProperty(value = "量表集合")
    private List<ScaleDto> scales;

    @ApiModelProperty(value = "是否需要为必填选项")
    private Integer isMust;

    @ApiModelProperty(value = "条件")
    private String conditions;

}
