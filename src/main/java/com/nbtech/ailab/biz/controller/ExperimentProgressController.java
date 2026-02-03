package com.nbtech.ailab.biz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.dto.ExperimentProgressDto;
import com.nbtech.ailab.biz.service.IExperimentProgressService;
import com.nbtech.ailab.facade.ExperimentProgressFacade;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.OverAuthenParamVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * 实验流程进展表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@RestController
@RequestMapping("experimentprogress")
@Api(tags = "实验流程进展表")
public class ExperimentProgressController {



    @Autowired
    private ExperimentProgressFacade experimentProgressFacade;

    @Autowired
    private IExperimentProgressService experimentProgressService;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<ExperimentProgressDto>> page(PageDto pageDto, ExperimentProgressDto dto) {
        PageResult<ExperimentProgressDto> page = experimentProgressService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @PostMapping("getProgress")
    @ApiOperation("获取实验最新流程信息")
    public BizResponse<ElementVo> getProgress(@RequestBody ExperimentProgressDto dto)
            throws JsonProcessingException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchAlgorithmException {
        return BizResponse.success(experimentProgressFacade.getProgress(dto));
    }

    @PostMapping("forwardNextProgress")
    @ApiOperation("完成当前算子 推进下一算子开始")
    public BizResponse<ElementVo> forwardNextProgress(@RequestBody ExperimentProgressDto dto) throws Exception {
        return BizResponse.success(experimentProgressFacade.forwardNextProgress(dto));
    }

    @PostMapping("overAuthentication")
    @ApiOperation("完成身份校验算子")
    public BizResponse<Long> overAuthentication(@RequestBody OverAuthenParamVo authenParamVo) throws Exception {
        return BizResponse.success(experimentProgressFacade.overAuthentication(authenParamVo));
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody ExperimentProgressDto dto) {
        experimentProgressService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody ExperimentProgressDto dto) {
        experimentProgressService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        experimentProgressService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

    @PostMapping("endExperiment")
    @ApiOperation("用户完成实验")
    public BizResponse<ElementVo> endExperiment(@RequestBody ExperimentProgressDto dto) throws Exception {
         experimentProgressFacade.endExperiment(dto);
        return BizResponse.success();
    }

    @GetMapping("getInterveneList")
    @ApiOperation("查询当前实验组下的所有干预")
    public BizResponse<List<ElementVo>> getInterveneList(@ApiParam("实验组id") @RequestParam("groupId") Long groupId) {
        return BizResponse.success(experimentProgressFacade.getInterveneList(groupId));
    }

}