package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.FavoritePageDto;
import com.nbtech.ailab.biz.dto.MaterialFavoritePageDto;
import com.nbtech.ailab.biz.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Mapper
public interface FavoriteDao extends BaseMapper<FavoriteEntity> {

    /**
     * 分页查询当前用户收藏的实验计划（联表查询）
     *
     * @param page   分页参数
     * @param userId 用户id
     * @param name   实验名称（可选，用于模糊查询）
     * @param experimentField 实验领域（可选，用于模糊查询）
     * @return 分页结果
     */
    Page<FavoritePageDto> pageFavorite(Page<Object> page, @Param("userId") Long userId, @Param("name") String name, @Param("experimentField") String experimentField);

    /**
     * 分页查询当前用户收藏的素材（联表查询）
     *
     * @param page         分页参数
     * @param userId       用户id
     * @param materialType 素材类型（可选，用于筛选）
     * @param materialName 素材名称（可选，用于模糊查询）
     * @return 分页结果
     */
    Page<MaterialFavoritePageDto> pageFavoriteMaterial(Page<Object> page, @Param("userId") Long userId,
            @Param("materialType") String materialType, @Param("materialName") String materialName);

    /**
     * 分页查询当前用户收藏的模型（联表查询）
     *
     * @param page     分页参数
     * @param userId   用户id
     * @param modelName 模型名称（可选，用于模糊查询）
     * @return 分页结果
     */
    Page<com.nbtech.ailab.biz.dto.ModelFavoritePageDto> pageFavoriteModel(Page<Object> page,
            @Param("userId") Long userId, @Param("modelName") String modelName);

    /**
     * 分页查询当前用户收藏的问卷（联表查询）
     *
     * @param page             分页参数
     * @param userId           用户id
     * @param questionnaireName 问卷名称（可选，用于模糊查询）
     * @return 分页结果
     */
    Page<com.nbtech.ailab.biz.dto.QuestionnaireFavoritePageDto> pageFavoriteQuestionnaire(Page<Object> page,
            @Param("userId") Long userId, @Param("questionnaireName") String questionnaireName);

}
