package com.nbtech.ailab.biz.controller;

import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.SelectPushDto;
import com.nbtech.ailab.biz.service.ISelectPushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 选择结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@RestController
@RequestMapping("biz/selectpush")
@Api(tags = "选择结果推送数据")
public class SelectPushController {

    @Autowired
    private ISelectPushService selectPushService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<SelectPushDto>> page(PageDto pageDto, SelectPushDto dto) {
        PageResult<SelectPushDto> page = selectPushService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<SelectPushDto> get(@PathVariable("id") Long id) {
            SelectPushDto data = selectPushService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody SelectPushDto dto) {
            selectPushService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody SelectPushDto dto) {
            selectPushService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
            selectPushService.delete(ids);
        return BizResponse.success();
    }

}