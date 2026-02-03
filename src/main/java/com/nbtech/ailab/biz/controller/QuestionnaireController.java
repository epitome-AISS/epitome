package com.nbtech.ailab.biz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.QuestionnaireEntity;
import com.nbtech.ailab.facade.QuestionnaireFacade;
import com.nbtech.ailab.vo.QuestionnaireVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.service.IQuestionnaireService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 问卷管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("questionnaire")
@Api(tags = "问卷管理")
public class QuestionnaireController {
    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private QuestionnaireFacade questionnaireFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    @RequiresRoles("manager")
    public BizResponse<PageResult<QuestionnairePageDto>> page(PageDto pageDto, QuestionnairePageDto dto) {
        PageResult<QuestionnairePageDto> page = questionnaireFacade.pageQuestionnaire(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("pagePublic")
    @ApiOperation("分页查询所有status等于OPEN的问卷")
    @RequiresRoles("manager")
    public BizResponse<PageResult<QuestionnaireDto>> pagePublic(
            PageDto pageDto,
            @ApiParam(value = "归属人（可选）") @RequestParam(required = false) String questionnaireAttribution) {
        PageResult<QuestionnaireDto> page = questionnaireService.pagePublic(pageDto, questionnaireAttribution);
        return BizResponse.success(page);
    }

    @GetMapping("pageAudit")
    @ApiOperation("审核者问卷分页")
    @RequiresRoles("manager")
    public BizResponse<PageResult<QuestionnairePageDto>> pageAudit(PageDto pageDto, QuestionnairePageDto dto) {
        PageResult<QuestionnairePageDto> page = questionnaireFacade.pageAudit(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiresRoles("manager")
    public BizResponse<List<QuestionnaireDto>> list(QuestionnaireDto dto) {
        List<QuestionnaireDto> list = questionnaireFacade.listVo(dto);
        return BizResponse.success(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresRoles("manager")
    public BizResponse<QuestionnaireDto> get(@PathVariable("id") Long id) {
        QuestionnaireDto data = questionnaireService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("设计问卷 供新建/编辑")
    @RequiresRoles("manager")
    public BizResponse<?> operateQuestionnaire(@RequestBody QuestionnaireVo vo) throws JsonProcessingException {
        questionnaireFacade.operateQuestionnaire(vo);
        return BizResponse.success();
    }

    @PutMapping("/updateExperimentPlanId")
    @ApiOperation("修改问卷的实验计划id")
    @RequiresRoles("manager")
    public BizResponse<?> updateExperimentPlanId(@RequestParam("id") Long id,
            @RequestParam("experimentPlanId") Long experimentPlanId) {
        questionnaireService.updateExperimentPlanId(id, experimentPlanId);
        return BizResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除问卷")
    @RequiresRoles("manager")
    public BizResponse<?> deleteQuestionnaire(@PathVariable("id") Long id) {
        questionnaireFacade.deleteQuestionnaire(id);
        return BizResponse.success();
    }

    @GetMapping("listAll")
    @ApiOperation("全部问卷")
    @RequiresRoles("manager")
    public BizResponse<?> listAll(QuestionnaireDto dto) {
        List<QuestionnaireDto> list = questionnaireService.listAll(dto);
        return BizResponse.success(list);
    }

    @PostMapping("flow")
    @ApiOperation("问卷状态流程操作")
    @RequiresRoles("manager")
    public BizResponse<?> questionnaireFlow(@RequestBody FlowDto dto) {
        questionnaireFacade.questionnaireFlow(dto);
        return BizResponse.success();
    }

    @PostMapping("/review")
    @ApiOperation("审核")
    @RequiresRoles("manager")
    public BizResponse<?> review(@RequestBody ReviewTestDto dto) {
        questionnaireFacade.review(dto);
        return BizResponse.success();
    }

    @GetMapping("/openList")
    @ApiOperation("开源问卷列表")
    public BizResponse<?> openList() {
        List<QuestionnaireDto> list = questionnaireService.openList();
        return BizResponse.success(list);
    }

    @PutMapping("/copyQuestionnaire")
    @ApiOperation("复制问卷")
    @RequiresRoles("manager")
    public BizResponse<QuestionnaireEntity> copyQuestionnaire(@RequestParam("id") Long id) {
        QuestionnaireEntity questionnaireEntity = questionnaireService.copyQuestionnaire(id, null);
        return BizResponse.success(questionnaireEntity);
    }

}