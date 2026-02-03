package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.ExperimentPlanEntity;
import com.nbtech.ailab.vo.HomeRecordVo;
import com.nbtech.ailab.vo.PlanStatusVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 实验表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface ExperimentPlanDao extends BaseMapper<ExperimentPlanEntity> {

    /**
     * 查询最新的实验编码
     *
     * @return
     */
    String getOldCode();

    /**
     * 统计实验的 新建 待审核 待发布 进行中 已完成 个数
     */
    PlanStatusVo getPlanStatus(PlanStatusVo planStatusVo);

    /**
     * 通过实验组id获取实验状态
     */
    String getSourceStatus(Long groupId);

    /**
     * 统计首页数据
     *
     * @param beEnd         待完成
     * @param experimentEnd 实验的完成
     * @param groupEnd      实验组的完成
     * @return
     */
    HomeRecordVo getHomeRecord(String beEnd, String experimentEnd, String groupEnd);

    /**
     * 获取进行中和完成的实验计划id集合
     *
     * @return 进行中和完成的实验计划id集合
     */
    List<Long> getProcessingPlanIds();

    /**
     * 统计所有的学生数量
     */
    Integer getStudentNumber();

    /**
     * 统计所有的公开课堂数
     */
    Integer getPublicClassNumber();

    /**
     * 分页查询公开的实验计划（联表查询是否收藏）
     *
     * @param page 分页参数
     * @param dto 查询条件
     * @param userId 当前用户id
     * @return 分页结果
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.nbtech.ailab.biz.dto.ExperimentPlanDto> pagePublicWithFavorite(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<Object> page,
            com.nbtech.ailab.biz.dto.ExperimentPlanDto dto,
            Long userId);
}