package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.ExperimentMessageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实验组信息表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-06-11
 */
@Mapper
public interface ExperimentMessageDao extends BaseMapper<ExperimentMessageEntity> {
	
}