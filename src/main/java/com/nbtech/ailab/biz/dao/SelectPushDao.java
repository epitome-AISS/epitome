package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.SelectPushEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 选择结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@Mapper
public interface SelectPushDao extends BaseMapper<SelectPushEntity> {

    /**
     * 获取用户选择推送的数据
     * @param elementId 算子id
     * @return
     */
    List<SelectPushEntity> getPushData(String elementId);
}