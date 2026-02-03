package com.nbtech.ailab.biz.controller;


import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.facade.ExperimentPlanFacade;
import com.nbtech.ailab.facade.GroupFacade;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


/**
 * 实验组表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@RestController
@RequestMapping("experiment/groups")
@Api(tags = "实验组表")
@Slf4j
public class GroupsController {

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private GroupFacade groupFacade;

    @Autowired
    private ExperimentPlanFacade experimentPlanFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    @RequiresRoles("manager")
    public BizResponse<PageResult<GroupsDto>> page(PageDto pageDto, GroupsDto dto) {
        PageResult<GroupsDto> page = groupsService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<GroupsDto> get(@PathVariable("id") Long id) {
        GroupsDto data = groupsService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresRoles("manager")
    public BizResponse<?> save(@RequestBody GroupsDto dto) {
        groupsService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresRoles("manager")
    public BizResponse<?> update(@RequestBody GroupsDto dto) {
        groupsService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresRoles("manager")
    public BizResponse<?> delete(@RequestBody Long[] ids) {
        return groupsService.deleteByIds(ids);
    }

    @PostMapping("addGroup")
    @ApiOperation("新增实验组")
    @LogOperation("新增实验组")
    @RequiresRoles("manager")
    public BizResponse<?> addGroup(@RequestBody GroupsDto groupsDto) throws Exception {
        return BizResponse.success(groupFacade.addGroup(groupsDto));
    }

    @PutMapping("updateGroup")
    @ApiOperation("修改实验组")
    @LogOperation("修改实验组")
    @RequiresRoles("manager")
    public BizResponse<?> updateGroup(@RequestBody GroupsDto groupsDto) {
        return BizResponse.success(groupFacade.updateGroup(groupsDto));
    }

    @GetMapping("copyGroup")
    @ApiOperation("复制实验组")
    @LogOperation("复制实验组")
    @RequiresRoles("manager")
    public BizResponse<?> copyGroup(@RequestParam(name = "id") Long id) throws Exception {
        return BizResponse.success(groupFacade.copyGroup(id, null, new HashMap<>()));
    }

    @GetMapping("copyExperimentPlan")
    @ApiOperation("复制实验计划")
    @LogOperation("复制实验计划")
    @RequiresRoles("manager")
    public BizResponse<?> copyExperimentPlan(@RequestParam(name = "id") Long id) throws Exception {
        experimentPlanFacade.copyPlanAndUseElement(id);
        return BizResponse.success();
    }

    @PostMapping("getImageUrl")
    @ApiOperation("获取图片url")
    @RequiresRoles("manager")
    public BizResponse<?> getImageUrl(@RequestBody MultipartFile file) throws Exception {
        return BizResponse.success(groupFacade.getFileUrl(file));
    }

    @PostMapping("getRandImageUrl")
    @ApiOperation("获取图片url(随机生成地址)")
    @RequiresRoles("manager")
    public BizResponse<?> getRandImageUrl(@RequestBody List<MultipartFile> files) throws Exception {
        return BizResponse.success(groupFacade.getFilesUrl(files));
    }

    @PostMapping("exportData")
    @ApiOperation("导出所有实验结果包数据")
    @LogOperation("导出所有实验结果包数据")
    @RequiresRoles("manager")
    public void exportData(@RequestBody GroupsDto dto, HttpServletResponse response) throws Exception {
        groupFacade.exportData(dto, response);
    }


}