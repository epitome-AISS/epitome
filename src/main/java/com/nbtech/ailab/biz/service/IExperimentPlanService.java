package com.nbtech.ailab.biz.service;


import com.nbtech.ailab.biz.dto.ExperimentPlanDto;
import com.nbtech.ailab.biz.dto.ExperimentPlanSimpleDto;
import com.nbtech.ailab.biz.entity.ExperimentPlanEntity;
import com.nbtech.ailab.vo.HomeRecordVo;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.CrudService;

import java.util.List;

/**
 * 实验表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
public interface IExperimentPlanService extends CrudService<ExperimentPlanEntity, ExperimentPlanDto> {


    /**
     * 根据实验计划id获取实验详情
     *
     * @param id 实验计划id
     * @return 返回值
     */
    ExperimentPlanDto getPlanById(Long id);

    Long getByExperimentName(String experimentName);

    /**
     * 统计首页数据
     */
    HomeRecordVo getHomeRecord();

    /**
     * 获取实验计划的最新号码
     * @return
     */
    String getOldCode();

    /**
     * 删除实验计划集合
     * @param ids 实验计划ids
     */
    void deleteExperimentPlan(Long[] ids);

    /**
     * 分页查询所有开源的实验
     * @param pageDto 分页参数
     * @param dto 查询条件
     * @return 分页结果
     */
    PageResult<ExperimentPlanDto> pagePublic(PageDto pageDto, ExperimentPlanDto dto);

    /**
     * 查询当前用户归属且未删除的实验计划列表（仅包含id、名称、标题、创建时间）
     * @return 实验计划列表，按创建时间排序
     */
    List<ExperimentPlanSimpleDto> listMyPlans();

    /**
     * 查询当前用户自己的已开源的实验计划列表（holdingStatus=PUBLIC）
     * @param experimentName 实验计划名称（可选，用于模糊查询）
     * @return 实验计划列表
     */
    List<ExperimentPlanSimpleDto> listMyPublicPlans(String experimentName);

    /**
     * 分页查询
     * @param pageDto
     * @param dto
     * @return
     */
    PageResult<ExperimentPlanDto> pagePlan(PageDto pageDto, ExperimentPlanDto dto);

}