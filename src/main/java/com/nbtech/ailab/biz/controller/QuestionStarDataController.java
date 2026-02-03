package com.nbtech.ailab.biz.controller;

import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.QuestionStarDataDto;
import com.nbtech.ailab.biz.service.IQuestionStarDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问卷星的问卷答题结果
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-27
 */
@RestController
@RequestMapping("biz/questionstardata")
@Api(tags = "问卷星的问卷答题结果")
public class QuestionStarDataController {

    @Autowired
    private IQuestionStarDataService questionStarDataService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<QuestionStarDataDto>> page(PageDto pageDto, QuestionStarDataDto dto) {
        PageResult<QuestionStarDataDto> page = questionStarDataService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<QuestionStarDataDto> get(@PathVariable("id") Long id) {
            QuestionStarDataDto data = questionStarDataService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody QuestionStarDataDto dto) {
            questionStarDataService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody QuestionStarDataDto dto) {
            questionStarDataService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
            questionStarDataService.delete(ids);
        return BizResponse.success();
    }

}