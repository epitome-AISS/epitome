package com.nbtech.ailab.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.dto.ExperimentPlanDto;
import com.nbtech.ailab.biz.dto.ExperimentPlanSimpleDto;
import com.nbtech.ailab.biz.service.IExperimentPlanService;
import com.nbtech.ailab.facade.ExperimentPlanFacade;
import com.nbtech.ailab.facade.GroupFacade;
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

import java.util.List;

/**
 * 实验表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("experiment/experimentplan")
@Api(tags = "实验表")
public class ExperimentPlanController {
    @Autowired
    private IExperimentPlanService experimentPlanService;

    @Autowired
    private GroupFacade groupFacade;

    @Autowired
    private ExperimentPlanFacade experimentPlanFacade;

    @GetMapping("page")
    @ApiOperation("分页查询自己的实验")
    @RequiresRoles("manager")
    public BizResponse<PageResult<ExperimentPlanDto>> pageMy(PageDto pageDto, ExperimentPlanDto dto) {
        PageResult<ExperimentPlanDto> page = experimentPlanService.pagePlan(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("pagePublic")
    @ApiOperation("分页查询所有开源的实验")
    @RequiresRoles("manager")
    public BizResponse<PageResult<ExperimentPlanDto>> pagePublic(PageDto pageDto, ExperimentPlanDto dto) {
        PageResult<ExperimentPlanDto> page = experimentPlanService.pagePublic(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping
    @ApiOperation("信息")
    public BizResponse<ExperimentPlanDto> get(@RequestParam(name = "id") Long id) {
        ExperimentPlanDto data = experimentPlanService.getPlanById(id);
        return BizResponse.success(data);
    }

    @GetMapping("getPlanInfo")
    @ApiOperation("获取计划详情信息")
    @RequiresRoles("manager")
    public BizResponse<ExperimentPlanDto> getPlanInfo(@RequestParam(name = "id") Long id) {
        ExperimentPlanDto data = experimentPlanService.getPlanById(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresRoles("manager")
    public BizResponse<?> save(@RequestBody ExperimentPlanDto dto) {
        return experimentPlanFacade.planSave(dto);
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresRoles("manager")
    public BizResponse<?> update(@RequestBody ExperimentPlanDto dto) {
        experimentPlanService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresRoles("manager")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        experimentPlanService.deleteExperimentPlan(ids);
        return BizResponse.success();
    }

    @GetMapping("getGroupsByPlan")
    @ApiOperation("查询实验计划下的实验组")
    @LogOperation("查询实验计划下的实验组")
    @RequiresRoles("manager")
    public BizResponse<?> getGroupsByPlan(@ApiParam("当前页") @RequestParam(name = "current") Integer current,
            @ApiParam("每页展示条数") @RequestParam(name = "size") Integer size,
            @ApiParam("实验计划Id") @RequestParam(name = "planId") String planId) throws JsonProcessingException {
        Page<String> page = new Page<String>(current, size);
        return BizResponse.success(groupFacade.getGroupsByPlan(page, planId));
    }

    @GetMapping("getExperimentCode")
    @ApiOperation("生成最新的实验编码")
    public BizResponse<?> getExperimentCode() {
        return BizResponse.success(experimentPlanFacade.getExperimentCode());
    }

    @GetMapping("getPlanStatus")
    @ApiOperation("统计实验的 新建 待审核 待发布 进行中 已完成 个数")
    public BizResponse<?> getPlanStatus() {
        return BizResponse.success(experimentPlanFacade.getPlanStatus());
    }

    @GetMapping("countName")
    @ApiOperation("校验实验名称是否添加")
    public BizResponse<?> countName(@ApiParam("实验名称") String experimentName) {
        return BizResponse.success(experimentPlanFacade.countName(experimentName));
    }

    @GetMapping("getHomeRecord")
    @ApiOperation("统计首页数据")
    public BizResponse<?> getHomeRecord() {
        return BizResponse.success(experimentPlanService.getHomeRecord());
    }

    @GetMapping("listMyPlans")
    @ApiOperation("查询当前用户实验计划列表")
    @RequiresRoles("manager")
    public BizResponse<List<ExperimentPlanSimpleDto>> listMyPlans() {
        List<ExperimentPlanSimpleDto> list = experimentPlanService.listMyPlans();
        return BizResponse.success(list);
    }

    @GetMapping("listMyPublicPlans")
    @ApiOperation("查询当前用户自己的未开源的实验计划列表")
    @RequiresRoles("manager")
    public BizResponse<List<ExperimentPlanSimpleDto>> listMyPublicPlans(
            @ApiParam(value = "实验计划名称（可选，用于模糊查询）") @RequestParam(required = false) String experimentName) {
        List<ExperimentPlanSimpleDto> list = experimentPlanService.listMyPublicPlans(experimentName);
        return BizResponse.success(list);
    }

    @PostMapping("copyPlanAndAllElement")
    @ApiOperation("复制实验计划及所有元素")
    @LogOperation("复制实验计划及所有元素")
    @RequiresRoles("manager")
    public BizResponse<?> copyPlanAndAllElement(@ApiParam("实验计划id") @RequestParam(name = "id") Long id)
            throws JsonProcessingException, InterruptedException {
        Long newPlanId = experimentPlanFacade.copyPlanAndAllElement(id);
        return BizResponse.success(newPlanId);
    }

}