package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.BasicModelDto;
import com.nbtech.ailab.biz.service.IBasicModelService;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础模型表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
@RestController
@RequestMapping("basicmodel")
@Api(tags = "基础模型表")
public class BasicModelController {

    @Autowired
    private IBasicModelService basicModelService;

    @GetMapping("list")
    @ApiOperation("查询自己的模型列表")
    public BizResponse<List<BasicModelDto>> list() {
        List<BasicModelDto> list = basicModelService.getOwnerBasicModel();
        return BizResponse.success(list);
    }

    @GetMapping("page")
    @ApiOperation("基础模型分页查询")
    public BizResponse<PageResult<BasicModelDto>> page(PageDto pageDto, BasicModelDto dto) {
        PageResult<BasicModelDto> page = basicModelService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("基础模型详情查询")
    public BizResponse<BasicModelDto> get(@PathVariable("id") Long id) {
        BasicModelDto data = basicModelService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @LogOperation("保存")
    @ApiOperation("保存")
    public BizResponse<?> save(@RequestBody BasicModelDto dto) {
        basicModelService.saveBasicMode(dto);
        return BizResponse.success();
    }

    @PutMapping
    @LogOperation("修改")
    @ApiOperation("修改")
    public BizResponse<?> update(@RequestBody BasicModelDto dto) {
        basicModelService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @LogOperation("删除")
    @ApiOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
        basicModelService.deleteBasicModel(ids);
        return BizResponse.success();
    }

    @PutMapping("updateUseStatus")
    @LogOperation("修改使用状态")
    @ApiOperation("修改使用状态")
    public BizResponse<?> updateUseStatus(@RequestBody BasicModelDto dto) {
        basicModelService.updateUseStatus(dto);
        return BizResponse.success();
    }

    @PutMapping("verify/{id}")
    @LogOperation("验证基础模型可用状态")
    @ApiOperation("验证基础模型可用状态")
    public BizResponse<?> verifyBasicModelById(@PathVariable("id") Long id) {
        basicModelService.verifyBasicModelById(id);
        return BizResponse.success();
    }

    @GetMapping("judgeSameName")
    @LogOperation("判断相同的模型名称是否存在")
    @ApiOperation("判断相同的模型名称是否存在")
    public BizResponse<?> judgeSameName(@RequestParam(value = "name") String name,
            @RequestParam(value = "type") Integer type) {
        return BizResponse.success(basicModelService.judgeSameName(name, type));
    }

}