package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.ExperimentPlanOperationRecordDto;
import com.nbtech.ailab.biz.service.IExperimentPlanOperationRecordService;
import com.nbtech.ailab.common.HoldStatusEnum;
import com.nbtech.ailab.common.PlanStatusRecordEnum;
import com.nbtech.ailab.facade.ExperimentPlanFacade;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.model.PageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 实验更新表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("experiment/experimentplanoperationrecord")
@Api(tags = "实验更新表")
@RequiresRoles("manager")
public class ExperimentPlanOperationRecordController {

    @Autowired
    private IExperimentPlanOperationRecordService experimentPlanOperationRecordService;

    @Autowired
    private ExperimentPlanFacade experimentPlanFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<ExperimentPlanOperationRecordDto>> page(PageDto pageDto,
            ExperimentPlanOperationRecordDto dto) {
        PageResult<ExperimentPlanOperationRecordDto> page = experimentPlanOperationRecordService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<ExperimentPlanOperationRecordDto> get(@PathVariable("id") Long id) {
        ExperimentPlanOperationRecordDto data = experimentPlanOperationRecordService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("修改实验计划表状态")
    @LogOperation("修改实验计划表状态")
    public BizResponse<?> updatePlanStatus(@RequestBody ExperimentPlanOperationRecordDto dto) throws Exception {
        // 修改实验计划表状态
        experimentPlanFacade.updatePlanStatus(dto);
        return BizResponse.success();
    }

    /**
     * 暂时不发布 但是要写明原因 结果归纳到 发布原因里面
     *
     * @param dto
     * @return
     */
    @PostMapping("notPublish")
    @ApiOperation("暂不发布按钮")
    @LogOperation("暂不发布按钮")
    public BizResponse<?> notPublish(@RequestBody ExperimentPlanOperationRecordDto dto) {
        dto.setOperateType(PlanStatusRecordEnum.PUBLISH.getDesc());
        experimentPlanOperationRecordService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody ExperimentPlanOperationRecordDto dto) {
        experimentPlanOperationRecordService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        experimentPlanOperationRecordService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

    @GetMapping("getOperation")
    @ApiOperation("统计实验的审核 发布 完成 暂停说明")
    public BizResponse<?> getOperation(@ApiParam("实验计划id") @RequestParam("id") String id) {
        return BizResponse.success(experimentPlanFacade.getOperation(id));
    }

    @PostMapping("openSource")
    @ApiOperation("开源实验")
    @LogOperation("开源实验")
    public BizResponse<?> openSource(@ApiParam("实验计划id") @RequestParam("id") Long id) {
        experimentPlanFacade.updateHoldingStatus(id, HoldStatusEnum.PUBLIC.getDesc());
        return BizResponse.success();
    }

    @PostMapping("cancelOpenSource")
    @ApiOperation("取消开源实验")
    @LogOperation("取消开源实验")
    public BizResponse<?> cancelOpenSource(@ApiParam("实验计划id") @RequestParam("id") Long id) {
        experimentPlanFacade.updateHoldingStatus(id, HoldStatusEnum.PRIVATE.getDesc());
        return BizResponse.success();
    }

}