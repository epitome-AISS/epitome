package com.nbtech.ailab.biz.controller;

import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.QuestionStarDto;
import com.nbtech.ailab.biz.service.IQuestionStarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问卷星问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-28
 */
@RestController
@RequestMapping("biz/questionstar")
@Api(tags = "问卷星问卷数据")
public class QuestionStarController {

    @Autowired
    private IQuestionStarService questionStarService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<QuestionStarDto>> page(PageDto pageDto, QuestionStarDto dto) {
        PageResult<QuestionStarDto> page = questionStarService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<QuestionStarDto> get(@PathVariable("id") Long id) {
            QuestionStarDto data = questionStarService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody QuestionStarDto dto) {
            questionStarService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody QuestionStarDto dto) {
            questionStarService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
            questionStarService.delete(ids);
        return BizResponse.success();
    }

}