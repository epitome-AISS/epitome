package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.MaterialDto;
import com.nbtech.ailab.biz.entity.MaterialEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-07
 */
@Mapper
public interface MaterialDao extends BaseMapper<MaterialEntity> {

    List<MaterialDto> listVo(MaterialDto dto, String userName);

    Page<MaterialDto> pageMaterial(Page<Object> of, MaterialDto dto, String userName, Long roleId);

    Page<MaterialDto> pageAudit(Page<Object> of, MaterialDto dto, String userName, Long roleId);

    List<MaterialDto> getOpenList();

    /**
     * 查询这个类型的所有素材的标签
     * 
     * @param materialType 素材类型
     * @return
     */
    List<String> getMaterialTags(String materialType);

    /**
     * 统计所有的开源素材数量
     */
    Integer getPublicMaterialNumber(List<String> materialTypeList);

    /**
     * 分页查询公开的素材（固定查询条件：当前用户归属、状态为OPEN）
     *
     * @param page 分页参数
     * @param dto  查询条件（素材类别、素材名称）
     * @return 分页结果
     */
    Page<MaterialDto> pagePublic(Page<Object> page, MaterialDto dto);

    /**
     * 分页查询公开的素材（联表查询是否收藏）
     *
     * @param page   分页参数
     * @param dto    查询条件（素材类别、素材名称）
     * @param userId 当前用户id
     * @return 分页结果
     */
    Page<MaterialDto> pagePublicWithFavorite(Page<Object> page, MaterialDto dto, Long userId);

    /**
     * 根据ID查询素材（不限制is_deleted）
     *
     * @param id 素材ID
     * @return 素材实体
     */
    MaterialEntity selectById(Long id);

    /**
     * 查询所有素材（不限制is_deleted）
     *
     * @param dto 查询条件
     * @return 素材列表
     */
    List<MaterialDto> listAll(@Param("dto") MaterialDto dto);
}