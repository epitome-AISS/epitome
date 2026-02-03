package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.ModelDto;
import com.nbtech.ailab.biz.entity.ModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模型对话管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-11
 */
@Mapper
public interface ModelDao extends BaseMapper<ModelEntity> {

    Page<ModelDto> pageModel(Page<Object> of, ModelDto dto, String userName, Long roleId);

    Page<ModelDto> pageAudit(Page<Object> of, ModelDto dto, String userName, Long roleId);

    Integer getOpenList();

    List<ModelDto> liseModels(Integer modelBotType);

    /**
     * 查询所有模型的所有标签
     * @return
     */
    List<String> getModelTags();

    /**
     * 统计所有的开源模型机器人数量
     */
    Integer getModelCount(List<Integer> modelBotTypeList);

    /**
     * 分页查询所有status等于OPEN的模型（联表查询收藏标识）
     *
     * @param page   分页参数
     * @param userId 用户id
     * @param attribution 归属人（可选）
     * @return 分页结果
     */
    Page<ModelDto> pagePublicWithFavorite(Page<Object> page, @Param("userId") Long userId, @Param("attribution") String attribution);

    /**
     * 根据id查询模型(可以查询出已删除的数据)
     *
     * @param id 模型id
     * @return 模型实体
     */
    ModelEntity getModelById(@Param("id") Long id);

    /**
     * 查询所有模型（不限制is_delete）
     *
     * @param dto 查询条件
     * @return 模型列表
     */
    List<ModelDto> listAll(@Param("dto") ModelDto dto);
}