package com.nbtech.ailab.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 问卷统计数据
 *
 * @author nber
 */
@Data
@ApiModel(value = "问卷统计数据")
public class QuestionnaireStatisticsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问卷id")
    private Long questionnaireId;

    @ApiModelProperty(value = "问卷名称")
    private String questionnaireName;

    @ApiModelProperty(value = "题目类型占比（key为题目类型，value为百分比）")
    private Map<String, BigDecimal> questionTypePercentages;

    @ApiModelProperty(value = "有效答题数量")
    private Long validAnswerCount;

    @ApiModelProperty(value = "平均答题时间（分钟）")
    private BigDecimal averageAnswerTimeMinutes;
}

