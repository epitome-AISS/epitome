package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.ModelEntity;
import com.nbtech.ailab.biz.service.IModelService;
import com.nbtech.ailab.facade.ModelFacade;
import com.nbtech.ailab.vo.TagVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 模型对话管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-11
 */
@RestController
@RequestMapping("model")
@Api(tags = "模型对话管理")

public class ModelController {
    @Autowired
    private IModelService modelService;

    @Autowired
    private ModelFacade modelFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    @RequiresRoles("manager")
    public BizResponse<PageResult<ModelDto>> page(PageDto pageDto, ModelDto dto) {
        PageResult<ModelDto> page = modelService.pageModel(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("pagePublic")
    @ApiOperation("分页查询所有status等于OPEN的模型")
    @RequiresRoles("manager")
    public BizResponse<PageResult<ModelDto>> pagePublic(
            PageDto pageDto,
            @ApiParam(value = "归属人（可选）") @RequestParam(required = false) String attribution) {
        PageResult<ModelDto> page = modelService.pagePublic(pageDto, attribution);
        return BizResponse.success(page);
    }

    @GetMapping("pageAudit")
    @ApiOperation("审核者模型对话分页")
    @RequiresRoles("manager")
    public BizResponse<PageResult<ModelDto>> pageAudit(PageDto pageDto, ModelDto dto) {
        PageResult<ModelDto> page = modelService.pageAudit(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiresRoles("manager")
    public BizResponse<List<TagVo>> list(ModelDto dto) {
        return BizResponse.success(modelService.listVo(dto));
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("信息")
    @RequiresRoles("manager")
    public BizResponse<ModelDto> get(@PathVariable("id") Long id) {
        ModelDto data = modelService.get(id);
        return BizResponse.success(data);
    }

    @PutMapping("copyModel")
    @ApiOperation("模型复制")
    public BizResponse<ModelEntity> copyModel(@RequestParam(value = "id") Long id) {
        ModelEntity modelEntity = modelService.copyModel(id, null);
        return BizResponse.success(modelEntity);
    }

    @PostMapping
    @ApiOperation("操作模型 供新建/修改")
    public BizResponse<?> save(@RequestBody ModelDto vo) throws Exception {
        modelFacade.operateModel(vo);
        return BizResponse.success();
    }

    @PostMapping("/flow")
    @ApiOperation("模型流程操作")
    @RequiresRoles("manager")
    public BizResponse<?> flow(@RequestBody FlowDto dto) {
        modelFacade.flow(dto);
        return BizResponse.success();
    }

    @GetMapping("listAll")
    @ApiOperation("全部模型对话")
    @RequiresRoles("manager")
    public BizResponse<?> listAll(ModelDto dto) {
        List<ModelDto> list = modelService.listAll(dto);
        return BizResponse.success(list);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除模型对话")
    @RequiresRoles("manager")
    public BizResponse<?> deleteModel(@PathVariable("id") Long id) {
        modelFacade.deleteModel(id);
        return BizResponse.success();
    }

    @PostMapping("/review")
    @ApiOperation("审核")
    @RequiresRoles("manager")
    public BizResponse<?> review(@RequestBody ReviewTestDto dto) {
        modelFacade.review(dto);
        return BizResponse.success();
    }

    @GetMapping("getById")
    @ApiOperation("免登录获取模型信息")
    public BizResponse<ModelDto> getById(@RequestParam("id") Long id) {
        return BizResponse.success(modelService.getById(id));
    }

    @GetMapping("getModelTags")
    @ApiOperation("获取所有模型的tag")
    public BizResponse<?> getModelTags() {
        return BizResponse.success(modelService.getModelTags());
    }

    @GetMapping("/openList")
    @ApiOperation("开源模型对话列表")
    public BizResponse<?> openList(Integer modelBotType) {
        List<ModelDto> list = modelService.openList(modelBotType);
        return BizResponse.success(list);
    }

}