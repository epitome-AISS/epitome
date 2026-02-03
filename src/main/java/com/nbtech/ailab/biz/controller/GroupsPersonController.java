package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.dto.GroupsPersonDto;
import com.nbtech.ailab.biz.service.IGroupsPersonService;
import com.nbtech.ailab.facade.GroupFacade;
import com.nbtech.ailab.facade.GroupPersonFacade;
import com.nbtech.ailab.vo.GroupQcVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("experiment/groupsperson")
@Api(tags = "实验人群包")
public class GroupsPersonController {

    @Autowired
    private IGroupsPersonService groupsPersonService;

    @Autowired
    private GroupFacade groupFacade;

    @Autowired
    private GroupPersonFacade groupPersonFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<GroupsPersonDto>> page(PageDto pageDto, GroupsPersonDto dto) {
        PageResult<GroupsPersonDto> page = groupsPersonService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<GroupsPersonDto> get(@PathVariable("id") Long id) {
        GroupsPersonDto data = groupsPersonService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody GroupsPersonDto dto) {
        groupsPersonService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody GroupsPersonDto dto) {
        groupsPersonService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        groupsPersonService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

    @GetMapping("externalAddGroupPerson")
    @ApiOperation("额外添加实验组人群")
    @LogOperation("额外添加实验组人群")
    public BizResponse<?> externalAddGroupPerson(@RequestParam("groupId") Long groupId,
            @RequestParam("number") Integer number) throws Exception {
        return groupFacade.externalAddGroupPerson(groupId, number);
    }

    @PostMapping("insertGroupPerson")
    @ApiOperation("实验人群包创建")
    @LogOperation("实验人群包创建")
    public BizResponse<?> insertGroupPerson(@RequestBody GroupsDto groupsDto) throws Exception {
        return groupFacade.insertGroupPerson(groupsDto);
    }


    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    public void export(GroupsPersonDto dto, HttpServletResponse response) throws Exception {
        groupPersonFacade.export(dto, response);
    }

    @GetMapping("exportByExperimentPlan")
    @ApiOperation("导出实验计划下所有实验组的人员信息")
    @LogOperation("导出实验计划下所有实验组的人员信息")
    public void exportByExperimentPlan(@RequestParam("experimentPlanId") Long experimentPlanId,
            HttpServletResponse response) throws Exception {
        groupPersonFacade.exportByExperimentPlan(experimentPlanId, response);
    }

    @GetMapping("getUserQcList")
    @ApiOperation("获取用户和密码以及二维码")
    @LogOperation("获取用户和密码以及二维码")
    public BizResponse<?> getUserQcList(@RequestParam("groupId") Long groupId) throws Exception {
        return BizResponse.success(groupPersonFacade.getUserQcList(groupId));
    }

}