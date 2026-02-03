package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.SceneDto;
import com.nbtech.ailab.biz.service.ISceneService;
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
 * 场景
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("group/scene")
@Api(tags="场景")
@RequiresRoles("manager")
public class SceneController {
    @Autowired
    private ISceneService sceneService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<SceneDto>> page(PageDto pageDto, SceneDto dto){
        PageResult<SceneDto> page = sceneService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<SceneDto> get(@PathVariable("id") Long id){
        SceneDto data = sceneService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody SceneDto dto){
        sceneService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody SceneDto dto){
        sceneService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids){
        sceneService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

    @GetMapping("getSceneList")
    @ApiOperation("查询所有的实验场景")
    public BizResponse<?> getSceneList(){
        return BizResponse.success(sceneService.getSceneList());
    }


}