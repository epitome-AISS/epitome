package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.BasicModelDao;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.BasicModelEntity;
import com.nbtech.ailab.biz.entity.MaterialEntity;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.constant.CommonConstant;
import com.nbtech.ailab.constant.FlowStatus;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.BasicModelVo;
import com.nbtech.ailab.vo.TagVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.ModelDao;
import com.nbtech.ailab.biz.entity.ModelEntity;
import com.nbtech.ailab.biz.service.IModelService;
import com.nbtech.ailab.util.BlankStringUtil;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 模型对话管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-11
 */
@Service
public class ModelServiceImpl extends CrudServiceImpl<ModelDao, ModelEntity, ModelDto> implements IModelService {
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private BasicModelDao basicModelDao;

    @Override
    public QueryWrapper<ModelEntity> getWrapper(ModelDto dto) {

        QueryWrapper<ModelEntity> wrapper = new QueryWrapper<>();
        wrapper.like(dto.getTag() != null, "tag", dto.getTag());
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    /**
     * 根据名词查找模型 判断模型是否存在
     *
     * @param modelName modelName
     * @return model
     */
    @Override
    public ModelDto getByModelName(String modelName) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        ModelEntity modelEntity = this.baseDao.selectOne(
                Wrappers.<ModelEntity>lambdaQuery()
                        .eq(ModelEntity::getModelName, modelName)
                        .eq(ModelEntity::getAttribution, userName)
                        .last("limit 1"));
        return ConvertUtils.sourceToTarget(modelEntity, ModelDto.class);
    }

    @Override
    public List<TagVo> listVo(ModelDto dto) {
        // 校验 experimentPlanId 必填
        if (dto.getExperimentPlanId() == null) {
            throw new BizException(BizResponseCodeEnum.MODEL_LIST_EXPERIMENT_PLAN_ID_NOT_EMPTY);
        }

        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        List<ModelEntity> finalList = new ArrayList<>();
        // 是本人的 且不是草稿状态的模型机器人
        List<ModelEntity> list = this.baseDao.selectList(
                Wrappers.<ModelEntity>lambdaQuery()
                        .eq(ModelEntity::getAttribution, userName)
                        .eq(ModelEntity::getExperimentPlanId, dto.getExperimentPlanId()));
        finalList.addAll(list);
        // 开源的模型机器人
        List<ModelEntity> openList = this.baseDao.selectList(
                Wrappers.<ModelEntity>lambdaQuery()
                        .eq(ModelEntity::getExperimentPlanId, dto.getExperimentPlanId()));
        finalList.addAll(openList);
        List<ModelEntity> modelList = finalList.stream().distinct().collect(Collectors.toList());
        List<TagVo> tagVoList = new ArrayList<>();

        Map<String, List<ModelEntity>> tagModel = modelList.stream()
                .filter(modelEntity -> modelEntity.getTag() != null)
                .collect(Collectors.groupingBy(ModelEntity::getTag,
                        Collectors.mapping(Function.identity(), Collectors.toList())));

        for (String tag : tagModel.keySet()) {
            TagVo tagVo = new TagVo();
            tagVo.setTagName(tag);
            tagVo.setData(tagModel.get(tag));
            tagVoList.add(tagVo);
        }

        List<ModelEntity> nullModel = modelList.stream()
                .filter(modelEntity -> modelEntity.getTag() == null)
                .collect(Collectors.toList());
        if (!nullModel.isEmpty()) {
            TagVo tagVo = new TagVo();
            tagVo.setTagName("none");
            tagVo.setData(nullModel);
            tagVoList.add(tagVo);
        }

        return tagVoList;
    }

    /**
     * 模型分页展示
     *
     * @param pageDto pageDto
     * @param dto     dto
     * @return 模型
     */
    @Override
    public PageResult<ModelDto> pageModel(PageDto pageDto, ModelDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        Page<ModelDto> page = this.baseDao.pageModel(Page.of(pageDto.getCurrent(), pageDto.getSize()), dto, userName,
                roleId);
        return PageResult.build(page);
    }

