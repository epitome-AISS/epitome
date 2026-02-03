package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.PreviewStructureElementIdEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 预览多人多轮工作流的预算子id
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-29
 */
@Mapper
public interface PreviewStructureElementIdDao extends BaseMapper<PreviewStructureElementIdEntity> {

    @Select("select structure_id from t_preview_structure_element_id where element_id = #{elementId}")
    Long getStructureId(String elementId);
}