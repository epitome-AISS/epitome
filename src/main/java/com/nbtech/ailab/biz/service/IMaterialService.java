package com.nbtech.ailab.biz.service;

import com.nbtech.ailab.vo.MaterialTypeListVo;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.MaterialDto;
import com.nbtech.ailab.biz.entity.MaterialEntity;

import java.util.List;

/**
 * 素材管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-07
 */
public interface IMaterialService extends CrudService<MaterialEntity, MaterialDto> {
    void deleteIsReview(Long id);

    MaterialDto getByName(String name);

    List<MaterialDto> listVo(MaterialDto dto);

    PageResult<MaterialDto> pageMaterial(PageDto pageDto, MaterialDto dto);

    PageResult<MaterialDto> pagePublic(PageDto pageDto, MaterialDto dto);

    void deleteById(Long id);

    PageResult<MaterialDto> pageAudit(PageDto pageDto, MaterialDto dto);

    List<MaterialDto> openList(String materialType);

    /**
     * 统计素材的所有标签集合
     *
     * @return
     */
    List<String> getMaterialTags(String materialType);

    /**
     * 获取所有的素材
     *
     * @return
     */
    MaterialTypeListVo getMaterialList(MaterialDto dto);

    /**
     * 统计所有的开源素材数量
     */
    Integer getPublicMaterialNumber(List<String> materialTypeList);

    /**
     * 复制素材
     */
    MaterialEntity copyMaterial(Long materialId, Long planId);

    /**
     * 查询当前用户自己的未开源的素材列表（material_status != 'OPEN'）
     * @param materialName 素材名称（可选，用于模糊查询）
     * @param materialType 素材类型（可选，用于精确筛选）
     * @return 素材列表
     */
    List<MaterialDto> listMyPrivateMaterials(String materialName, String materialType);

    /**
     * 查询所有素材（不限制is_deleted）
     *
     * @param dto 查询条件
     * @return 素材列表
     */
    List<MaterialDto> listAll(MaterialDto dto);
}