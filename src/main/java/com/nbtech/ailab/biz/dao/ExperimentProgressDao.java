package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.ExperimentProgressDto;
import com.nbtech.ailab.biz.dto.MaterialGroupListDto;
import com.nbtech.ailab.biz.entity.ExperimentProgressEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 实验流程进展表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Mapper
public interface ExperimentProgressDao extends BaseMapper<ExperimentProgressEntity> {

    /**
     * 查询最新的实验进展
     * @param userId 查询条件
     * @return
     */
    ExperimentProgressDto getProgress(Long userId, Long experimentId, Long groupsId);

    /**
     * 查询这个人某个算子的最新记录
     * @param userId 查询条件
     * @return
     */
    ExperimentProgressDto getElementProgress(Long userId, String elementId);

    /**
     * 询这个实验组下的所有素材包 使用记录
     * @param groupId
     * @param elementIds
     * @return
     */
    List<MaterialGroupListDto> getMaterialGroup(Long groupId, List<String> elementIds);

    /**
     * 查询同一个模型组下该用户已经使用过哪些模型了
     * @param setId 模型组id
     * @param userId 用户id
     * @param groupsId 实验组id
     * @param experimentId 实验id
     * @return
     */
    List<String> getModelNames(Long setId, Long userId, Long groupsId, Long experimentId);
}