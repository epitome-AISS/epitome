package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.dto.QuestionDto;
import com.nbtech.ailab.biz.dto.QuestionnaireDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("问卷vo")
@Data
public class QuestionnaireVo extends QuestionnaireDto {
    @ApiModelProperty(value = "题目集合")
    private List<QuestionDto> questions;

}
