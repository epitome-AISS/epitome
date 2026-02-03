package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.dto.WordQuestionnaireDto;
import com.nbtech.ailab.biz.dto.WordTypeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("问卷数据统计看板vo")
public class QuestionnaireBoardVo {
    @ApiModelProperty("单选 多选 量表类型题目中选项数据汇总")
    private List<WordQuestionnaireDto> wordQuestionnaires;

    @ApiModelProperty(value = "题目名称")
    private String questionName;

    @ApiModelProperty(value = "题目类型")
    private String questionType;

    @ApiModelProperty(value = "题目顺序")
    private Long sort;

    @ApiModelProperty("总数")
    private int totalNum;

    @ApiModelProperty(value = "有效人数")
    private int effectiveNum;

    @ApiModelProperty("文字类型题目回答数据汇总")
    private List<WordTypeDto> contexts;

    @ApiModelProperty(value = "词云分析路径")
    private String url;

}
