package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.ExperimentMessageDto;
import com.nbtech.ailab.biz.service.IExperimentMessageService;
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
 * 实验组信息表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-06-11
 */
@RestController
@RequestMapping("experimentmessage")
@Api(tags = "实验组信息表")
@RequiresRoles("manager")
public class ExperimentMessageController {
    @Autowired
    private IExperimentMessageService experimentMessageService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<ExperimentMessageDto>> page(PageDto pageDto, ExperimentMessageDto dto) {
        PageResult<ExperimentMessageDto> page = experimentMessageService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public BizResponse<List<ExperimentMessageDto>> list(ExperimentMessageDto dto) {
        List<ExperimentMessageDto> list = experimentMessageService.list(dto);
        return BizResponse.success(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<ExperimentMessageDto> get(@PathVariable("id") Long id) {
        ExperimentMessageDto data = experimentMessageService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public BizResponse<?> save(@RequestBody ExperimentMessageDto dto) {
        experimentMessageService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    public BizResponse<?> update(@RequestBody ExperimentMessageDto dto) {
        experimentMessageService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
        experimentMessageService.delete(ids);
        return BizResponse.success();
    }

}