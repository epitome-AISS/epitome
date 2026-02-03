package com.nbtech.ailab.biz.controller;


import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.service.ISysUserService;

import com.nbtech.ailab.facade.AccountFacade;
import com.nbtech.ailab.facade.DataCollectionFacade;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = "用户")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private DataCollectionFacade dataCollectionFacade;

    @Autowired
    private AccountFacade accountFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<SysUserDto>> page(PageDto pageDto, SysUserDto dto) {
        PageResult<SysUserDto> page = sysUserService.getPage(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<SysUserDto> get(@PathVariable("id") Long id) {
        SysUserDto data = sysUserService.getInfo(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody SysUserVo vo) {
        sysUserService.saveUser(vo);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody SysUserVo vo) {
        sysUserService.updateUser(vo);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        sysUserService.deleteUser(Arrays.asList(ids));
        return BizResponse.success();
    }


    @GetMapping("getUserInfo")
    @ApiOperation("根据session获取用户信息")
    @LogOperation("根据session获取用户信息")
    public BizResponse<?> getUserInfo() {
        return BizResponse.success(sysUserService.getUserInfo());
    }

    @GetMapping("forbidUser")
    @ApiOperation("用户启用和禁用")
    @LogOperation("用户启用和禁用")
    public BizResponse<?> forbidUser(@ApiParam("用户Id") @RequestParam("id") Long id,
                                     @ApiParam("false 禁用 true 启用") @RequestParam("status") Boolean status) {
        sysUserService.forbidUser(id, status);
        return BizResponse.success();
    }

    /**
     * 解析实验者做实验地址
     */
    @GetMapping("getAddress")
    @ApiOperation("解析实验者ip地域到实验者自己")
    @LogOperation("解析实验者ip地域到实验者自己")
    BizResponse<?> getAddress(String ip) {
        dataCollectionFacade.getAddress(ip);
        return BizResponse.success();
    }

    @PostMapping("reset/password")
    @ApiOperation("重置密码")
    @RequiresRoles("manager")
    public BizResponse<?> resetPassword(@RequestBody SysUserDto dto) {
        accountFacade.resetPassword(dto);
        return BizResponse.success();
    }

}