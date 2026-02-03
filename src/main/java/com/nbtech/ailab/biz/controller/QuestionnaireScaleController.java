package com.nbtech.ailab.biz.controller;


import com.nbtech.ailab.biz.dto.QuestionnaireScaleDto;
import com.nbtech.ailab.biz.service.IQuestionnaireScaleService;
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
 * 问卷(量表)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@RestController
@RequestMapping("questionnairescale")
@Api(tags="问卷(量表)")
public class QuestionnaireScaleController {
    @Autowired
    private IQuestionnaireScaleService questionnaireScaleService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<QuestionnaireScaleDto>> page(PageDto pageDto, QuestionnaireScaleDto dto){
        PageResult<QuestionnaireScaleDto> page = questionnaireScaleService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<QuestionnaireScaleDto> get(@PathVariable("id") Long id){
        QuestionnaireScaleDto data = questionnaireScaleService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody QuestionnaireScaleDto dto){
        questionnaireScaleService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody QuestionnaireScaleDto dto){
        questionnaireScaleService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids){
        questionnaireScaleService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }



}