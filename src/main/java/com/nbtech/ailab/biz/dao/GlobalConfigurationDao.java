package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.GlobalConfigurationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 全局配置表
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-08
 */
@Mapper
public interface GlobalConfigurationDao extends BaseMapper<GlobalConfigurationEntity> {
	
}