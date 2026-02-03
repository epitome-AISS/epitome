package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.dto.AnswerDto;
import com.nbtech.ailab.biz.dto.QuestionnaireDataDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("问卷数据vo")
public class QuestionnaireDataVo extends QuestionnaireDataDto {
    @ApiModelProperty(value = "回答集合")
    private List<AnswerDto> answers;

    @ApiModelProperty(value = "问卷id")
    private Long questionnaireId;
}
