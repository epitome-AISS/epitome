package com.nbtech.ailab.biz.controller;

import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.ailab.biz.dto.ScorePushDto;
import com.nbtech.ailab.biz.service.IScorePushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测评结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-06-05
 */
@RestController
@RequestMapping("biz/scorepush")
@Api(tags = "测评结果推送数据")
public class ScorePushController {

    @Autowired
    private IScorePushService scorePushService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<ScorePushDto>> page(PageDto pageDto, ScorePushDto dto) {
        PageResult<ScorePushDto> page = scorePushService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<ScorePushDto> get(@PathVariable("id") Long id) {
            ScorePushDto data = scorePushService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody ScorePushDto dto) {
            scorePushService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody ScorePushDto dto) {
            scorePushService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody List<Long> ids) {
            scorePushService.delete(ids);
        return BizResponse.success();
    }

}