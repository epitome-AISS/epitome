package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.HistoryRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模型问答历史记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Mapper
public interface HistoryRecordDao extends BaseMapper<HistoryRecordEntity> {
	
}