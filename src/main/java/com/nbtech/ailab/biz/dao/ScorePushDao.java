package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.ScorePushEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测评结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-06-05
 */
@Mapper
public interface ScorePushDao extends BaseMapper<ScorePushEntity> {

    /**
     * 获取测评结果推送数据
     * @param elementId 算子id
     * @return
     */
    List<ScorePushEntity> getScoreData(String elementId);
}