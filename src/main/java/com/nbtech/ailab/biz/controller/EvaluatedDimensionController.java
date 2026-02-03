package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.EvaluatedDimensionDto;
import com.nbtech.ailab.biz.service.IEvaluatedDimensionService;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 受测用户维度
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@RestController
@RequestMapping("evaluateddimension")
@Api(tags = "受测用户维度")
@RequiresRoles("manager")
public class EvaluatedDimensionController {
    @Autowired
    private IEvaluatedDimensionService evaluatedDimensionService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<EvaluatedDimensionDto>> page(PageDto pageDto, EvaluatedDimensionDto dto) {
        PageResult<EvaluatedDimensionDto> page = evaluatedDimensionService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<EvaluatedDimensionDto> get(@PathVariable("id") Long id) {
        EvaluatedDimensionDto data = evaluatedDimensionService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody EvaluatedDimensionDto dto) {
        evaluatedDimensionService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody EvaluatedDimensionDto dto) {
        evaluatedDimensionService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        evaluatedDimensionService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

}