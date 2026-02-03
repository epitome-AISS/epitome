package com.nbtech.ailab.biz.controller;

import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.HistoryRecordDto;
import com.nbtech.ailab.biz.service.IHistoryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 模型问答历史记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@RestController
@RequestMapping("biz/historyrecord")
@Api(tags = "模型问答历史记录")
@RequiresRoles("manager")
public class HistoryRecordController {
    @Autowired
    private IHistoryRecordService historyRecordService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<HistoryRecordDto>> page(PageDto pageDto, HistoryRecordDto dto) {
        PageResult<HistoryRecordDto> page = historyRecordService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<HistoryRecordDto> get(@PathVariable("id") Long id) {
        HistoryRecordDto data = historyRecordService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody HistoryRecordDto dto) {
        historyRecordService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody HistoryRecordDto dto) {
        historyRecordService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        historyRecordService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

}