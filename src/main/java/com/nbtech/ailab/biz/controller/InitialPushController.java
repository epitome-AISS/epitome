package com.nbtech.ailab.biz.controller;

import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.InitialPushDto;
import com.nbtech.ailab.biz.service.IInitialPushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 初始化推送数据表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@RestController
@RequestMapping("biz/initialpush")
@Api(tags = "初始化推送数据表")
public class InitialPushController {

    @Autowired
    private IInitialPushService initialPushService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<InitialPushDto>> page(PageDto pageDto, InitialPushDto dto) {
        PageResult<InitialPushDto> page = initialPushService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<InitialPushDto> get(@PathVariable("id") Long id) {
            InitialPushDto data = initialPushService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody InitialPushDto dto) {
            initialPushService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody InitialPushDto dto) {
            initialPushService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
            initialPushService.delete(ids);
        return BizResponse.success();
    }

}