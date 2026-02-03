package com.nbtech.ailab.biz.service;

import com.nbtech.ailab.biz.dto.FavoriteDto;
import com.nbtech.ailab.biz.dto.FavoritePageDto;
import com.nbtech.ailab.biz.entity.FavoriteEntity;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.CrudService;

/**
 * 收藏表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
public interface IFavoriteService
                extends CrudService<FavoriteEntity, FavoriteDto> {

        /**
         * 分页查询当前用户收藏的实验计划（联表查询）
         *
         * @param pageDto 分页参数
         * @param name    实验名称（可选，用于模糊查询）
         * @param experimentField 实验领域（可选，用于模糊查询）
         * @return 分页结果
         */
        PageResult<FavoritePageDto> pageFavorite(PageDto pageDto, String name, String experimentField);

        /**
         * 分页查询当前用户收藏的素材（联表查询）
         *
         * @param pageDto      分页参数
         * @param materialType 素材类型（可选，用于筛选）
         * @param materialName 素材名称（可选，用于模糊查询）
         * @return 分页结果
         */
        PageResult<com.nbtech.ailab.biz.dto.MaterialFavoritePageDto> pageFavoriteMaterial(PageDto pageDto,
                        String materialType, String materialName);

        /**
         * 分页查询当前用户收藏的模型（联表查询）
         *
         * @param pageDto   分页参数
         * @param modelName 模型名称（可选，用于模糊查询）
         * @return 分页结果
         */
        PageResult<com.nbtech.ailab.biz.dto.ModelFavoritePageDto> pageFavoriteModel(PageDto pageDto, String modelName);

        /**
         * 分页查询当前用户收藏的问卷（联表查询）
         *
         * @param pageDto           分页参数
         * @param questionnaireName 问卷名称（可选，用于模糊查询）
         * @return 分页结果
         */
        PageResult<com.nbtech.ailab.biz.dto.QuestionnaireFavoritePageDto> pageFavoriteQuestionnaire(PageDto pageDto,
                        String questionnaireName);


        /**
         * 新增收藏
         *
         * @param favoriteType 收藏类型（EXPERIMENT_PLAN-实验计划，MATERIAL-素材，AGENT-智能体）
         * @param targetId     收藏目标id（实验计划id、素材id或智能体id）
         * @return 收藏信息
         */
        FavoriteDto saveFavorite(String favoriteType, Long targetId);

        /**
         * 根据收藏类型和目标id删除当前用户的收藏
         *
         * @param favoriteType 收藏类型
         * @param targetId     收藏目标id
         */
        void deleteByTargetId(String favoriteType, Long targetId);

}
