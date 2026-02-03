package com.nbtech.ailab.biz.controller;


import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.service.ISysRoleService;
import com.nbtech.ailab.common.AuthRoleEnum;
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
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@RestController
@RequestMapping("/sysRole")
@Api(tags="角色")
@RequiresRoles("manager")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<SysRoleDto>> page(PageDto pageDto, SysRoleDto dto){
        PageResult<SysRoleDto> page = sysRoleService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<SysRoleDto> get(@PathVariable("id") Long id){
        SysRoleDto data = sysRoleService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody SysRoleDto dto){
        sysRoleService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody SysRoleDto dto){
        sysRoleService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids){
        sysRoleService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

    @GetMapping("getRoleList")
    @ApiOperation("查询所有角色")
    @LogOperation("查询所有角色")
    public BizResponse<?> delete(@RequestParam(value = "roleName",required = false) String roleName){
        return BizResponse.success(sysRoleService.getRoleList(roleName));
    }

}