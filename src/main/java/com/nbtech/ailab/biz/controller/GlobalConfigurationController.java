package com.nbtech.ailab.biz.controller;

import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.GlobalConfigurationDto;
import com.nbtech.ailab.biz.service.IGlobalConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 全局配置表
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-08
 */
@RestController
@RequestMapping("globalconfiguration")
@Api(tags = "全局配置表")
public class GlobalConfigurationController {
    @Autowired
    private IGlobalConfigurationService globalConfigurationService;

    @ApiOperation("全局启用禁用审核")
    @PostMapping("enableReview/{isEnable}")
    public BizResponse<?> enableReview(@PathVariable("isEnable") Integer isEnable) {
        globalConfigurationService.enableReview(isEnable);
        return BizResponse.success();
    }

    @ApiOperation("获取当前全局审核禁用启用状态")
    @GetMapping("/getStatus")
    public BizResponse<?> getStatus() {
        GlobalConfigurationDto globalConfigurationDto = globalConfigurationService.get(1L);
        return BizResponse.success(globalConfigurationDto.getIsEnableReview());
    }


}