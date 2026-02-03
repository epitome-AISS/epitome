package com.nbtech.ailab.biz.controller;


import com.nbtech.ailab.biz.dto.QuestionnaireSelectionDto;
import com.nbtech.ailab.biz.service.IQuestionnaireSelectionService;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 问卷(选择题)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@RestController
@RequestMapping("questionnaireselection")
@Api(tags="问卷(选择题)")
public class QuestionnaireSelectionController {
    @Autowired
    private IQuestionnaireSelectionService questionnaireSelectionService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<QuestionnaireSelectionDto>> page(PageDto pageDto, QuestionnaireSelectionDto dto){
        PageResult<QuestionnaireSelectionDto> page = questionnaireSelectionService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<QuestionnaireSelectionDto> get(@PathVariable("id") Long id){
        QuestionnaireSelectionDto data = questionnaireSelectionService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody QuestionnaireSelectionDto dto){
        questionnaireSelectionService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody QuestionnaireSelectionDto dto){
        questionnaireSelectionService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids){
        questionnaireSelectionService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }


}