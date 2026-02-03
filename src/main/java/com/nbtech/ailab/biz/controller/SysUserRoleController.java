package com.nbtech.ailab.biz.controller;


import com.nbtech.ailab.biz.dto.SysUserRoleDto;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@RestController
@RequestMapping("/sysUserRole")
@Api(tags="角色用户关联")
public class SysUserRoleController {
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<SysUserRoleDto>> page(PageDto pageDto, SysUserRoleDto dto){
        PageResult<SysUserRoleDto> page = sysUserRoleService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<SysUserRoleDto> get(@PathVariable("id") Long id){
        SysUserRoleDto data = sysUserRoleService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody SysUserRoleDto dto){
        sysUserRoleService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody SysUserRoleDto dto){
        sysUserRoleService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids){
        sysUserRoleService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

}