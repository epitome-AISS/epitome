package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.EvaluatedDimensionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 受测用户维度
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Mapper
public interface EvaluatedDimensionDao extends BaseMapper<EvaluatedDimensionEntity> {

    /**
     * 统计每个实验组地域的人数
     */
    List<EvaluatedDimensionEntity> countAddress(@Param("groupId") Long groupId);
}