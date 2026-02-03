package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.QuestionnaireDataParamDto;
import com.nbtech.ailab.biz.dto.QuestionnaireDto;
import com.nbtech.ailab.facade.DataCollectionFacade;
import com.nbtech.ailab.facade.QuestionnaireFacade;
import com.nbtech.ailab.vo.QuestionnaireBoardVo;
import com.nbtech.ailab.vo.QuestionnaireStatisticsVo;
import com.nbtech.common.model.BizResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("QuestionnaireBoard")
@Api(tags = "问卷数据看板")
@RequiresRoles("manager")
public class QuestionnaireBoardController {
    @Autowired
    private DataCollectionFacade dataCollectionFacade;

    @Autowired
    private QuestionnaireFacade questionnaireFacade;

    @GetMapping("/data")
    @ApiOperation("问卷数据分析")
    public BizResponse<?> getQuestionnaireDataTotal(QuestionnaireDataParamDto dto) throws Exception {
        List<QuestionnaireBoardVo> list = dataCollectionFacade.getQuestionnaireDataTotal(dto);
        return BizResponse.success(list);
    }

    @GetMapping("/questionnaire/{groupId}")
    @ApiOperation("问卷数据回显信息")
    public BizResponse<?> getQuestionnaireEcho(@PathVariable("groupId") Long groupId) {
        List<QuestionnaireDto> list = questionnaireFacade.getEcho(groupId);
        return BizResponse.success(list);
    }

    @GetMapping("/statistics")
    @ApiOperation("获取实验计划下实验组的问卷统计数据（每个问卷一条记录）")
    public BizResponse<?> getQuestionnaireStatistics(QuestionnaireDataParamDto dto) {
        List<QuestionnaireStatisticsVo> statisticsList = dataCollectionFacade.getQuestionnaireStatistics(dto);
        return BizResponse.success(statisticsList);
    }
}
