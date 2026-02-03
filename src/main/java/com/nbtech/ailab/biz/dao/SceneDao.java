package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.nbtech.ailab.biz.entity.SceneEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 场景
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface SceneDao extends BaseMapper<SceneEntity> {

    /**
     * 查询所有的实验场景
     * @return
     */
    @Select("select scene_name from t_scene where is_deleted ='0'")
    List<String> getSceneList();
}