    /**
     * 根据id删除模型对话
     *
     * @param id id
     */
    @Override
    public void deleteById(Long id) {
        this.baseDao.deleteById(id);
    }

    /**
     * 审核者模型对话分页
     *
     * @param pageDto pageDto
     * @param dto     dto
     * @return 模型
     */
    @Override
    public PageResult<ModelDto> pageAudit(PageDto pageDto, ModelDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        Page<ModelDto> page = this.baseDao.pageAudit(Page.of(pageDto.getCurrent(), pageDto.getSize()), dto, userName,
                roleId);
        return PageResult.build(page);
    }

    @Override
    public ModelDto getById(Long id) {
        ModelDto data;
        try {
            data = get(id);
            ObjectMapper mapper = new ObjectMapper();
            List<BasicModelVo> basicModelVos = mapper.readValue(data.getModels(),
                    mapper.getTypeFactory().constructCollectionType(List.class, BasicModelVo.class));
            basicModelVos.forEach(x -> {
                BasicModelEntity basicModelEntity = basicModelDao.selectById(x.getId());
                x.setApiKey(basicModelEntity.getApiKey());
                x.setName(basicModelEntity.getName());
                x.setType(basicModelEntity.getType());
                x.setContextLength(basicModelEntity.getContextLength());
                x.setUrl(basicModelEntity.getUrl());
            });
            data.setModels(mapper.writeValueAsString(basicModelVos));
        } catch (Exception e) {
            throw new BizException(BizResponseCodeEnum.GET_MODE_ERROR, id);
        }
        return data;
    }

    @Override
    public List<ModelDto> openList(Integer modelBotType) {
        List<ModelEntity> openList = this.baseDao.selectList(
                Wrappers.<ModelEntity>lambdaQuery()
                        .eq(ModelEntity::getModelStatus, "OPEN")
                        .eq(ModelEntity::getModelBotType, modelBotType));
        return ConvertUtils.sourceToTarget(openList, ModelDto.class);
    }

    @Override
    public List<String> getModelTags() {
        return baseDao.getModelTags();
    }

    @Override
    public PageResult<ModelDto> pagePublic(PageDto pageDto, String attribution) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<ModelDto> result = baseDao.pagePublicWithFavorite(page, userId, attribution);
        // MyBatis 会自动将 SQL 中的 0/1 转换为 Boolean false/true
        return PageResult.build(result, result.getRecords());
    }

    @Override
    public ModelEntity copyModel(Long modelId, Long planId) {
        SysUserDto userEntity = ShiroUtils.getUserEntity();
        // 已删除的模型也要复制出来
        ModelEntity modelEntity = baseDao.getModelById(modelId);
        modelEntity.setId(null);

        modelEntity.setIsDelete(0);
        modelEntity.setModelName(modelEntity.getModelName() + "-1");
        modelEntity.setAttribution(userEntity.getUsername());

        modelEntity.setAttribution(userEntity.getUsername());
        modelEntity.setUserId(userEntity.getId());
        Long roleId = sysUserRoleService.getByUserId(userEntity.getId());
        modelEntity.setRoleId(roleId);
        modelEntity.setModelStatus(CommonConstant.DRAFT);
        modelEntity.setWorkFlow(FlowStatus.DISABLE);

        modelEntity.setCreator(userEntity.getId());
        modelEntity.setCreateDate(LocalDateTime.now());
        modelEntity.setCreateName(userEntity.getUsername());
        modelEntity.setUpdater(userEntity.getId());
        modelEntity.setUpdateDate(LocalDateTime.now());
        modelEntity.setUpdateName(userEntity.getUsername());
        modelEntity.setUserId(userEntity.getId());

        modelEntity.setExperimentPlanId(planId);

        baseDao.insert(modelEntity);
        return modelEntity;
    }

    @Override
    public List<ModelDto> listAll(ModelDto dto) {
        return baseDao.listAll(dto);
    }

}