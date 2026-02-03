package com.nbtech.ailab.biz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.dto.QuestionnaireDataDto;
import com.nbtech.ailab.biz.service.IQuestionnaireDataService;
import com.nbtech.ailab.facade.QuestionnaireFacade;
import com.nbtech.ailab.vo.QuestionnaireDataVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("questionnairedata")
@Api(tags = "问卷数据")
public class QuestionnaireDataController {

    @Autowired
    private IQuestionnaireDataService questionnaireDataService;

    @Autowired
    private QuestionnaireFacade questionnaireFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<QuestionnaireDataDto>> page(PageDto pageDto, QuestionnaireDataDto dto) {
        PageResult<QuestionnaireDataDto> page = questionnaireDataService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public BizResponse<List<QuestionnaireDataDto>> list(QuestionnaireDataDto dto) {
        List<QuestionnaireDataDto> list = questionnaireDataService.list(dto);
        return BizResponse.success(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<QuestionnaireDataDto> get(@PathVariable("id") Long id) {
        QuestionnaireDataDto data = questionnaireDataService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping("/saveData")
    @ApiOperation("保存答案")
    public BizResponse<?> save(@RequestBody QuestionnaireDataVo vo) throws Exception {
        questionnaireFacade.saveQuestionnaireData(vo);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    public BizResponse<?> update(@RequestBody QuestionnaireDataDto dto) {
        questionnaireDataService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
        questionnaireDataService.delete(ids);
        return BizResponse.success();
    }

}