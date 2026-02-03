package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.ChatHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天记录主表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-07-11
 */
@Mapper
public interface ChatHistoryDao extends BaseMapper<ChatHistoryEntity> {

}