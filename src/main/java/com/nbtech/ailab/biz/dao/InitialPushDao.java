package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.InitialPushEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 初始化推送数据表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@Mapper
public interface InitialPushDao extends BaseMapper<InitialPushEntity> {

    /**
     * 获取初始推送的数据
     * @param elementId 算子id
     * @return
     */
    List<InitialPushEntity> getInitialData(String elementId);
}