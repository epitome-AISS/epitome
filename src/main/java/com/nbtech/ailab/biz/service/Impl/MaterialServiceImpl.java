package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.common.exception.BizException;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.MaterialTypeListVo;
import com.nbtech.ailab.vo.TagVo;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.MaterialDao;
import com.nbtech.ailab.biz.dto.MaterialDto;
import com.nbtech.ailab.biz.entity.MaterialEntity;
import com.nbtech.ailab.biz.service.IMaterialService;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 素材管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-07
 */
@Service
public class MaterialServiceImpl extends CrudServiceImpl<MaterialDao, MaterialEntity, MaterialDto>
        implements IMaterialService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public QueryWrapper<MaterialEntity> getWrapper(MaterialDto dto) {
        QueryWrapper<MaterialEntity> wrapper = new QueryWrapper<>();
        wrapper.like(dto.getTag() != null, "tag", dto.getTag());
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public void deleteIsReview(Long id) {
        UpdateWrapper<MaterialEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("is_review", null);
        this.baseDao.update(null, updateWrapper);
    }

    /**
     * 根据名称查找素材
     *
     * @param name name
     * @return 素材
     */
    @Override
    public MaterialDto getByName(String name) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        MaterialEntity materialEntity = this.baseDao.selectOne(
                Wrappers.<MaterialEntity>lambdaQuery()
                        .eq(MaterialEntity::getMaterialName, name)
                        .eq(MaterialEntity::getMaterialAttribution, userName));
        return ConvertUtils.sourceToTarget(materialEntity, MaterialDto.class);
    }

    /**
     * 获取实验组配置中素材的下拉列表
     *
     * @param dto dto
     * @return 素材集合
     */
    @Override
    public List<MaterialDto> listVo(MaterialDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        List<MaterialDto> list = this.baseDao.listVo(dto, userName);
        return list;
    }

    /**
     * 实现素材的分页
     *
     * @param pageDto pageDto
     * @param dto     dto
     * @return 分页
     */
    @Override
    public PageResult<MaterialDto> pageMaterial(PageDto pageDto, MaterialDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        Page<MaterialDto> page = this.baseDao.pageMaterial(Page.of(pageDto.getCurrent(), pageDto.getSize()), dto,
                userName, roleId);
        return PageResult.build(page);
    }

    @Override
    public PageResult<MaterialDto> pagePublic(PageDto pageDto, MaterialDto dto) {
        // 如果传递了 materialAttribution，就设置为当前用户名（查询自己的）
        if (dto.getMaterialAttribution() != null && !dto.getMaterialAttribution().isEmpty()) {
            SysUserDto user = ShiroUtils.getUserEntity();
            dto.setMaterialAttribution(user.getUsername());
        }
        Long userId = ShiroUtils.getUserId();
        Page<MaterialDto> page = this.baseDao.pagePublicWithFavorite(Page.of(pageDto.getCurrent(), pageDto.getSize()),
                dto, userId);
        // MyBatis 会自动将 SQL 中的 0/1 转换为 Boolean false/true
        return PageResult.build(page);
    }

    /**
     * 根据素材id删除素材
     *
     * @param id id
     */
    @Override
    public void deleteById(Long id) {
        this.baseDao.deleteById(id);
    }

    /**
     * 审核者素材
     *
     * @param pageDto
     * @param dto
     * @return
     */
    @Override
    public PageResult<MaterialDto> pageAudit(PageDto pageDto, MaterialDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        Page<MaterialDto> page = this.baseDao.pageAudit(Page.of(pageDto.getCurrent(), pageDto.getSize()), dto, userName,
                roleId);
        return PageResult.build(page);
    }

    @Override
    public List<MaterialDto> openList(String materialType) {
        List<MaterialEntity> openList = this.baseDao.selectList(
                Wrappers.<MaterialEntity>lambdaQuery()
                        .eq(MaterialEntity::getMaterialStatus, "OPEN")
                        .eq(MaterialEntity::getMaterialType, materialType));
        return ConvertUtils.sourceToTarget(openList, MaterialDto.class);
    }

    @Override
    public List<MaterialDto> listMyPrivateMaterials(String materialName, String materialType) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();

        List<MaterialEntity> materialList = this.baseDao.selectList(
                Wrappers.<MaterialEntity>lambdaQuery()
                        .eq(MaterialEntity::getMaterialAttribution, userName)
                        .ne(MaterialEntity::getMaterialStatus, "OPEN")
                        .eq(MaterialEntity::getIsDelete, 0)
                        .like(materialName != null && !materialName.trim().isEmpty(), MaterialEntity::getMaterialName,
                                materialName)
                        .eq(MaterialEntity::getMaterialType,
                                materialType)
                        .orderByDesc(MaterialEntity::getUpdateDate));
        return ConvertUtils.sourceToTarget(materialList, MaterialDto.class);
    }

    @Override
    public List<MaterialDto> listAll(MaterialDto dto) {
        return baseDao.listAll(dto);
    }

    @Override
    public List<String> getMaterialTags(String materialType) {
        return baseDao.getMaterialTags(materialType);
    }

    @Override
    public MaterialTypeListVo getMaterialList(MaterialDto dto) {
        // 校验 experimentPlanId 必填
        if (dto.getExperimentPlanId() == null) {
            throw new BizException(BizResponseCodeEnum.MATERIAL_LIST_EXPERIMENT_PLAN_ID_NOT_EMPTY);
        }

        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        List<MaterialDto> list = this.baseDao.listVo(dto, userName);

        Map<String, List<MaterialDto>> tagMaterial = list.stream()
                .filter(materialDto -> materialDto.getTag() != null)
                .collect(Collectors.groupingBy(MaterialDto::getTag,
                        Collectors.mapping(Function.identity(), Collectors.toList())));
        List<MaterialDto> nullTagMaterials = list.stream()
                .filter(materialDto -> materialDto.getTag() == null)
                .collect(Collectors.toList());
        if (!nullTagMaterials.isEmpty()) {
            tagMaterial.put("none", nullTagMaterials);
        }
        Set<String> tagSet = tagMaterial.keySet();
        List<TagVo> tagVos = new ArrayList<>();
        for (String tag : tagSet) {
            TagVo tagVo = new TagVo();
            tagVo.setTagName(tag);
            tagVo.setData(tagMaterial.get(tag));
            tagVos.add(tagVo);
        }
        return MaterialTypeListVo.builder().typeName(dto.getMaterialType()).tagList(tagVos).build();
    }

    @Override
    public Integer getPublicMaterialNumber(List<String> materialTypeList) {
        return baseDao.getPublicMaterialNumber(materialTypeList);
    }

    @Override
    public MaterialEntity copyMaterial(Long materialId, Long planId) {
        SysUserDto userEntity = ShiroUtils.getUserEntity();
        MaterialEntity materialEntity = baseDao.selectById(materialId);
        materialEntity.setId(null);
        materialEntity.setIsDelete(0);
        materialEntity.setMaterialAttribution(userEntity.getUsername());
        materialEntity.setCreator(userEntity.getId());
        materialEntity.setCreateDate(LocalDateTime.now());
        materialEntity.setCreateName(userEntity.getUsername());
        materialEntity.setUpdater(userEntity.getId());
        materialEntity.setUpdateDate(LocalDateTime.now());
        materialEntity.setUpdateName(userEntity.getUsername());
        materialEntity.setUserId(userEntity.getId());
        if (planId != null) {
            materialEntity.setExperimentPlanId(planId);
        }
        baseDao.insert(materialEntity);
        return materialEntity;
    }

}