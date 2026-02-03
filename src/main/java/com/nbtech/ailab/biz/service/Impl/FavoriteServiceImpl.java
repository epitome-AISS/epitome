package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dao.FavoriteDao;
import com.nbtech.ailab.biz.dto.FavoriteDto;
import com.nbtech.ailab.biz.dto.FavoritePageDto;
import com.nbtech.ailab.biz.dto.MaterialFavoritePageDto;
import com.nbtech.ailab.biz.dto.ModelFavoritePageDto;
import com.nbtech.ailab.biz.dto.QuestionnaireFavoritePageDto;
import com.nbtech.ailab.biz.entity.FavoriteEntity;
import com.nbtech.ailab.biz.service.IFavoriteService;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 收藏表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Service
public class FavoriteServiceImpl
        extends CrudServiceImpl<FavoriteDao, FavoriteEntity, FavoriteDto>
        implements IFavoriteService {

    @Override
    public QueryWrapper<FavoriteEntity> getWrapper(FavoriteDto favoriteDto) {
        return null;
    }

    @Override
    public PageResult<FavoritePageDto> pageFavorite(PageDto pageDto, String name, String experimentField) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<FavoritePageDto> result = baseDao.pageFavorite(page, userId, name, experimentField);
        return PageResult.build(result, result.getRecords());
    }

    @Override
    public PageResult<MaterialFavoritePageDto> pageFavoriteMaterial(PageDto pageDto, String materialType,
            String materialName) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<MaterialFavoritePageDto> result = baseDao.pageFavoriteMaterial(page, userId, materialType, materialName);
        return PageResult.build(result, result.getRecords());
    }

    @Override
    public PageResult<ModelFavoritePageDto> pageFavoriteModel(PageDto pageDto, String modelName) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<ModelFavoritePageDto> result = baseDao.pageFavoriteModel(page, userId, modelName);
        return PageResult.build(result, result.getRecords());
    }

    @Override
    public PageResult<QuestionnaireFavoritePageDto> pageFavoriteQuestionnaire(PageDto pageDto,
            String questionnaireName) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<QuestionnaireFavoritePageDto> result = baseDao.pageFavoriteQuestionnaire(page, userId, questionnaireName);
        return PageResult.build(result, result.getRecords());
    }



    @Override
    public FavoriteDto saveFavorite(String favoriteType, Long targetId) {
        Long userId = ShiroUtils.getUserId();

        // 将字符串转换为枚举
        com.nbtech.ailab.common.FavoriteTypeEnum favoriteTypeEnum = com.nbtech.ailab.common.FavoriteTypeEnum
                .getByValue(favoriteType);
        if (favoriteTypeEnum == null) {
            throw new IllegalArgumentException("无效的收藏类型: " + favoriteType);
        }

        // 检查是否已经收藏过（@TableLogic会自动过滤已删除的记录）
        QueryWrapper<FavoriteEntity> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("favorite_type", favoriteType)
                .eq("target_id", targetId)
                .eq("user_id", userId);
        FavoriteEntity existing = baseDao.selectOne(checkWrapper);
        if (existing != null) {
            // 如果已经收藏过，直接返回
            return ConvertUtils.sourceToTarget(existing, FavoriteDto.class);
        }

        // 创建新的收藏记录
        FavoriteEntity entity = new FavoriteEntity();
        entity.setFavoriteType(favoriteTypeEnum);
        entity.setTargetId(targetId);
        entity.setUserId(userId);
        entity.setFavoriteTime(LocalDateTime.now());

        baseDao.insert(entity);
        return ConvertUtils.sourceToTarget(entity, FavoriteDto.class);
    }

    @Override
    public void deleteByTargetId(String favoriteType, Long targetId) {
        Long userId = ShiroUtils.getUserId();

        QueryWrapper<FavoriteEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("favorite_type", favoriteType)
                .eq("target_id", targetId);

        // MyBatis-Plus会自动处理逻辑删除（@TableLogic注解）
        baseDao.delete(wrapper);
    }
}
